package implementations;

import java.util.Set;

import interfaces.Endpoint;
import interfaces.Method;

public class EndpointImpl implements Endpoint {

    String path;            // eg. "prices"
    String label;           // eg. "endpoint for prices"

    Set<String> docs;       // notes for using this endpoint
    Set<Method> methods;    // endpoint 1-N methods


	public String getPath() {
		return this.path;
    }
    
	public void setPath(String path) {
		this.path = path;
    }
    
	public String getLabel() {
		return this.label;
    }
    
	public void setLabel(String label) {
        this.label = label;		
    }
    

	public Set<String> getDocs() {
        return this.docs;
    }
    
	public void setDocs(Set<String> docs) {
        this.docs = docs;
    }
    
   
	public Set<Method> getMethods() {
		return this.methods;
	}

    public void setMethods(Set<Method> methods) {
        this.methods = methods;
	}

}