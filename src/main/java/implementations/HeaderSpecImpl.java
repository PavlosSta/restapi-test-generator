package implementations;

import interfaces.HeaderSpec;

public class HeaderSpecImpl implements HeaderSpec {

    private final String name;
    private final String value;
    private final String defaultValue;
    private final boolean mandatory;

    HeaderSpecImpl(String name, String value, String defaultValue, boolean mandatory) {
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
        this.mandatory = mandatory;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isMandatory() {
        return this.mandatory;
    }

    public String defaultValueIfOptionalAndMissing() {
        return this.defaultValue;
    }
    
}