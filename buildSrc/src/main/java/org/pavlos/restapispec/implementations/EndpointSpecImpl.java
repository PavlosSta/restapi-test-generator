package org.pavlos.restapispec.implementations;

import java.util.Set;

import org.pavlos.restapispec.interfaces.EndpointSpec;
import org.pavlos.restapispec.interfaces.MethodSpec;

public class EndpointSpecImpl implements EndpointSpec {

    private final String path;
    private final String label;
    private final String description;
    private final Set<String> attributes;
    private final Set<MethodSpec> methods;

    EndpointSpecImpl(String path, String label, String description, Set<String> attributes, Set<MethodSpec> methods) {
        this.path = path;
        this.label = label;
        this.description = description;
        this.attributes = attributes;
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

    public Set<String> getAttributes() {
        return this.attributes;
    }

    public Set<MethodSpec> getMethods() {
		return this.methods;
	}


}