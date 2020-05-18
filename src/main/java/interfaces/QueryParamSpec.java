package interfaces;

public interface QueryParamSpec {

    String getName();
    String getType();
    Object getValue();
    Object getDefaultValue();
    boolean isMandatory();

}

