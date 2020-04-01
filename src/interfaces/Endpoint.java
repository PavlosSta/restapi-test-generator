package interfaces;

import java.util.Set;

public interface Endpoint {

    String getPath();
    
    void setPath(String path);
    
    String getLabel();   

    void setLabel(String label);

    Set<String> getDocs();    

    void setDocs(Set<String> docs);

    Set<Method> getMethods(); 

    void setMethods(Set<Method> methods);

}
