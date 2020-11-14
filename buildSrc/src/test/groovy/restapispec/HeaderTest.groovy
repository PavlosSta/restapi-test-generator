package restapispec

import org.pavlos.restapispec.implementations.HeaderSpecBuilder
import org.pavlos.restapispec.interfaces.HeaderSpec
import spock.lang.Specification

class HeaderTest extends Specification{

    def "header builder works for mandatory"() {
        def newHeaderBuilder = new HeaderSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setDefaultValueIfOptionalAndMissing("defaultHeaderBody").setMandatory(true).build()

        then:
        newHeader.getName() == "headerName"
        newHeader.getDefaultValueIfOptionalAndMissing() == "defaultHeaderBody"
        newHeader.isMandatory()

    }

    def "header builder works for not mandatory"() {
        def newHeaderBuilder = new HeaderSpecBuilder()

        when:
        HeaderSpec newHeader = newHeaderBuilder.setName("headerName").setMandatory(false).setDefaultValueIfOptionalAndMissing("defValue").build()

        then:
        newHeader.getName() == "headerName"
        newHeader.getDefaultValueIfOptionalAndMissing() == "defValue"

    }

    def "header builder raises exception for missing name"() {
        def newHeaderBuilder = new HeaderSpecBuilder()

        when:
        newHeaderBuilder.setMandatory(false).build()

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

    def "header builder raises exception when optional and missing default body"() {
        def newHeaderBuilder = new HeaderSpecBuilder()

        when:
        newHeaderBuilder.setName("headerName").setMandatory(false).build()

        then:
        thrown RuntimeException
    }
}
