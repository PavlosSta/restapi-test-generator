package restapispec

import org.pavlos.restapispec.implementations.HeaderSpecBuilder
import org.pavlos.restapispec.implementations.ResponseSpecBuilder
import org.pavlos.restapispec.interfaces.HeaderSpec
import org.pavlos.restapispec.interfaces.ResponseSpec

import spock.lang.Specification

class ResponseTest extends Specification {

    def "response builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setMandatory(true).build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeader).addResponseBodySchema("JSON").setCode(200).build()

        then:
        newResponse.getHeaders()[0].getName() == "headerName"

    }

    def "response builder raises exception when missing status"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setMandatory(true).build()
        newResponseBuilder.addHeader(newHeader).build()

        then:
        thrown RuntimeException

    }

}
