import implementations.HeaderSpecBuilder
import implementations.ResponseSpecBuilder
import implementations.StatusSpecBuilder
import interfaces.HeaderSpec
import interfaces.ResponseSpec
import interfaces.StatusSpec
import spock.lang.Specification

class ResponseTest extends Specification {

    def "response builder works"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newStatusBuilder = new StatusSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setMandatory(true).build()
        StatusSpec newStatus = newStatusBuilder.setCode("statusCode").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeader).addStatus(newStatus).build()

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
