package implementations;

import java.util.Hashtable;

import interfaces.StatusSpec;

public class StatusSpecImpl implements StatusSpec {

    private final String code;
    private final String body;
    private final Hashtable<String, Integer> conditionBody;

    StatusSpecImpl(String code, String body, Hashtable<String, Integer> conditionBody) {
        this.code = code;
        this.body = body;
        this.conditionBody = conditionBody;
    }

    public String getCode() {
        return this.code;
    }

    public String getBody() {
        return this.body;
    }

    public Hashtable<String, Integer> getConditionBody() {
        return this.conditionBody;
    }

}