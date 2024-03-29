package org.pavlos.restapispec.implementations;

import org.pavlos.restapispec.interfaces.ParameterSpec;

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

    public String getDefaultBodyIfOptionalAndMissing() {
        return this.defaultValue;
    }

    public boolean getMandatory() {
        return this.mandatory;
    }

}