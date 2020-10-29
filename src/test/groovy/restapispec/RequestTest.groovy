package restapispec

import implementations.HeaderSpecBuilder
import implementations.ParamSpecBuilder
import implementations.RequestGenericSpecBuilder
import interfaces.HeaderSpec
import interfaces.ParameterSpec
import interfaces.RequestSpec
import spock.lang.Specification

class RequestTest extends Specification {

    def "request builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newQueryParamBuilder = new ParamSpecBuilder()
        def newRequestBuilder = new RequestGenericSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setMandatory(true).build()
        ParameterSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("String").setMandatory(true).build()
        RequestSpec newRequest = newRequestBuilder.addHeader(newHeader).addQueryParam(newQueryParam).build()

        then:
        newRequest.getHeaders()[0].getName() == "headerName"
        newRequest.getQueryParams()[0].getName() == "queryName"
        newRequest.getQueryParams()[0].getType() == "String"
        newRequest.getQueryParams()[0].isMandatory()

    }

    def "request builder raises exception when missing headers and bodyParams and queryParams"() {

        def newRequestBuilder = new RequestGenericSpecBuilder()

        when:
        newRequestBuilder.build()

        then:
        thrown RuntimeException

    }
}
