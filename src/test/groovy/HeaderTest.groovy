import implementations.HeaderSpecBuilder
import interfaces.HeaderSpec
import spock.lang.Specification

class HeaderTest extends Specification{

    def "header builder works for mandatory"() {
        def newHeaderBuilder = new HeaderSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setBody("headerBody").setDefaultBody("defaultHeaderBody").setMandatory(true).build()

        then:
        newHeader.getName() == "headerName"
        newHeader.getBody() == "headerBody"
        newHeader.getDefaultBody() == "defaultHeaderBody"
        newHeader.getMandatory()

    }

    def "header builder works for not mandatory"() {
        def newHeaderBuilder = new HeaderSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setBody("headerBody").setMandatory(false).build()

        then:
        newHeader.getName() == "headerName"
        newHeader.getBody() == "headerBody"

    }

    def "header builder raises exception for missing name"() {
        def newHeaderBuilder = new HeaderSpecBuilder()

        when:
        newHeaderBuilder.setBody("headerBody").setMandatory(false).build()

        then:
        thrown RuntimeException
    }

    def "header builder raises exception for missing body"() {
        def newHeaderBuilder = new HeaderSpecBuilder()

        when:
        newHeaderBuilder.setName("headerName").setMandatory(false).build()

        then:
        thrown RuntimeException
    }

    def "header builder raises exception when mandatory and missing default body"() {
        def newHeaderBuilder = new HeaderSpecBuilder()

        when:
        newHeaderBuilder.setName("headerName").setBody("headerBody").setMandatory(true).build()

        then:
        thrown RuntimeException
    }
}
