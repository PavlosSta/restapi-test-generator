import freemarker.template.*;
import implementations.*;
import interfaces.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main
{
	public static void main(String[] args)
	{

		APISpecBuilder newApiBuilder 				= new APISpecBuilder();
		EndpointSpecBuilder newEndpointBuilder		= new EndpointSpecBuilder();
		HeaderSpecBuilder newHeaderBuilder 			= new HeaderSpecBuilder();
		MethodSpecBuilder newMethodBuilder 			= new MethodSpecBuilder();
		QueryParamSpecBuilder newQueryParamBuilder 	= new QueryParamSpecBuilder();
		RequestSpecBuilder newRequestBuilder 		= new RequestSpecBuilder();
		ResponseSpecBuilder newResponseBuilder		= new ResponseSpecBuilder();
		StatusSpecBuilder newStatusBuilder			= new StatusSpecBuilder();

		StatusSpec newStatus = newStatusBuilder
				.setCode("statusLabel")
				.setBody("statusBody")
				.build();

		HeaderSpec newHeader = newHeaderBuilder
				.setName("headerName")
				.setValue("headerBody")
				.setDefaultValueIfOptionalAndMissing("headerDefBody")
				.setMandatory(true)
				.build();

		QueryParamSpec newQueryParam = newQueryParamBuilder
				.setName("queryName")
				.setType("queryType")
				.setValue("queryBody")
				.setDefaultValue("queryDefBody")
				.setMandatory(true)
				.build();

		RequestSpec newRequest = newRequestBuilder.
				addHeader(newHeader).
				addQueryParam(newQueryParam).
				build();

		ResponseSpec newResponse = newResponseBuilder
				.addHeader(newHeader)
				.addStatus(newStatus)
				.build();

		MethodSpec newMethod = newMethodBuilder
				.setType("GET")
				.setRequest(newRequest)
				.setResponse(newResponse)
				.build();

		EndpointSpec newEndpoint = newEndpointBuilder
				.setPath("/test")
				.setLabel("test endpoint")
				.addMethod(newMethod)
				.build();

		APISpec newAPI = newApiBuilder
				.setLabel("api")
				.setBaseUrl("https://www.myapi.gr")
				.addEndpoint(newEndpoint)
				.build();

		// Creates a Configuration instance
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

		cfg.setClassForTemplateLoading(Main.class, "templates");

		// Specifies the source where the template files come from.
		try {
			cfg.setDirectoryForTemplateLoading(new File("src/main/java/client/templates"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		cfg.setDefaultEncoding("UTF-8");

		// Sets how errors will appear.
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
		cfg.setLogTemplateExceptions(false);

		// Wrap unchecked exceptions thrown during template processing into TemplateException-s:
		cfg.setWrapUncheckedExceptions(true);

		// Do not fall back to higher scopes when reading a null loop variable:
		cfg.setFallbackOnNullLoopVariable(false);

		/* Create a data-model */
		Map<String, APISpec> root = new HashMap<>();
		root.put("api", newAPI);

		// Gets the template
		try {
			Template temp = cfg.getTemplate("restapi.ftl");

			// Writes to console
			Writer fileWriter = new FileWriter(new File("src/main/java/client/RestAPI.java"));
			try {
				temp.process(root, fileWriter);
			} finally {
				fileWriter.close();
			}

		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}




	}
}