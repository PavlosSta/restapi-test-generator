package implementations;

import interfaces.StatusSpec;

import java.util.function.Predicate;

public class StatusSpecBuilder {

    private String label;
    private String body;
    private Predicate<String> conditionBody;

    public StatusSpecBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public StatusSpecBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public StatusSpecBuilder setConditionBody(Predicate<String> conditionBody) {
        this.conditionBody = conditionBody;
        return this;
    }

    public StatusSpec build() {

        //TODO validate

        return new StatusSpecImpl(label, body, conditionBody);
    }
}
