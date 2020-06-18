package implementations;

import interfaces.QueryParamSpec;

public class QueryParamSpecBuilder {

    private String name;
    private String type;
    private String value;
    private String defaultValue;
    private boolean mandatory;

    public QueryParamSpecBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public QueryParamSpecBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public QueryParamSpecBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public QueryParamSpecBuilder setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public QueryParamSpecBuilder setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public QueryParamSpec build() {

        if (name == null || type == null || (!mandatory && value == null) || (mandatory && defaultValue == null)) {
            throw new RuntimeException("Query Parameter bad input");
        }

        return new QueryParamSpecImpl(name, type, value, defaultValue, mandatory);


    }
}
