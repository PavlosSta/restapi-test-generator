import implementations.HeaderSpecBuilder
import implementations.QueryParamSpecBuilder
import implementations.RequestSpecBuilder
import interfaces.HeaderSpec
import interfaces.QueryParamSpec
import interfaces.RequestSpec
import spock.lang.Specification

class RequestTest extends Specification {

    def "request builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newQueryParamBuilder = new QueryParamSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setValue("headerBody").setMandatory(false).build()
        QueryParamSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("queryType").setValue("queryBody").setMandatory(false).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeader).addQueryParam(newQueryParam).build()

        then:
        newRequest.getHeaders()[0].getName() == "headerName"
        newRequest.getHeaders()[0].getValue() == "headerBody"
        newRequest.getQueryParams()[0].getName() == "queryName"
        newRequest.getQueryParams()[0].getType() == "queryType"
        newRequest.getQueryParams()[0].getValue() == "queryBody"
        !newRequest.getQueryParams()[0].isMandatory()

    }

    def "request builder raises exception when missing header"() {

        def newQueryParamBuilder = new QueryParamSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder()

        when:
        QueryParamSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("queryType").setValue("queryBody").setMandatory(false).build()
        newRequestBuilder.addQueryParam(newQueryParam).build()

        then:
        thrown RuntimeException

    }

    def "request builder raises exception when missing queryParam"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newRequestBuilder = new RequestSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setValue("headerBody").setMandatory(false).build()
        newRequestBuilder.addHeader(newHeader).build()

        then:
        thrown RuntimeException

    }
}
