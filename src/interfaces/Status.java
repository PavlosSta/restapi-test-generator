package interfaces;

import java.util.function.Predicate;

public interface Status {

    String getLabel();
    String getBody();
    Predicate<String> getConditionBody();

}

