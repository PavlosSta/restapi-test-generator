package restapispec

import implementations.HeaderSpecBuilder
import implementations.MethodSpecBuilder
import implementations.ParamSpecBuilder
import implementations.RequestSpecBuilder
import implementations.ResponseSpecBuilder
import implementations.StatusSpecBuilder
import interfaces.HeaderSpec
import interfaces.MethodSpec
import interfaces.ParameterSpec
import interfaces.RequestSpec
import interfaces.ResponseSpec
import interfaces.StatusSpec
import spock.lang.Specification

class MethodTest extends Specification {

    def "method builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new ParamSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder("JSON")
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setMandatory(true).build()
        ParameterSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("String").setMandatory(true).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).build()

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setMandatory(true).build()
        StatusSpec newStatus = newStatusBuilder.setCode("200").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).addResponseBodySchema("JSON").build()

        MethodSpec newMethod = newMethodBuilder.setType("GET").setRequest(newRequest).setResponse(newResponse).build()

        then:

        newMethod.getType().name() == "GET"

        newMethod.getRequest().getHeaders()[0].getName() == "headerRequestName"
        newMethod.getRequest().getHeaders()[0].isMandatory()
        newMethod.getRequest().getQueryParams()[0].getName() == "queryName"
        newMethod.getRequest().getQueryParams()[0].getType() == "String"
        newMethod.getRequest().getQueryParams()[0].isMandatory()

        newMethod.getResponse().getHeaders()[0].getName() == "headerResponseName"
        newMethod.getResponse().getHeaders()[0].isMandatory()

    }

    def "method builder raises exception when missing type"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new ParamSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder("URL")
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setMandatory(false).build()
        ParameterSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("String").setMandatory(false).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).build()

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setMandatory(false).build()
        StatusSpec newStatus = newStatusBuilder.setCode("statusCode").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).build()

        newMethodBuilder.setRequest(newRequest).setResponse(newResponse).build()

        then:
        thrown RuntimeException

    }

    def "method builder raises exception when missing request"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()

        when:

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setMandatory(false).build()
        StatusSpec newStatus = newStatusBuilder.setCode("statusCode").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).build()

        newMethodBuilder.setType("GET").setResponse(newResponse).build()

        then:
        thrown RuntimeException

    }

    def "method builder raises exception when missing response"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newQueryParamBuilder = new ParamSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder("URL")
        def newMethodBuilder = new MethodSpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setMandatory(false).build()
        ParameterSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("String").setMandatory(false).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).build()

        newMethodBuilder.setType("GET").setRequest(newRequest).build()

        then:
        thrown RuntimeException

    }

}
