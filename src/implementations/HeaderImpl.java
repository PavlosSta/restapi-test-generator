package implementations;

import interfaces.Header;

public class HeaderImpl implements Header {

    String name;
    String body;
    String defaultBody;
    Boolean mandatory;

    HeaderImpl(String name, String body, String defaultBody, Boolean mandatory) {
        this.name = name;
        this.body = body;
        this.defaultBody = defaultBody;
        this.mandatory = mandatory;
    }

    public String getName() {
        return this.name;
    }

    public String getBody() {
        return body;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public String getDefaultBody() {
        return defaultBody;
    }
    
}