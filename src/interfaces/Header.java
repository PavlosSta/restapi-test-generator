package interfaces;

public interface Header {

    String getName();

    void setName(String name);

    String getBody();

    void setBody(String body);

    Boolean getMandatory();

    void setMandatory(Boolean mandatory);

    String getDefaultBody();

    void setDefaultBody(String defaultBody);

}

