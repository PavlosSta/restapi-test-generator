package restapispec

import org.pavlos.restapispec.implementations.HeaderSpecBuilder
import org.pavlos.restapispec.implementations.ResponseSpecBuilder
import org.pavlos.restapispec.implementations.StatusSpecBuilder
import org.pavlos.restapispec.interfaces.HeaderSpec
import org.pavlos.restapispec.interfaces.ResponseSpec
import org.pavlos.restapispec.interfaces.StatusSpec
import spock.lang.Specification

class ResponseTest extends Specification {

    def "response builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setMandatory(true).build()
        StatusSpec newStatus = newStatusBuilder.setCode("200").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeader).addStatus(newStatus).addResponseBodySchema("JSON").build()

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
