package implementations;

import interfaces.QueryParam;

public class QueryParamImpl implements QueryParam {

    String name;
    String type;
    Object body;
    Object defaultBody;
    Boolean mandatory;

    public QueryParamImpl(String name, String type, Object body, Object defaultBody, Boolean mandatory) {
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