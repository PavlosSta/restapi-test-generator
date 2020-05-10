import implementations.StatusSpecBuilder
import interfaces.StatusSpec
import spock.lang.Specification

class StatusTest extends Specification{

    def "status builder works for body"() {
        def newStatusBuilder = new StatusSpecBuilder()

        when:
        StatusSpec newStatus = newStatusBuilder.setLabel("statusLabel").setBody("statusBody").build()

        then:
        newStatus.getBody() == "statusBody"
        newStatus.getLabel() == "statusLabel"

    }

    def "status builder works for conditionBody"() {
        def newStatusBuilder = new StatusSpecBuilder()
        def conditionBody = new Hashtable()
        conditionBody.put("request.headers.foo == null", 200)

        when:
        StatusSpec newStatus = newStatusBuilder.setLabel("statusLabel").setConditionBody(conditionBody).build()

        then:
        newStatus.getConditionBody() == conditionBody
        newStatus.getLabel() == "statusLabel"

    }

    def "status builder doesn't build without body nor conditionBody"() {
        def newStatusBuilder = new StatusSpecBuilder()

        when:
        newStatusBuilder.setLabel("statusLabel").build()

        then:
        thrown RuntimeException

    }

    def "status builder doesn't build with body and conditionBody"() {
        def newStatusBuilder = new StatusSpecBuilder()
        def conditionBody = new Hashtable()
        conditionBody.put("request.headers.foo == null", 200)

        when:
        newStatusBuilder.setLabel("statusLabel").setBody("statusBody").setConditionBody(conditionBody).build()

        then:
        thrown RuntimeException

    }

    def "status builder doesn't build without label"() {
        def newStatusBuilder = new StatusSpecBuilder()

        when:
        newStatusBuilder.setBody("statusBody").build()

        then:
        thrown RuntimeException
    }
}
