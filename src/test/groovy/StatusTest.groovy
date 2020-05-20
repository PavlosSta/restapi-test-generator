import implementations.StatusSpecBuilder
import interfaces.StatusSpec
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

    def "status builder works for conditionBody"() {
        def newStatusBuilder = new StatusSpecBuilder()
        def conditionBody = new Hashtable()
        conditionBody.put("request.headers.foo == null", 200)

        when:
        StatusSpec newStatus = newStatusBuilder.setCode("statusCode").setConditionBody(conditionBody).build()

        then:
        newStatus.getConditionBody() == conditionBody
        newStatus.getCode() == "statusCode"

    }

    def "status builder raises exception when missing both body and conditionBody"() {
        def newStatusBuilder = new StatusSpecBuilder()

        when:
        newStatusBuilder.setCode("statusCode").build()

        then:
        thrown RuntimeException

    }

    def "status builder raises exception when having both body and conditionBody"() {
        def newStatusBuilder = new StatusSpecBuilder()
        def conditionBody = new Hashtable()
        conditionBody.put("request.headers.foo == null", 200)

        when:
        newStatusBuilder.setCode("statusCode").setBody("statusBody").setConditionBody(conditionBody).build()

        then:
        thrown RuntimeException

    }

    def "status builder raises exception when missing label"() {
        def newStatusBuilder = new StatusSpecBuilder()

        when:
        newStatusBuilder.setBody("statusBody").build()

        then:
        thrown RuntimeException
    }
}
