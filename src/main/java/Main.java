import implementations.*;
import interfaces.*;

public class Main
{
	public static void main(String[] args)
	{

		//TODO java template engines!

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
				.setBaseUrl("/api")
				.addEndpoint(newEndpoint)
				.build();

		System.out.println(newAPI.getBaseUrl());
	}
}