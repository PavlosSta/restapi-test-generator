package interfaces;

public interface QueryParam {

    String getName();
    String getType();
    Object getBody();
    Object getDefaultBody();
    Boolean getMandatory();

}

