package implementations;

import interfaces.Header;

public class HeaderImpl implements Header {

    String name;
    String body;
    String defaultBody;
    Boolean mandatory;

    public HeaderImpl(String name, String body, String defaultBody, Boolean mandatory) {
        this.name = name;
        this.body = body;
        this.defaultBody = defaultBody;
        this.mandatory = mandatory;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getDefaultBody() {
        return defaultBody;
    }

    public void setDefaultBody(String defaultBody) {
        this.defaultBody = defaultBody;
    }
    
}