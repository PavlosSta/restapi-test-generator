package implementations;

import java.util.function.Predicate;

import interfaces.Status;

public class StatusImpl implements Status {

    String label;
    String body;
    Predicate<String> conditionBody;

    StatusImpl(String label, String body, Predicate<String> conditionBody) {
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