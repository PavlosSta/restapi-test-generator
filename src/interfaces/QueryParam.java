package interfaces;

public interface QueryParam {

    String getName();

    void setName(String name);

    String getType();

    void setType(String type);
    
    Object getBody();

    void setBody(Object body);

    Object getDefaultBody();

    void setDefaultBody(Object defaultBody);

    Boolean getMandatory();

    void setMandatory(Boolean mandatory);
    
}

