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
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setBody("headerBody").setMandatory(false).build()
        StatusSpec newStatus = newStatusBuilder.setLabel("statusLabel").setBody("statusBody").build()
        ResponseSpec newResponse = newResponseBuilder.addHeader(newHeader).addStatus(newStatus).build()

        then:
        newResponse.getHeaders()[0].getName() == "headerName"
        newResponse.getHeaders()[0].getBody() == "headerBody"
        newResponse.getStatuses()[0].getBody() == "statusBody"
        newResponse.getStatuses()[0].getLabel() == "statusLabel"

    }

    def "response builder raises exception when missing header"() {

        def newStatusBuilder = new StatusSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()

        when:
        StatusSpec newStatus = newStatusBuilder.setLabel("statusLabel").setBody("statusBody").build()
        newResponseBuilder.addStatus(newStatus).build()

        then:
        thrown RuntimeException

    }

    def "response builder raises exception when missing status"() {

        def newHeaderBuilder = new HeaderSpecBuilder()
        def newResponseBuilder = new ResponseSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setBody("headerBody").setMandatory(false).build()
        newResponseBuilder.addHeader(newHeader).build()

        then:
        thrown RuntimeException

    }

}
