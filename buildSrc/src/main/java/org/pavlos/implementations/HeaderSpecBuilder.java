package org.pavlos.implementations;

import org.pavlos.interfaces.HeaderSpec;

public class HeaderSpecBuilder {

    private String name;
    private String defaultValue;
    private boolean mandatory;

    public HeaderSpecBuilder setName(String name) {
        this.name = name;
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

        if (name == null  || (!mandatory && defaultValue == null)) {
            throw new RuntimeException("Header bad input");
        }

        return new HeaderSpecImpl(name, defaultValue, mandatory);

    }

}