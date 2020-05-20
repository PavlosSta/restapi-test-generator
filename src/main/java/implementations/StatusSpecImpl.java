package implementations;

import java.util.Hashtable;

import interfaces.StatusSpec;

public class StatusSpecImpl implements StatusSpec {

    private final String label;
    private final String body;
    private final Hashtable<String, Integer> conditionBody;

    StatusSpecImpl(String label, String body, Hashtable<String, Integer> conditionBody) {
        this.label = label;
        this.body = body;
        this.conditionBody = conditionBody;
    }

    public String getLabel() {
        return label;
    }

    public String getBody() {
        return body;
    }

    public Hashtable<String, Integer> getConditionBody() {
        return conditionBody;
    }

}