package implementations;

import interfaces.StatusSpec;

import java.util.function.Predicate;

public class StatusBuilder {

    private String label;
    private String body;
    private Predicate<String> conditionBody;

    public StatusBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public StatusBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public StatusBuilder setConditionBody(Predicate<String> conditionBody) {
        this.conditionBody = conditionBody;
        return this;
    }

    public StatusSpec build() {
        return new StatusSpecImpl(label, body, conditionBody);
    }
}
