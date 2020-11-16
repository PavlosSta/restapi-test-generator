package restapispec

import org.pavlos.restapispec.implementations.EndpointSpecBuilder
import org.pavlos.restapispec.implementations.HeaderSpecBuilder
import org.pavlos.restapispec.implementations.MethodSpecBuilder
import org.pavlos.restapispec.implementations.ParamSpecBuilder
import org.pavlos.restapispec.implementations.RequestJSONSpecBuilder
import org.pavlos.restapispec.implementations.ResponseSpecBuilder
import org.pavlos.restapispec.implementations.StatusSpecBuilder
import org.pavlos.restapispec.interfaces.EndpointSpec
import org.pavlos.restapispec.interfaces.HeaderSpec
import org.pavlos.restapispec.interfaces.MethodSpec
import org.pavlos.restapispec.interfaces.ParameterSpec
import org.pavlos.restapispec.interfaces.RequestSpec
import org.pavlos.restapispec.interfaces.ResponseSpec
import org.pavlos.restapispec.interfaces.StatusSpec
import spock.lang.Specification

class EndpointTest extends Specification {

    def "GroovyApiSpecBuilder.endpoint builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new ParamSpecBuilder()
        def newRequestBuilder = new RequestJSONSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()
        def newEndpointBuilder = new EndpointSpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setMandatory(true).build()
        ParameterSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("String").setMandatory(true).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).build()

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setMandatory(true).build()
        StatusSpec newStatus = newStatusBuilder.setCode("200").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).addResponseBodySchema("JSON").build()

        MethodSpec newMethod = newMethodBuilder.setType("GET").setRequest(newRequest).setResponse(newResponse).build()

        EndpointSpec newEndpoint = newEndpointBuilder.setLabel("endpointLabel").setPath("/test").addDescription("endpointDoc").addMethod(newMethod).build()

        then:

        newEndpoint.getLabel() == "endpointLabel"
        newEndpoint.getPath() == "/test"
        newEndpoint.getDescription() == "endpointDoc"

        newEndpoint.getMethods()[0]

        newEndpoint.getMethods()[0].getType().name() == "GET"

        newEndpoint.getMethods()[0].getRequest().getHeaders()[0].getName() == "headerRequestName"
        newEndpoint.getMethods()[0].getRequest().getHeaders()[0].isMandatory()
        newEndpoint.getMethods()[0].getRequest().getQueryParams()[0].getName() == "queryName"
        newEndpoint.getMethods()[0].getRequest().getQueryParams()[0].getType() == "String"
        newEndpoint.getMethods()[0].getRequest().getQueryParams()[0].isMandatory()

        newEndpoint.getMethods()[0].getResponse().getHeaders()[0].getName() == "headerResponseName"
        newEndpoint.getMethods()[0].getResponse().getHeaders()[0].isMandatory()

    }

    def "GroovyApiSpecBuilder.endpoint builder raises exception when missing path"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new ParamSpecBuilder()
        def newRequestBuilder = new RequestJSONSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()
        def newEndpointBuilder = new EndpointSpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setMandatory(false).build()
        ParameterSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("String").setMandatory(false).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).build()

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setMandatory(false).build()
        StatusSpec newStatus = newStatusBuilder.setCode("statusCode").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).build()

        MethodSpec newMethod = newMethodBuilder.setType("GET").setRequest(newRequest).setResponse(newResponse).build()

        newEndpointBuilder.setLabel("endpointLabel").addDescription("endpointDoc").addMethod(newMethod).build()

        then:
        thrown RuntimeException

    }

    def "GroovyApiSpecBuilder.endpoint builder raises exception when missing method"() {

        def newEndpointBuilder = new EndpointSpecBuilder()

        when:

        newEndpointBuilder.setLabel("endpointLabel").setPath("/test").addDescription("endpointDoc").build()

        then:
        thrown RuntimeException

    }

}