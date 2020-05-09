import implementations.APISpecBuilder
import spock.lang.Specification

class APITest extends Specification {

    def "baseUrl validation works"() {
        def API = new APISpecBuilder()

        when:
        Boolean resultNotValid = API.validateUrl("localhosttest")
        Boolean resultValid = API.validateUrl("https://www.test.com/api")

        then:
        !resultNotValid
        resultValid
    }





}
