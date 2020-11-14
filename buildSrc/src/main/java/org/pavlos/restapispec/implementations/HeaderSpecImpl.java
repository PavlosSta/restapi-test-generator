package org.pavlos.restapispec.implementations;

import org.pavlos.restapispec.interfaces.HeaderSpec;

public class HeaderSpecImpl implements HeaderSpec {

    private final String name;
    private final String defaultValue;
    private final boolean mandatory;

    HeaderSpecImpl(String name, String defaultValue, boolean mandatory) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.mandatory = mandatory;
    }

    public String getName() {
        return this.name;
    }

    public boolean isMandatory() {
        return this.mandatory;
    }

    public String getDefaultValueIfOptionalAndMissing() {
        return this.defaultValue;
    }
    
}