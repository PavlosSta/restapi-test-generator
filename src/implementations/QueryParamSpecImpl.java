package implementations;

import interfaces.QueryParamSpec;

public class QueryParamSpecImpl implements QueryParamSpec {

    private final String name;
    private final String type;
    private final Object body;
    private final Object defaultBody;
    private final Boolean mandatory;

    QueryParamSpecImpl(String name, String type, Object body, Object defaultBody, Boolean mandatory) {
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

    public Object getBody() {
        return body;
    }

    public Object getDefaultBody() {
        return defaultBody;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

}