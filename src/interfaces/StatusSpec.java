package interfaces;

import java.util.function.Predicate;

public interface StatusSpec {

    String getLabel();
    String getBody();
    Predicate<String> getConditionBody();

}

