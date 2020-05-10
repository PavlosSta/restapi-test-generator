import implementations.QueryParamSpecBuilder
import interfaces.QueryParamSpec
import spock.lang.Specification

class QueryParamTest extends Specification {

    def "queryParam builder works for mandatory"() {
        def newQueryParamBuilder = new QueryParamSpecBuilder()

        when:
        QueryParamSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("queryType").setBody("queryBody").setDefaultBody("defaultQueryBody").setMandatory(true).build()

        then:
        newQueryParam.getName() == "queryName"
        newQueryParam.getType() == "queryType"
        newQueryParam.getBody() == "queryBody"
        newQueryParam.getDefaultBody() == "defaultQueryBody"
        newQueryParam.getMandatory()

    }

    def "queryParam builder works for not mandatory"() {
        def newQueryParamBuilder = new QueryParamSpecBuilder()

        when:
        QueryParamSpec newQueryParam = newQueryParamBuilder.setName("queryName").setType("queryType").setBody("queryBody").setMandatory(false).build()

        then:
        newQueryParam.getName() == "queryName"
        newQueryParam.getType() == "queryType"
        newQueryParam.getBody() == "queryBody"

    }

    def "queryParam builder raises exception for missing name"() {
        def newQueryParamBuilder = new QueryParamSpecBuilder()

        when:
        newQueryParamBuilder.setBody("queryBody").setType("queryType").setMandatory(false).build()

        then:
        thrown RuntimeException
    }

    def "queryParam builder raises exception for missing body"() {
        def newQueryParamBuilder = new QueryParamSpecBuilder()

        when:
        newQueryParamBuilder.setName("queryName").setType("queryType").setMandatory(false).build()

        then:
        thrown RuntimeException
    }

    def "queryParam builder raises exception when mandatory and missing default body"() {
        def newQueryParamBuilder = new QueryParamSpecBuilder()

        when:
        newQueryParamBuilder.setName("queryName").setType("queryType").setBody("queryBody").setMandatory(true).build()

        then:
        thrown RuntimeException
    }

    def "queryParam builder raises exception for missing type"() {
        def newQueryParamBuilder = new QueryParamSpecBuilder()

        when:
        newQueryParamBuilder.setName("queryName").setBody("queryBody").setMandatory(false).build()

        then:
        thrown RuntimeException
    }
}
