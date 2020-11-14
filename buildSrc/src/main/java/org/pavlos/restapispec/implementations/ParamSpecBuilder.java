package org.pavlos.restapispec.implementations;

import org.pavlos.restapispec.interfaces.ParameterSpec;

public class ParamSpecBuilder {

    private String name;
    private String defaultValue;
    private String type;
    private boolean mandatory;

    public ParamSpecBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ParamSpecBuilder setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ParamSpecBuilder setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public ParamSpecBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public ParameterSpec build() {


        if (name == null || (!mandatory && defaultValue == null)) {
            throw new RuntimeException("Query Parameter bad input");
        }
        else if (!(type.equals("String") || type.equals("Integer") || type.equals("float") || type.equals("boolean"))) {
            throw new RuntimeException("Query Parameter bad type: " + type + ". Supported Types: String, Integer, float, boolean");
        }

        return new ParameterSpecImpl(name, defaultValue, type, mandatory);


    }
}
