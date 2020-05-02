/*
 * This Spock specification was generated by the Gradle 'init' task.
 */
import spock.lang.Specification

class MainTest extends Specification {
    def "test returns true"() {
        setup:
        def lib = new Main()

        when:
        def result = lib.test()

        then:
        result == true
    }

    def "one plus one should equal two"() {
        expect:
        1 + 1 == 2
    }

    def "two plus two should equal four"() {
        given:
        int left = 2
        int right = 2

        when:
        int result = left + right

        then:
        result == 4
    }

}
