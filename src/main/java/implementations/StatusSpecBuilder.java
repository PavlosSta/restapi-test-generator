package implementations;

import interfaces.StatusSpec;

import java.util.Hashtable;
import java.util.Objects;
import java.util.function.Predicate;

public class StatusSpecBuilder {

    private String label;
    private String body;
    private Hashtable<String, Integer> conditionBody;

    public StatusSpecBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public StatusSpecBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public StatusSpecBuilder setConditionBody(Hashtable conditionBody) {
        this.conditionBody = conditionBody;
        return this;
    }

    public StatusSpec build() {

        if (label == null || ((body == null) == (conditionBody == null))) {
            throw new RuntimeException();
        }
        else {
            return new StatusSpecImpl(label, body, conditionBody);
        }
    }
}
