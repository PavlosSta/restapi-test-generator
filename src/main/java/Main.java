import client.generator.FreeMarkerJavaCodeGenerator;
import implementations.*;
import interfaces.*;

import java.io.File;

public class Main
{
	public static void main(String[] args) {

		APISpecBuilder newApiBuilder;
		EndpointSpecBuilder newEndpointBuilder;
		HeaderSpecBuilder newHeaderBuilder;
		MethodSpecBuilder newMethodBuilder;
		ParamSpecBuilder newParamBuilder;
		RequestJSONSpecBuilder newRequestJSONBuilder;
		RequestURLSpecBuilder newRequestURLBuilder;
		ResponseSpecBuilder newResponseBuilder;

		// Positive Statuses

		StatusSpecBuilder newStatusBuilder = new StatusSpecBuilder();
		StatusSpec status200 = newStatusBuilder
				.setCode("200")
				.setBody("OK")
				.build();

		StatusSpec status201 = newStatusBuilder
				.setCode("201")
				.setBody("Created")
				.build();

		// Login

		newParamBuilder = new ParamSpecBuilder();
		ParameterSpec usernameBodyParam = newParamBuilder
				.setName("username")
				.setType("String")
				.setMandatory(true)
				.build();
		ParameterSpec passwordBodyParam = newParamBuilder
				.setType("String")
				.setName("password")
				.setMandatory(true)
				.build();

		newRequestURLBuilder = new RequestURLSpecBuilder();
		RequestSpec loginRequest = newRequestURLBuilder
				.addBodyParam(usernameBodyParam)
				.addBodyParam(passwordBodyParam)
				.build();

		newHeaderBuilder = new HeaderSpecBuilder();
		HeaderSpec authenticationTokenHeader = newHeaderBuilder
				.setName("X-OBSERVATORY-AUTH")
				.setMandatory(true)
				.build();

		newResponseBuilder = new ResponseSpecBuilder();
		ResponseSpec loginResponse = newResponseBuilder
				.addHeader(authenticationTokenHeader)
				.addStatus(status201)
				.addResponseBodySchema("JSON")
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec loginMethodPOST = newMethodBuilder
				.setType("POST")
				.setRequest(loginRequest)
				.setResponse(loginResponse)
				.build();

		newEndpointBuilder = new EndpointSpecBuilder();
		EndpointSpec loginEndpoint = newEndpointBuilder
				.setPath("/login")
				.setLabel("login endpoint")
				.addMethod(loginMethodPOST)
				.build();

		// Logout

		newRequestURLBuilder = new RequestURLSpecBuilder();
		RequestSpec logoutRequest = newRequestURLBuilder
				.addHeader(authenticationTokenHeader)
				.build();

		newResponseBuilder = new ResponseSpecBuilder();
		ResponseSpec logoutResponse = newResponseBuilder
				.addResponseBodySchema("JSON")
				.addStatus(status201)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec logoutMethodPOST = newMethodBuilder
				.setType("POST")
				.setRequest(logoutRequest)
				.setResponse(logoutResponse)
				.build();

		newEndpointBuilder = new EndpointSpecBuilder();
		EndpointSpec logoutEndpoint = newEndpointBuilder
				.setPath("/logout")
				.setLabel("logout endpoint")
				.addMethod(logoutMethodPOST)
				.build();


		// Products

		// GET Products

		newParamBuilder = new ParamSpecBuilder();
		ParameterSpec getProductsStartParam = newParamBuilder
				.setName("start")
				.setType("Integer")
				.setDefaultValue("0")
				.setMandatory(false)
				.build();

		newParamBuilder = new ParamSpecBuilder();
		ParameterSpec getProductsCountParam = newParamBuilder
				.setName("count")
				.setType("Integer")
				.setDefaultValue("20")
				.setMandatory(false)
				.build();

		newParamBuilder = new ParamSpecBuilder();
		ParameterSpec getProductsStatusParam = newParamBuilder
				.setName("status")
				.setType("String")
				.setDefaultValue("ACTIVE")
				.setMandatory(false)
				.build();

		newParamBuilder = new ParamSpecBuilder();
		ParameterSpec getProductsSortParam = newParamBuilder
				.setName("sort")
				.setType("String")
				.setDefaultValue("id|DESC")
				.setMandatory(false)
				.build();

		newRequestJSONBuilder = new RequestJSONSpecBuilder();
		RequestJSONSpec getProductsRequest = newRequestJSONBuilder
				.addQueryParam(getProductsStartParam)
				.addQueryParam(getProductsCountParam)
				.addQueryParam(getProductsStatusParam)
				.addQueryParam(getProductsSortParam)
				.build();

		newResponseBuilder = new ResponseSpecBuilder();
		ResponseSpec getProductsResponse = newResponseBuilder
				.addResponseBodySchema("String")
				.addStatus(status200)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec getProductsMethod = newMethodBuilder
				.setType("GET")
				.setRequest(getProductsRequest)
				.setResponse(getProductsResponse)
				.build();

		// POST Products

		newParamBuilder = new ParamSpecBuilder();
		ParameterSpec postProductsNameParam = newParamBuilder
				.setName("name")
				.setType("String")
				.setMandatory(true)
				.build();

		newParamBuilder = new ParamSpecBuilder();
		ParameterSpec postProductsDescriptionParam = newParamBuilder
				.setName("description")
				.setType("String")
				.setMandatory(true)
				.build();

		newParamBuilder = new ParamSpecBuilder();
		ParameterSpec postProductsCategoryParam = newParamBuilder
				.setName("category")
				.setType("String")
				.setMandatory(true)
				.build();

		newParamBuilder = new ParamSpecBuilder();
		ParameterSpec postProductsTagsParam = newParamBuilder
				.setName("tags")
				.setType("String")
				.setMandatory(true)
				.build();

		newRequestURLBuilder = new RequestURLSpecBuilder();
		RequestSpec postProductsRequest = newRequestURLBuilder
				.addBodyParam(postProductsNameParam)
				.addBodyParam(postProductsDescriptionParam)
				.addBodyParam(postProductsCategoryParam)
				.addBodyParam(postProductsTagsParam)
				.build();

		newResponseBuilder = new ResponseSpecBuilder();
		ResponseSpec postProductsResponse = newResponseBuilder
				.addResponseBodySchema("Integer")
				.addStatus(status201)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec postProductsMethod = newMethodBuilder
				.setType("POST")
				.setRequest(postProductsRequest)
				.setResponse(postProductsResponse)
				.build();

		// GET Products by id

		newRequestJSONBuilder = new RequestJSONSpecBuilder();
		RequestSpec getProductsByIdRequest = newRequestJSONBuilder
				.build();

		newResponseBuilder = new ResponseSpecBuilder();
		ResponseSpec getProductsByIdResponse = newResponseBuilder
				.addResponseBodySchema("JSON")
				.addStatus(status200)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec getProductsByIdMethod = newMethodBuilder
				.setType("GET")
				.setRequest(getProductsByIdRequest)
				.setResponse(getProductsByIdResponse)
				.build();

		// PUT Products by id

		newRequestURLBuilder = new RequestURLSpecBuilder();
		RequestSpec putProductsByIdRequest = newRequestURLBuilder
				.addBodyParam(postProductsNameParam)
				.addBodyParam(postProductsDescriptionParam)
				.addBodyParam(postProductsCategoryParam)
				.addBodyParam(postProductsTagsParam)
				.build();

		newResponseBuilder = new ResponseSpecBuilder();
		ResponseSpec putProductsByIdResponse = newResponseBuilder
				.addResponseBodySchema("JSON")
				.addStatus(status201)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec putProductsByIdMethod = newMethodBuilder
				.setType("PUT")
				.setRequest(putProductsByIdRequest)
				.setResponse(putProductsByIdResponse)
				.build();

		// PATCH Products by id

		newRequestURLBuilder = new RequestURLSpecBuilder();
		RequestSpec patchProductsByIdRequest = newRequestURLBuilder
				.addBodyParam(postProductsNameParam)
				.addBodyParam(postProductsDescriptionParam)
				.addBodyParam(postProductsCategoryParam)
				.addBodyParam(postProductsTagsParam)
				.build();

		newResponseBuilder = new ResponseSpecBuilder();
		ResponseSpec patchProductsByIdResponse = newResponseBuilder
				.addResponseBodySchema("JSON")
				.addStatus(status201)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec patchProductsByIdMethod = newMethodBuilder
				.setType("PATCH")
				.setRequest(patchProductsByIdRequest)
				.setResponse(patchProductsByIdResponse)
				.build();

		// DELETE Products by id

		newRequestURLBuilder = new RequestURLSpecBuilder();
		RequestSpec deleteProductsByIdRequest = newRequestURLBuilder
				.addHeader(authenticationTokenHeader)
				.build();

		newResponseBuilder = new ResponseSpecBuilder();
		ResponseSpec deleteProductsByIdResponse = newResponseBuilder
				.addResponseBodySchema("JSON")
				.addStatus(status201)
				.build();

		newMethodBuilder = new MethodSpecBuilder();
		MethodSpec deleteProductsByIdMethod = newMethodBuilder
				.setType("DELETE")
				.setRequest(deleteProductsByIdRequest)
				.setResponse(deleteProductsByIdResponse)
				.build();

		// Endpoint Products

		newEndpointBuilder = new EndpointSpecBuilder();
		EndpointSpec productsWithoutAttribute = newEndpointBuilder
				.setPath("/products")
				.setLabel("endpoint for products without attribute")
				.addMethod(getProductsMethod)
				.addMethod(postProductsMethod)
				.build();

		// Endpoint Products By Id

		newEndpointBuilder = new EndpointSpecBuilder();
		EndpointSpec productsWithAttribute = newEndpointBuilder
				.setPath("/products")
				.setLabel("endpoint for products with attribute")
				.addMethod(getProductsByIdMethod)
				.addMethod(putProductsByIdMethod)
				.addMethod(patchProductsByIdMethod)
				.addMethod(deleteProductsByIdMethod)
				.addAttribute("id")
				.build();


		// API Build

		newApiBuilder = new APISpecBuilder();
		APISpec newAPI = newApiBuilder
				.setLabel("api")
				.setBaseUrl("/observatory/api")
				.addEndpoint(loginEndpoint)
				.addEndpoint(logoutEndpoint)
				.addEndpoint(productsWithAttribute)
				.addEndpoint(productsWithoutAttribute)
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
		javaGenerator.generateClient(new File("src/main/java/client/RestAPIClient.java"), new File(clientPath));
		javaGenerator.generateServer(new File(serverPath));


	//	System.out.println("Client tests saved at: " + clientPath);
	//	System.out.println("Server tests saved at: " + serverPath);

		// buildSrc

	}

}