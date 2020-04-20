package implementations;

import java.util.function.Predicate;

import interfaces.StatusSpec;

public class StatusSpecImpl implements StatusSpec {

    private final String label;
    private final String body;
    private final Predicate<String> conditionBody;

    StatusSpecImpl(String label, String body, Predicate<String> conditionBody) {
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

    public Predicate<String> getConditionBody() {
        return conditionBody;
    }

}