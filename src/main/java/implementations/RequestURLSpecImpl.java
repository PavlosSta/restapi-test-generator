package implementations;

        import java.util.Set;

        import interfaces.HeaderSpec;
        import interfaces.ParameterSpec;
        import interfaces.RequestSpec;

public class RequestURLSpecImpl implements RequestSpec {

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

    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }

}