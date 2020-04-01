package implementations;

import interfaces.QueryParam;

public class QueryParamImpl implements QueryParam {

    String name;
    String type;
    Object body;
    Object defaultBody;
    Boolean mandatory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Object getDefaultBody() {
        return defaultBody;
    }

    public void setDefaultBody(Object defaultBody) {
        this.defaultBody = defaultBody;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

}