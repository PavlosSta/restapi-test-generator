package implementations;

import interfaces.QueryParamSpec;

public class QueryParamSpecImpl implements QueryParamSpec {

    private final String name;
    private final String type;
    private final String body;
    private final String defaultBody;
    private final Boolean mandatory;

    QueryParamSpecImpl(String name, String type, String body, String defaultBody, Boolean mandatory) {
        this.name = name;
        this.type = type;
        this.body = body;
        this.defaultBody = defaultBody;
        this.mandatory = mandatory;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public String getDefaultBody() {
        return defaultBody;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

}