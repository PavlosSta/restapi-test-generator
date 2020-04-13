package implementations;

import interfaces.HeaderSpec;

public class HeaderSpecBuilder {

    private String name;
    private String body;
    private String defaultBody;
    private Boolean mandatory;

    public HeaderSpecBuilder name(String name) {
        this.name = name;
        return this;
    }

    public HeaderSpecBuilder body(String body) {
        this.body = body;
        return this;
    }

    public HeaderSpecBuilder defaultBody(String defaultBody) {
        this.defaultBody = defaultBody;
        return this;
    }

    public HeaderSpecBuilder mandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public HeaderSpec build() {
        return new HeaderSpecImpl(name, body, defaultBody, mandatory);
    }

}