package implementations;

import interfaces.QueryParamSpec;

public class QueryParamSpecImpl implements QueryParamSpec {

    private final String name;
    private final String type;
    private final String value;
    private final String defaultValue;
    private final boolean mandatory;

    QueryParamSpecImpl(String name, String type, String value, String defaultValue, boolean mandatory) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.defaultValue = defaultValue;
        this.mandatory = mandatory;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public boolean isMandatory() {
        return this.mandatory;
    }

}