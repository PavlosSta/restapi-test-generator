package implementations;

public class Builders {

    public static APISpecBuilder newAPI() {
        return new APISpecBuilder();
    }

    public static EndpointSpecBuilder newEndpoint() {
        return new EndpointSpecBuilder();
    }

    public static HeaderSpecBuilder newHeader() {
        return new HeaderSpecBuilder();
    }

    public static MethodSpecBuilder newMethod() {
        return new MethodSpecBuilder();
    }

    public static QueryParamSpecBuilder newQueryParam() {
        return new QueryParamSpecBuilder();
    }

    public static RequestSpecBuilder newRequest() {
        return new RequestSpecBuilder();
    }

    public static ResponseSpecBuilder newResponse() {
        return new ResponseSpecBuilder();
    }

    public static StatusBuilder newStatus() {
        return new StatusBuilder();
    }

}
