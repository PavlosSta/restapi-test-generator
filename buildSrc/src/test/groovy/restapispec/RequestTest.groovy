package restapispec

import org.pavlos.restapispec.implementations.HeaderSpecBuilder
import org.pavlos.restapispec.implementations.ParamSpecBuilder
import org.pavlos.restapispec.implementations.RequestJSONSpecBuilder
import org.pavlos.restapispec.interfaces.HeaderSpec
import org.pavlos.restapispec.interfaces.ParameterSpec
import org.pavlos.restapispec.interfaces.RequestSpec
import spock.lang.Specification

class RequestTest extends Specification {

    def "request builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newQueryParamBuilder = new ParamSpecBuilder()
        def newRequestBuilder = new RequestJSONSpecBuilder()

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

}
