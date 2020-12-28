package restapispec

import org.pavlos.restapispec.implementations.StatusSpecBuilder
import org.pavlos.restapispec.interfaces.StatusSpec
import spock.lang.Specification

class StatusTest extends Specification{

    def "status builder works for body"() {
        def newStatusBuilder = new StatusSpecBuilder()

        when:
        StatusSpec newStatus = newStatusBuilder.setCode("statusCode").setBody("statusBody").build()

        then:
        newStatus.getBody() == "statusBody"
        newStatus.getCode() == "statusCode"

    }


    def "status builder raises exception when missing body"() {
        def newStatusBuilder = new StatusSpecBuilder()

        when:
        newStatusBuilder.setCode("statusCode").build()

        then:
        thrown RuntimeException

    }


    def "status builder raises exception when missing code"() {
        def newStatusBuilder = new StatusSpecBuilder()

        when:
        newStatusBuilder.setBody("statusBody").build()

        then:
        thrown RuntimeException
    }

    def "status builder raises exception when invalid code"() {
        def newStatusBuilder = new StatusSpecBuilder()

        when:
        newStatusBuilder.setBody("statusBody").setCode("600").build()

        then:
        thrown RuntimeException
    }
}
