import client.generator.FreeMarkerJavaCodeGenerator;
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

		MethodSpec newMethodGET = newMethodBuilder
				.setType("GET")
				.setRequest(newRequest)
				.setResponse(newResponse)
				.build();

		MethodSpec newMethodPOST = newMethodBuilder
				.setType("POST")
				.setRequest(newRequest)
				.setResponse(newResponse)
				.build();

		MethodSpec newMethodPUT = newMethodBuilder
				.setType("PUT")
				.setRequest(newRequest)
				.setResponse(newResponse)
				.build();

		MethodSpec newMethodPATCH = newMethodBuilder
				.setType("PATCH")
				.setRequest(newRequest)
				.setResponse(newResponse)
				.build();

		MethodSpec newMethodDELETE = newMethodBuilder
				.setType("DELETE")
				.setRequest(newRequest)
				.setResponse(newResponse)
				.build();

		EndpointSpec newEndpoint = newEndpointBuilder
				.setPath("/test")
				.setLabel("test")
				.addMethod(newMethodGET)
				.addMethod(newMethodPOST)
				.addMethod(newMethodPUT)
				.addMethod(newMethodPATCH)
				.addMethod(newMethodDELETE)
				.build();

		APISpec newAPI = newApiBuilder
				.setLabel("api")
				.setBaseUrl("https://www.myapi.gr")
				.addEndpoint(newEndpoint)
				.build();

		// Template engine / Code generation
		FreeMarkerJavaCodeGenerator javaGenerator = new FreeMarkerJavaCodeGenerator(newAPI);

		javaGenerator.generate(new File("src/main/java/client/RestAPI.java"), new File("src/main/java/client/tests/TestClient.groovy"));

	}
}