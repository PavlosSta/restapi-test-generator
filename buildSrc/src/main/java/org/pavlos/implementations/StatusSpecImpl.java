package org.pavlos.implementations;

import org.pavlos.interfaces.StatusSpec;

public class StatusSpecImpl implements StatusSpec {

    private final String code;
    private final String body;

    StatusSpecImpl(String code, String body) {
        this.code = code;
        this.body = body;
    }

    public String getCode() {
        return this.code;
    }

    public String getBody() {
        return this.body;
    }

}