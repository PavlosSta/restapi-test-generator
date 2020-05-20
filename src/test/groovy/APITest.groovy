import implementations.APISpecBuilder
import implementations.EndpointSpecBuilder
import implementations.HeaderSpecBuilder
import implementations.MethodSpecBuilder
import implementations.QueryParamSpecBuilder
import implementations.RequestSpecBuilder
import implementations.ResponseSpecBuilder
import implementations.StatusSpecBuilder
import interfaces.APISpec
import interfaces.EndpointSpec
import interfaces.HeaderSpec
import interfaces.MethodSpec
import interfaces.QueryParamSpec
import interfaces.RequestSpec
import interfaces.ResponseSpec
import interfaces.StatusSpec
import spock.lang.Specification

class APITest extends Specification {

    def "baseUrl validation works"() {
        def API = new APISpecBuilder()

        when:
        Boolean resultNotValid = API.validateUrl("localhosttest")
        Boolean resultValid = API.validateUrl("https://www.test.com/api")

        then:
        !resultNotValid
        resultValid
    }

    def "API builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new QueryParamSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()
        def newEndpointBuilder = new EndpointSpecBuilder()
        def newAPIBuilder = new APISpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setValue("headerRequestBody").setMandatory(false).build()
        QueryParamSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("queryType").setValue("queryBody").setMandatory(false).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).build()

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setValue("headerResponseBody").setMandatory(false).build()
        StatusSpec newStatus = newStatusBuilder.setCode("statusCode").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).build()

        MethodSpec newMethod = newMethodBuilder.setType("methodType").setRequest(newRequest).setResponse(newResponse).build()

        EndpointSpec newEndpoint = newEndpointBuilder.setLabel("endpointLabel").setPath("/test").addDescription("endpointDoc").addMethod(newMethod).build()

        APISpec newAPI = newAPIBuilder.setLabel("api").setBaseUrl("https://www.myapi.com/api/").addEndpoint(newEndpoint).build()

        then:

        newAPI.getLabel() == "api"
        newAPI.getBaseUrl() == "https://www.myapi.com/api/"

        newAPI.getEndpoints()[0].getLabel() == "endpointLabel"
        newAPI.getEndpoints()[0].getPath() == "/test"
        newAPI.getEndpoints()[0].getDescription() == "endpointDoc"

        newAPI.getEndpoints()[0].getMethods()[0]

        newAPI.getEndpoints()[0].getMethods()[0].getType() == "methodType"

        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getHeaders()[0].getName() == "headerRequestName"
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getHeaders()[0].getValue() == "headerRequestBody"
        !newAPI.getEndpoints()[0].getMethods()[0].getRequest().getHeaders()[0].isMandatory()
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getQueryParams()[0].getName() == "queryName"
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getQueryParams()[0].getType() == "queryType"
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getQueryParams()[0].getValue() == "queryBody"
        !newAPI.getEndpoints()[0].getMethods()[0].getRequest().getQueryParams()[0].isMandatory()

        newAPI.getEndpoints()[0].getMethods()[0].getResponse().getHeaders()[0].getName() == "headerResponseName"
        newAPI.getEndpoints()[0].getMethods()[0].getResponse().getHeaders()[0].getValue() == "headerResponseBody"
        !newAPI.getEndpoints()[0].getMethods()[0].getResponse().getHeaders()[0].isMandatory()
        newAPI.getEndpoints()[0].getMethods()[0].getResponse().getStatuses()[0].getCode() == "statusCode"
        newAPI.getEndpoints()[0].getMethods()[0].getResponse().getStatuses()[0].getBody() == "statusBody"
    }


    def "API builder raises exception when missing endpoint"() {

        def newAPIBuilder = new APISpecBuilder()

        when:
        newAPIBuilder.setLabel("api").setBaseUrl("https://www.myapi.com/api/").build()

        then:
        thrown RuntimeException

    }

}
