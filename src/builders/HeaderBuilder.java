package builders;

import implementations.HeaderImpl;
import interfaces.Header;

public class HeaderBuilder {

    String name;
    String body;
    String defaultBody;
    Boolean mandatory;

    public HeaderBuilder name(String name) {
        this.name = name;
        return this;
    }

    public HeaderBuilder body(String body) {
        this.body = body;
        return this;
    }

    public HeaderBuilder defaultBody(String defaultBody) {
        this.defaultBody = defaultBody;
        return this;
    }

    public HeaderBuilder mandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public Header build() {
        return new HeaderImpl(name, body, defaultBody, mandatory);
    }

}