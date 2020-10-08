import implementations.EndpointSpecBuilder
import implementations.HeaderSpecBuilder
import implementations.MethodSpecBuilder
import implementations.QueryParamSpecBuilder
import implementations.RequestSpecBuilder
import implementations.ResponseSpecBuilder
import implementations.StatusSpecBuilder
import interfaces.EndpointSpec
import interfaces.HeaderSpec
import interfaces.MethodSpec
import interfaces.QueryParamSpec
import interfaces.RequestSpec
import interfaces.ResponseSpec
import interfaces.StatusSpec
import spock.lang.Specification

class EndpointTest extends Specification {

    def "endpoint builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new QueryParamSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()
        def newEndpointBuilder = new EndpointSpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setValue("headerRequestBody").setMandatory(false).build()
        QueryParamSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("queryType").setValue("queryBody").setMandatory(false).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).build()

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setValue("headerResponseBody").setMandatory(false).build()
        StatusSpec newStatus = newStatusBuilder.setCode("statusCode").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).build()

        MethodSpec newMethod = newMethodBuilder.setType("GET").setRequest(newRequest).setResponse(newResponse).build()

        EndpointSpec newEndpoint = newEndpointBuilder.setLabel("endpointLabel").setPath("/test").addDescription("endpointDoc").addMethod(newMethod).build()

        then:

        newEndpoint.getLabel() == "endpointLabel"
        newEndpoint.getPath() == "/test"
        newEndpoint.getDescription() == "endpointDoc"

        newEndpoint.getMethods()[0]

        newEndpoint.getMethods()[0].getType().name() == "GET"

        newEndpoint.getMethods()[0].getRequest().getHeaders()[0].getName() == "headerRequestName"
        newEndpoint.getMethods()[0].getRequest().getHeaders()[0].getValue() == "headerRequestBody"
        !newEndpoint.getMethods()[0].getRequest().getHeaders()[0].isMandatory()
        newEndpoint.getMethods()[0].getRequest().getQueryParams()[0].getName() == "queryName"
        newEndpoint.getMethods()[0].getRequest().getQueryParams()[0].getType() == "queryType"
        newEndpoint.getMethods()[0].getRequest().getQueryParams()[0].getValue() == "queryBody"
        !newEndpoint.getMethods()[0].getRequest().getQueryParams()[0].isMandatory()

        newEndpoint.getMethods()[0].getResponse().getHeaders()[0].getName() == "headerResponseName"
        newEndpoint.getMethods()[0].getResponse().getHeaders()[0].getValue() == "headerResponseBody"
        !newEndpoint.getMethods()[0].getResponse().getHeaders()[0].isMandatory()
        newEndpoint.getMethods()[0].getResponse().getStatuses()[0].getCode() == "statusCode"
        newEndpoint.getMethods()[0].getResponse().getStatuses()[0].getBody() == "statusBody"

    }

    def "endpoint builder raises exception when missing path"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new QueryParamSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()
        def newEndpointBuilder = new EndpointSpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setValue("headerRequestBody").setMandatory(false).build()
        QueryParamSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("queryType").setValue("queryBody").setMandatory(false).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).build()

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setValue("headerResponseBody").setMandatory(false).build()
        StatusSpec newStatus = newStatusBuilder.setCode("statusCode").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).build()

        MethodSpec newMethod = newMethodBuilder.setType("GET").setRequest(newRequest).setResponse(newResponse).build()

        newEndpointBuilder.setLabel("endpointLabel").addDescription("endpointDoc").addMethod(newMethod).build()

        then:
        thrown RuntimeException

    }

    def "endpoint builder raises exception when missing method"() {

        def newEndpointBuilder = new EndpointSpecBuilder()

        when:

        newEndpointBuilder.setLabel("endpointLabel").setPath("/test").addDescription("endpointDoc").build()

        then:
        thrown RuntimeException

    }

}
