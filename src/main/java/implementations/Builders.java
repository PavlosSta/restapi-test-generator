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

    public static ParamSpecBuilder newQueryParam() {
        return new ParamSpecBuilder();
    }

    public static RequestJSONSpecBuilder newRequestJSON() {
        return new RequestJSONSpecBuilder();
    }

    public static RequestURLSpecBuilder newRequestURL() {
        return new RequestURLSpecBuilder();
    }

    public static ResponseSpecBuilder newResponse() {
        return new ResponseSpecBuilder();
    }

    public static StatusSpecBuilder newStatus() {
        return new StatusSpecBuilder();
    }

}
