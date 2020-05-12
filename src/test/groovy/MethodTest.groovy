import implementations.HeaderSpecBuilder
import implementations.MethodSpecBuilder
import implementations.QueryParamSpecBuilder
import implementations.RequestSpecBuilder
import implementations.ResponseSpecBuilder
import implementations.StatusSpecBuilder
import interfaces.HeaderSpec
import interfaces.MethodSpec
import interfaces.QueryParamSpec
import interfaces.RequestSpec
import interfaces.ResponseSpec
import interfaces.StatusSpec
import spock.lang.Specification

class MethodTest extends Specification {

    def "method builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new QueryParamSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setBody("headerRequestBody").setMandatory(false).build()
        QueryParamSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("queryType").setBody("queryBody").setMandatory(false).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).setJwt("jwt").build()

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setBody("headerResponseBody").setMandatory(false).build()
        StatusSpec newStatus = newStatusBuilder.setLabel("statusLabel").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).build()

        MethodSpec newMethod = newMethodBuilder.setType("methodType").setRequest(newRequest).setResponse(newResponse).build()

        then:

        newMethod.getType() == "methodType"

        newMethod.getRequest().getJwt() == "jwt"
        newMethod.getRequest().getHeaders()[0].getName() == "headerRequestName"
        newMethod.getRequest().getHeaders()[0].getBody() == "headerRequestBody"
        !newMethod.getRequest().getHeaders()[0].getMandatory()
        newMethod.getRequest().getQueryParams()[0].getName() == "queryName"
        newMethod.getRequest().getQueryParams()[0].getType() == "queryType"
        newMethod.getRequest().getQueryParams()[0].getBody() == "queryBody"
        !newMethod.getRequest().getQueryParams()[0].getMandatory()

        newMethod.getResponse().getHeaders()[0].getName() == "headerResponseName"
        newMethod.getResponse().getHeaders()[0].getBody() == "headerResponseBody"
        !newMethod.getResponse().getHeaders()[0].getMandatory()
        newMethod.getResponse().getStatuses()[0].getLabel() == "statusLabel"
        newMethod.getResponse().getStatuses()[0].getBody() == "statusBody"

    }

    def "method builder raises exception when missing type"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newQueryParamBuilder = new QueryParamSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setBody("headerRequestBody").setMandatory(false).build()
        QueryParamSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("queryType").setBody("queryBody").setMandatory(false).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).setJwt("jwt").build()

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setBody("headerResponseBody").setMandatory(false).build()
        StatusSpec newStatus = newStatusBuilder.setLabel("statusLabel").setBody("statusBody").build()
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

        HeaderSpec newHeaderResponse = newHeaderBuilder.setName("headerResponseName").setBody("headerResponseBody").setMandatory(false).build()
        StatusSpec newStatus = newStatusBuilder.setLabel("statusLabel").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeaderResponse).addStatus(newStatus).build()

        newMethodBuilder.setType("methodType").setResponse(newResponse).build()

        then:
        thrown RuntimeException

    }

    def "method builder raises exception when missing response"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newQueryParamBuilder = new QueryParamSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder()
        def newMethodBuilder = new MethodSpecBuilder()

        when:

        HeaderSpec newHeaderRequest = newHeaderBuilder.setName("headerRequestName").setBody("headerRequestBody").setMandatory(false).build()
        QueryParamSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("queryType").setBody("queryBody").setMandatory(false).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeaderRequest).addQueryParam(newQueryParam).setJwt("jwt").build()

        newMethodBuilder.setType("methodType").setRequest(newRequest).build()

        then:
        thrown RuntimeException

    }

}
