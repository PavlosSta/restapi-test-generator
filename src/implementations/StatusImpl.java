package implementations;

import java.util.function.Predicate;

import interfaces.Status;

public class StatusImpl implements Status {

    String label;
    String body;
    Predicate<String> conditionBody;

    public StatusImpl(String label, String body, Predicate<String> conditionBody) {
        this.label = label;
        this.body = body;
        this.conditionBody = conditionBody;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Predicate<String> getConditionBody() {
        return conditionBody;
    }

    public void setConditionBody(Predicate<String> conditionBody) {
        this.conditionBody = conditionBody;
    }

}