package interfaces;

import java.util.function.Predicate;

public interface Status {

    String getLabel();

    void setLabel(String label);

    String getBody();

    void setBody(String body);

    Predicate<String> getConditionBody();

    void setConditionBody(Predicate<String> conditionBody);

}

