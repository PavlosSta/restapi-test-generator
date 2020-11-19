package restapispec

import org.pavlos.restapispec.implementations.HeaderSpecBuilder
import org.pavlos.restapispec.implementations.MethodSpecBuilder
import org.pavlos.restapispec.implementations.ParamSpecBuilder
import org.pavlos.restapispec.implementations.RequestJSONSpecBuilder
import org.pavlos.restapispec.implementations.ResponseSpecBuilder
import org.pavlos.restapispec.implementations.StatusSpecBuilder
import org.pavlos.restapispec.interfaces.HeaderSpec
import org.pavlos.restapispec.interfaces.MethodSpec
import org.pavlos.restapispec.interfaces.ParameterSpec
import org.pavlos.restapispec.interfaces.RequestSpec
import org.pavlos.restapispec.interfaces.ResponseSpec
import org.pavlos.restapispec.interfaces.StatusSpec
import spock.lang.Specification

class MethodTest extends Specification {

    def "method builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new ParamSpecBuilder()
        def newRequestBuilder = new RequestJSONSpecBuilder()
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
        newMethod.getRequest().getHeaders()[0].getMandatory()
        newMethod.getRequest().getQueryParams()[0].getName() == "queryName"
        newMethod.getRequest().getQueryParams()[0].getType() == "String"
        newMethod.getRequest().getQueryParams()[0].getMandatory()

        newMethod.getResponse().getHeaders()[0].getName() == "headerResponseName"
        newMethod.getResponse().getHeaders()[0].getMandatory()

    }

    def "method builder raises exception when missing type"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new ParamSpecBuilder()
        def newRequestBuilder = new RequestJSONSpecBuilder()
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
        def newRequestBuilder = new RequestJSONSpecBuilder()
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
