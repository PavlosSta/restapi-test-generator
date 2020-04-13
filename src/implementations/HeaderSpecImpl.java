package implementations;

import interfaces.HeaderSpec;

public class HeaderSpecImpl implements HeaderSpec {

    private final String name;
    private final String body;
    private final String defaultBody;
    private final Boolean mandatory;

    HeaderSpecImpl(String name, String body, String defaultBody, Boolean mandatory) {
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