package implementations;

import java.util.Set;

import interfaces.EndpointSpec;
import interfaces.MethodSpec;

public class EndpointSpecImpl implements EndpointSpec {

    private final String path;            // eg. "prices"
    private final String label;           // eg. "endpoint for prices"
    private final String description;       // notes for using this endpoint
    private final Set<MethodSpec> methods;    // endpoint 1-N methods

    EndpointSpecImpl(String path, String label, String description, Set<MethodSpec> methods) {
        this.path = path;
        this.label = label;
        this.description = description;
        this.methods = methods;
    }

	public String getPath() {
		return this.path;
    }

	public String getLabel() {
		return this.label;
    }

	public String getDescription() {
        return this.description;
    }

	public Set<MethodSpec> getMethods() {
		return this.methods;
	}


}