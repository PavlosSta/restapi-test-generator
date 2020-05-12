import implementations.*;
import interfaces.*;

import java.util.function.Predicate;

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
				.setLabel("statusLabel")
				.setBody("statusBody")
				.build();

		HeaderSpec newHeader = newHeaderBuilder
				.setName("headerName")
				.setBody("headerBody")
				.setDefaultBody("headerDefBody")
				.setMandatory(true)
				.build();

		QueryParamSpec newQueryParam = newQueryParamBuilder
				.setName("queryName")
				.setType("queryType")
				.setBody("queryBody")
				.setDefaultBody("queryDefBody")
				.setMandatory(true)
				.build();

		RequestSpec newRequest = newRequestBuilder.
				addHeader(newHeader).
				addQueryParam(newQueryParam).
				setJwt("jwt").
				build();

		ResponseSpec newResponse = newResponseBuilder
				.addHeader(newHeader)
				.addStatus(newStatus)
				.build();

		MethodSpec newMethod = newMethodBuilder
				.setType("")
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
				.setBaseUrl("/api")
				.addEndpoint(newEndpoint)
				.build();

		System.out.println(newAPI.getBaseUrl());
	}
}