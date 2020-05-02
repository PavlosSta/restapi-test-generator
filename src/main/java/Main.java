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

		Predicate<String> statusCode = st -> (st.equals(200));

		StatusSpec newStatus = newStatusBuilder
				.setLabel("statusLabel")
				.setBody("statusBody")
				.setConditionBody(statusCode)
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
				addHeaders(newHeader).
				addQueryParams(newQueryParam).
				setJwt("jwt").
				build();

		ResponseSpec newResponse = newResponseBuilder
				.addHeaders(newHeader)
				.addStatuses(newStatus)
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
				.setLabel("")
				.setBaseUrl("/api")
				.addEndpoint(newEndpoint)
				.build();

		System.out.println(newAPI.getBaseUrl());
	}

	public boolean test()
	{
		return true;
	}
}