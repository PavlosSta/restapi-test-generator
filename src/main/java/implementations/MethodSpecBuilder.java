package implementations;

import interfaces.MethodSpec;
import interfaces.RequestSpec;
import interfaces.ResponseSpec;

import java.util.Objects;

public class MethodSpecBuilder {

    private MethodSpec.MethodType type;        // GET, POST, PUT, PATCH, DELETE
    private RequestSpec request;    // request spec
    private ResponseSpec response;   // response spec

    public MethodSpecBuilder setType(String typeString) {
        this.type = MethodSpec.MethodType.valueOf(typeString);
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

        if (type == null || request == null || response == null) {
            throw new RuntimeException("Method bad input");
        }

        return new MethodSpecImpl(type, request, response);

    }

}
