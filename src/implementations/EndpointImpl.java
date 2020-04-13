package implementations;

import java.util.Set;

import interfaces.Endpoint;
import interfaces.Method;

public class EndpointImpl implements Endpoint {

    String path;            // eg. "prices"
    String label;           // eg. "endpoint for prices"

    Set<String> docs;       // notes for using this endpoint
    Set<Method> methods;    // endpoint 1-N methods

    EndpointImpl(String path, String label, Set<String> docs, Set<Method> methods) {
        this.path = path;
        this.label = label;
        this.docs = docs;
        this.methods = methods;
    }

	public String getPath() {
		return this.path;
    }

	public String getLabel() {
		return this.label;
    }

	public Set<String> getDocs() {
        return this.docs;
    }

	public Set<Method> getMethods() {
		return this.methods;
	}


}