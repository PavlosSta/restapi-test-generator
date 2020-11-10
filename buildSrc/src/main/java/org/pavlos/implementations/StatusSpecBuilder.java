package org.pavlos.implementations;

import org.pavlos.interfaces.StatusSpec;

public class StatusSpecBuilder {

    private String code;
    private String body;

    public StatusSpecBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    public StatusSpecBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public StatusSpec build() {

        if (code == null || body == null) {
            throw new RuntimeException("Status bad input");
        }

        return new StatusSpecImpl(code, body);

    }
}
