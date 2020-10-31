package interfaces;

import java.util.Set;

/**
 * This is a Request specification.
 */
public interface RequestSpec {

    /**
     * @return a set with the Header Specifications of the Request
     */
    Set<HeaderSpec> getHeaders();

    /**
     * @return a set with the Query Parameter Specifications of the Request
     */
    Set<ParameterSpec>  getQueryParams();

    /**
     * @return the Content Type header of the Request
     */
    String getContentType();

}

/*
GroovyApiSpecBuilder builder = new GroovyApiSpecBuilder()
APISpec apiSpec = builder.build {
    api {
        baseUrl "www.api.com"
        label "My api"
        endpoint ("/foo") {
            path "/foo"
            label "..."
            method ("get") {

            }
        }

    }
}

class GroovyApiSpecBuilder {

    private APISpecBuilder builder;

    void api(Closure c) {
        builder = new APISpecBuilder();
        c.call(this)
    }

    void baseUrl(String url) {
        builder.setBaseUrl(url);
    }

    void endpoint(String path, Closure c) {

    }

    void methodMissing(String name) {

    }

    APISpec build(Closure c) {

    }

}
*/