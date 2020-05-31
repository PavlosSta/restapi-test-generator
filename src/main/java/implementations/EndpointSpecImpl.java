package implementations;

import java.util.Set;

import interfaces.EndpointSpec;
import interfaces.MethodSpec;

public class EndpointSpecImpl implements EndpointSpec {

    private final String path;
    private final String label;
    private final String description;
    private final String attribute;
    private final Set<MethodSpec> methods;

    EndpointSpecImpl(String path, String label, String description, String attribute, Set<MethodSpec> methods) {
        this.path = path;
        this.label = label;
        this.description = description;
        this.attribute = attribute;
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

    public String getAttribute() {
        return this.attribute;
    }

    public Set<MethodSpec> getMethods() {
		return this.methods;
	}


}