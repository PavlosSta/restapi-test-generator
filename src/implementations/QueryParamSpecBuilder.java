package implementations;

import interfaces.QueryParamSpec;

public class QueryParamSpecBuilder {

    private String name;
    private String type;
    private Object body;
    private Object defaultBody;
    private Boolean mandatory;

    public QueryParamSpecBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public QueryParamSpecBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public QueryParamSpecBuilder setBody(Object body) {
        this.body = body;
        return this;
    }

    public QueryParamSpecBuilder setDefaultBody(Object defaultBody) {
        this.defaultBody = defaultBody;
        return this;
    }

    public QueryParamSpecBuilder setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public QueryParamSpec build() {
        return new QueryParamSpecImpl(name, type, body, defaultBody, mandatory);
    }
}
