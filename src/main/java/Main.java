import client.generator.FreeMarkerJavaCodeGenerator;
import implementations.*;
import interfaces.*;

import java.io.*;
import java.util.Iterator;

public class Main
{
	public static void main(String[] args) {

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
		QueryParamSpec queryParamA1 = newQueryParamBuilder
				.setName("QueryA")
				.setType("String")
				.setValue("3")
				.setDefaultValue("queryDefBody")
				.setMandatory(true)
				.build();
		QueryParamSpec queryParamA2 = newQueryParamBuilder
				.setName("QueryA")
				.setType("String")
				.setValue("5")
				.setDefaultValue("queryDefBody")
				.setMandatory(true)
				.build();
		QueryParamSpec queryParamB = newQueryParamBuilder
				.setName("QueryA")
				.setType("String")
				.setValue("2")
				.setDefaultValue("queryDefBody")
				.setMandatory(true)
				.build();

		newRequestBuilder = new RequestSpecBuilder();
		RequestSpec newRequest = newRequestBuilder
				.addHeader(newHeader)
				.addQueryParam(queryParamA1)
				.addQueryParam(queryParamA2)
				.addQueryParam(queryParamB)
				.build();

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
				.setPath("/products")
				.addAttribute("id")
				.setLabel("endpoint for products with attribute")
				.addMethod(newMethodGET)
				.addMethod(newMethodPUT)
				.addMethod(newMethodPATCH)
				.addMethod(newMethodDELETE)
				.build();

		newEndpointBuilder = new EndpointSpecBuilder();
		EndpointSpec newEndpointWithoutAttribute = newEndpointBuilder
				.setPath("/products")
				.setLabel("endpoint for products without attribute")
				.addMethod(newMethodGET)
				.addMethod(newMethodPOST)
				.build();

		newApiBuilder = new APISpecBuilder();
		APISpec newAPI = newApiBuilder
				.setLabel("api")
				.setBaseUrl("/rest/api")
				.addEndpoint(newEndpointWithAttribute)
				.addEndpoint(newEndpointWithoutAttribute)
				.build();

		// Template engine / Code generation
		FreeMarkerJavaCodeGenerator javaGenerator = new FreeMarkerJavaCodeGenerator(newAPI);

		String clientFolder = "src/test/groovy/";
		String clientPackage = "restapiclient";
		String clientName = "TestClient";
		String clientPath = clientFolder + clientPackage.replaceAll("\\.","/") + "/" + clientName + ".groovy";

		String serverFolder = "src/test/groovy/";
		String serverPackage = "restapiserver";
		String serverName = "TestServer";
		String serverPath = serverFolder + serverPackage.replaceAll("\\.","/") + "/" + serverName + ".groovy";

		//RestAPIClient
		javaGenerator.generateClient(new File("src/main/java/client/RestAPIClient.java"), new File("src/test/groovy/restapiclient/TestClient.groovy"));

		javaGenerator.generateServer(new File("src/test/groovy/restapiserver/TestServer.groovy"));

		// buildSrc

	}

}