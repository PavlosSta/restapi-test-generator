package implementations;

import interfaces.MethodSpec;
import interfaces.RequestSpec;
import interfaces.ResponseSpec;

import java.util.Objects;

public class MethodSpecBuilder {

    private String type;        // GET, POST, PUT, PATCH, DELETE
    private RequestSpec request;    // request spec
    private ResponseSpec response;   // response spec

    public MethodSpecBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public MethodSpecBuilder setRequest(RequestSpec request) {
        this.request = request;
        return this;
    }

    public MethodSpecBuilder setResponse(ResponseSpec response) {
        this.response = response;
        return this;
    }

    public MethodSpec build() {

        if (type.isEmpty() || Objects.isNull(request) || Objects.isNull(response)) {
            throw new RuntimeException();
        }
        else {
            return new MethodSpecImpl(type, request, response);
        }
    }

}
