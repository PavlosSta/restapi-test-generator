import implementations.APISpecBuilder
import implementations.EndpointSpecBuilder
import implementations.HeaderSpecBuilder
import implementations.MethodSpecBuilder
import implementations.ParamSpecBuilder
import implementations.RequestGenericSpecBuilder
import implementations.ResponseSpecBuilder
import implementations.StatusSpecBuilder
import interfaces.APISpec
import interfaces.EndpointSpec
import interfaces.HeaderSpec
import interfaces.MethodSpec
import interfaces.ParameterSpec
import interfaces.RequestSpec
import interfaces.ResponseSpec
import interfaces.StatusSpec
import spock.lang.Specification

class APITest extends Specification {

    def "baseUrl validation works"() {
        def API = new APISpecBuilder()

        when:
        Boolean resultNotValid = API.validateUrl("https://rest.api")
        Boolean resultValid = API.validateUrl("/rest/api")

        then:
        !resultNotValid
        resultValid
    }

    def "API builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new ParamSpecBuilder()
        def newRequestBuilder = new RequestGenericSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()
        def newEndpointBuilder = new EndpointSpecBuilder()
        def newAPIBuilder = new APISpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setMandatory(true).build()
        ParameterSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("String").setMandatory(true).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).build()

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setMandatory(true).build()
        StatusSpec newStatus = newStatusBuilder.setCode("statusCode").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).build()

        MethodSpec newMethod = newMethodBuilder.setType("GET").setRequest(newRequest).setResponse(newResponse).build()

        EndpointSpec newEndpoint = newEndpointBuilder.setLabel("endpointLabel").setPath("/test").addDescription("endpointDoc").addMethod(newMethod).build()

        APISpec newAPI = newAPIBuilder.setLabel("api").setBaseUrl("/api").addEndpoint(newEndpoint).build()

        then:

        newAPI.getLabel() == "api"
        newAPI.getBaseUrl() == "/api"

        newAPI.getEndpoints()[0].getLabel() == "endpointLabel"
        newAPI.getEndpoints()[0].getPath() == "/test"
        newAPI.getEndpoints()[0].getDescription() == "endpointDoc"

        newAPI.getEndpoints()[0].getMethods()[0]

        newAPI.getEndpoints()[0].getMethods()[0].getType().name() == "GET"

        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getHeaders()[0].getName() == "headerRequestName"
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getHeaders()[0].isMandatory()
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getQueryParams()[0].getName() == "queryName"
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getQueryParams()[0].getType() == "String"
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getQueryParams()[0].isMandatory()

        newAPI.getEndpoints()[0].getMethods()[0].getResponse().getHeaders()[0].getName() == "headerResponseName"
        newAPI.getEndpoints()[0].getMethods()[0].getResponse().getHeaders()[0].isMandatory()
    }


    def "API builder raises exception when missing endpoint"() {

        def newAPIBuilder = new APISpecBuilder()

        when:
        newAPIBuilder.setLabel("api").setBaseUrl("https://www.myapi.com/api/").build()

        then:
        thrown RuntimeException

    }

}
