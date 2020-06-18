package implementations;

import interfaces.HeaderSpec;

public class HeaderSpecBuilder {

    private String name;
    private String value;
    private String defaultValue;
    private boolean mandatory;

    public HeaderSpecBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public HeaderSpecBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public HeaderSpecBuilder setDefaultValueIfOptionalAndMissing(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public HeaderSpecBuilder setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public HeaderSpec build() {

        if (name == null || value == null || (mandatory && (defaultValue == null))) {
            throw new RuntimeException("Header bad input");
        }

        return new HeaderSpecImpl(name, value, defaultValue, mandatory);

    }

}