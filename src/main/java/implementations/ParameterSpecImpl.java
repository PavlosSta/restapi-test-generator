package implementations;

import interfaces.ParameterSpec;

public class ParameterSpecImpl implements ParameterSpec {

    private final String name;
    private final String defaultValue;
    private final String type;
    private final boolean mandatory;

    ParameterSpecImpl(String name, String defaultValue, String type, boolean mandatory) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.mandatory = mandatory;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String defaultBodyIfOptionalAndMissing() {
        return this.defaultValue;
    }

    public boolean isMandatory() {
        return this.mandatory;
    }

}