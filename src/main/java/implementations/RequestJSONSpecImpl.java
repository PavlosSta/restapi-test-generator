package implementations;

import java.util.Set;

import interfaces.HeaderSpec;
import interfaces.ParameterSpec;
import interfaces.RequestJSONSpec;

public class RequestJSONSpecImpl implements RequestJSONSpec {

    private final Set<HeaderSpec> headers;
    private final Set<ParameterSpec> queryParams;
    private final String bodySchema;

    RequestJSONSpecImpl(Set<HeaderSpec> headers, Set<ParameterSpec> queryParams, String bodySchema) {
        this.headers = headers;
        this.queryParams = queryParams;
        this.bodySchema = bodySchema;
    }

    public Set<HeaderSpec> getHeaders() {
        return this.headers;
    }

    public Set<ParameterSpec> getQueryParams() {
        return this.queryParams;
    }

    public String getRequestBodySchema() {
        return this.bodySchema;
    }

}