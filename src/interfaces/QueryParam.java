package interfaces;

public interface QueryParam {

    String getName();
    String getType();
    Object getBody();

    Boolean getMandatory();
    Object getDefaultBody();

}

