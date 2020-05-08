package implementations;

import interfaces.HeaderSpec;

public class HeaderSpecBuilder {

    private String name;
    private String body;
    private String defaultBody;
    private Boolean mandatory;

    public HeaderSpecBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public HeaderSpecBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public HeaderSpecBuilder setDefaultBody(String defaultBody) {
        this.defaultBody = defaultBody;
        return this;
    }

    public HeaderSpecBuilder setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public HeaderSpec build() {

        if (name.isEmpty() || body.isEmpty() || (mandatory && defaultBody.isEmpty())) {
            throw new RuntimeException();
        }
        else {
            return new HeaderSpecImpl(name, body, defaultBody, mandatory);
        }

    }

}