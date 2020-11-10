package org.pavlos.implementations;

        import java.util.Set;

        import org.pavlos.interfaces.HeaderSpec;
        import org.pavlos.interfaces.ParameterSpec;
        import org.pavlos.interfaces.RequestURLSpec;

public class RequestURLSpecImpl implements RequestURLSpec {

    private final Set<HeaderSpec> headers;
    private final Set<ParameterSpec> queryParams;
    private final Set<ParameterSpec> bodyParams;

    RequestURLSpecImpl(Set<HeaderSpec> headers, Set<ParameterSpec> queryParams, Set<ParameterSpec> bodyParams) {
        this.headers = headers;
        this.queryParams = queryParams;
        this.bodyParams = bodyParams;
    }

    public Set<HeaderSpec> getHeaders() {
        return this.headers;
    }

    public Set<ParameterSpec> getQueryParams() {
        return this.queryParams;
    }

    public Set<ParameterSpec> getBodyParams() {
        return this.bodyParams;
    }


}