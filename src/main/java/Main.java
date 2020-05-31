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

		APISpecBuilder newApiBuilder;
		EndpointSpecBuilder newEndpointBuilder;
		HeaderSpecBuilder newHeaderBuilder;
		MethodSpecBuilder newMethodBuilder;
		QueryParamSpecBuilder newQueryParamBuilder;
		RequestSpecBuilder newRequestBuilder;
		ResponseSpecBuilder newResponseBuilder;


		StatusSpecBuilder newStatusBuilder = new StatusSpecBuilder();
		StatusSpec newStatus = newStatusBuilder
				.setCode("statusLabel")
				.setBody("statusBody")
				.build();

		newHeaderBuilder = new HeaderSpecBuilder();
		HeaderSpec newHeader = newHeaderBuilder
				.setName("headerName")
				.setValue("headerBody")
				.setDefaultValueIfOptionalAndMissing("headerDefBody")
				.setMandatory(true)
				.build();

		newQueryParamBuilder = new QueryParamSpecBuilder();
		QueryParamSpec newQueryParam = newQueryParamBuilder
				.setName("queryName")
				.setType("queryType")
				.setValue("queryBody")
				.setDefaultValue("queryDefBody")
				.setMandatory(true)
				.build();

		newRequestBuilder = new RequestSpecBuilder();
		RequestSpec newRequest = newRequestBuilder.
				addHeader(newHeader).
				addQueryParam(newQueryParam).
				build();

		newResponseBuilder = new ResponseSpecBuilder();
		ResponseSpec newResponse = newResponseBuilder
				.addHeader(newHeader)
				.addStatus(newStatus)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec newMethodGET = newMethodBuilder
				.setType("GET")
				.setRequest(newRequest)
				.setResponse(newResponse)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec newMethodPOST = newMethodBuilder
				.setType("POST")
				.setRequest(newRequest)
				.setResponse(newResponse)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec newMethodPUT = newMethodBuilder
				.setType("PUT")
				.setRequest(newRequest)
				.setResponse(newResponse)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec newMethodPATCH = newMethodBuilder
				.setType("PATCH")
				.setRequest(newRequest)
				.setResponse(newResponse)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec newMethodDELETE = newMethodBuilder
				.setType("DELETE")
				.setRequest(newRequest)
				.setResponse(newResponse)
				.build();

		newEndpointBuilder = new EndpointSpecBuilder();
		EndpointSpec newEndpointWithAttribute = newEndpointBuilder
				.setPath("/test")
				.setLabel("test")
				.addAttribute("id")
				.addMethod(newMethodGET)
				.addMethod(newMethodPUT)
				.addMethod(newMethodPATCH)
				.addMethod(newMethodDELETE)
				.build();

		newEndpointBuilder = new EndpointSpecBuilder();
		EndpointSpec newEndpointWithoutAttribute = newEndpointBuilder
				.setPath("/test")
				.setLabel("test")
				.addMethod(newMethodGET)
				.addMethod(newMethodPOST)
				.build();

		newApiBuilder = new APISpecBuilder();
		APISpec newAPI = newApiBuilder
				.setLabel("api")
				.setBaseUrl("https://www.myapi.gr")
				.addEndpoint(newEndpointWithAttribute)
				.addEndpoint(newEndpointWithoutAttribute)
				.build();

		// Template engine / Code generation
	//	FreeMarkerJavaCodeGenerator javaGenerator = new FreeMarkerJavaCodeGenerator(newAPI);

	//	javaGenerator.generate(new File("src/main/java/client/RestAPI.java"), new File("src/main/java/client/tests/TestClient.groovy"));

	}
}