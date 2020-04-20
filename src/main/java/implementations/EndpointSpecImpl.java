package implementations;

import java.util.Set;

import interfaces.EndpointSpec;
import interfaces.MethodSpec;

public class EndpointSpecImpl implements EndpointSpec {

    private final String path;            // eg. "prices"
    private final String label;           // eg. "endpoint for prices"

    private final Set<String> docs;       // notes for using this endpoint
    private final Set<MethodSpec> methods;    // endpoint 1-N methods

    EndpointSpecImpl(String path, String label, Set<String> docs, Set<MethodSpec> methods) {
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

	public Set<MethodSpec> getMethods() {
		return this.methods;
	}


}