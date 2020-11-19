package restapispec

import org.pavlos.restapispec.implementations.APISpecBuilder
import org.pavlos.restapispec.implementations.EndpointSpecBuilder
import org.pavlos.restapispec.implementations.HeaderSpecBuilder
import org.pavlos.restapispec.implementations.MethodSpecBuilder
import org.pavlos.restapispec.implementations.ParamSpecBuilder
import org.pavlos.restapispec.implementations.RequestJSONSpecBuilder
import org.pavlos.restapispec.implementations.ResponseSpecBuilder
import org.pavlos.restapispec.implementations.StatusSpecBuilder
import org.pavlos.restapispec.interfaces.APISpec
import org.pavlos.restapispec.interfaces.EndpointSpec
import org.pavlos.restapispec.interfaces.HeaderSpec
import org.pavlos.restapispec.interfaces.MethodSpec
import org.pavlos.restapispec.interfaces.ParameterSpec
import org.pavlos.restapispec.interfaces.RequestSpec
import org.pavlos.restapispec.interfaces.ResponseSpec
import org.pavlos.restapispec.interfaces.StatusSpec
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
        def newRequestBuilder = new RequestJSONSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()
        def newEndpointBuilder = new EndpointSpecBuilder()
        def newAPIBuilder = new APISpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setMandatory(true).build()
        ParameterSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("String").setMandatory(true).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).build()

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setMandatory(true).build()
        StatusSpec newStatus = newStatusBuilder.setCode("200").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).addResponseBodySchema("JSON").build()

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
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getHeaders()[0].getMandatory()
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getQueryParams()[0].getName() == "queryName"
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getQueryParams()[0].getType() == "String"
        newAPI.getEndpoints()[0].getMethods()[0].getRequest().getQueryParams()[0].isMandatory()

        newAPI.getEndpoints()[0].getMethods()[0].getResponse().getHeaders()[0].getName() == "headerResponseName"
        newAPI.getEndpoints()[0].getMethods()[0].getResponse().getHeaders()[0].getMandatory()
    }


    def "API builder raises exception when missing GroovyApiSpecBuilder.endpoint"() {

        def newAPIBuilder = new APISpecBuilder()

        when:
        newAPIBuilder.setLabel("api").setBaseUrl("https://www.myapi.com/api/").build()

        then:
        thrown RuntimeException

    }

}
