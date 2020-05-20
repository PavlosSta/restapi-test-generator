package interfaces;

import java.util.Hashtable;

public interface StatusSpec {

    String getLabel();
    String getBody();
    Hashtable<String, Integer> getConditionBody();

}

