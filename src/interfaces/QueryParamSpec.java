package interfaces;

public interface QueryParamSpec {

    String getName();
    String getType();
    Object getBody();
    Object getDefaultBody();
    Boolean getMandatory();

}

