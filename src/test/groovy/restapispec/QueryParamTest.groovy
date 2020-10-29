package restapispec

import implementations.ParamSpecBuilder
import interfaces.ParameterSpec
import spock.lang.Specification

class QueryParamTest extends Specification {

    def "queryParam builder works for mandatory"() {
        def newQueryParamBuilder = new ParamSpecBuilder()

        when:
        ParameterSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("String").setMandatory(true).build()

        then:
        newQueryParam.getName() == "queryName"
        newQueryParam.getType() == "String"
        newQueryParam.isMandatory()

    }

    def "queryParam builder works for not mandatory"() {
        def newQueryParamBuilder = new ParamSpecBuilder()

        when:
        ParameterSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("String").setDefaultValue("defaultQueryBody").setMandatory(false).build()

        then:
        newQueryParam.getName() == "queryName"
        newQueryParam.getType() == "String"
        newQueryParam.defaultBodyIfOptionalAndMissing() == "defaultQueryBody"

    }

    def "queryParam builder raises exception for missing name"() {
        def newQueryParamBuilder = new ParamSpecBuilder()

        when:
        newQueryParamBuilder.setType("String").setMandatory(false).build()

        then:
        thrown RuntimeException
    }

    def "queryParam builder raises exception for missing value"() {
        def newQueryParamBuilder = new ParamSpecBuilder()

        when:
        newQueryParamBuilder.setName("queryName").setType("String").setMandatory(false).build()

        then:
        thrown RuntimeException
    }

    def "queryParam builder raises exception when optional and missing default body"() {
        def newQueryParamBuilder = new ParamSpecBuilder()

        when:
        newQueryParamBuilder.setName("queryName").setType("String").setMandatory(false).build()

        then:
        thrown RuntimeException
    }

    def "queryParam builder raises exception for missing type"() {
        def newQueryParamBuilder = new ParamSpecBuilder()

        when:
        newQueryParamBuilder.setName("queryName").setMandatory(false).build()

        then:
        thrown RuntimeException
    }
}
