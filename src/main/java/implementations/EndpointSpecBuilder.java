package implementations;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import interfaces.EndpointSpec;
import interfaces.MethodSpec;

public class EndpointSpecBuilder {

    private String path;
    private String label;
    private String description;
    private Set<String> attributes = new LinkedHashSet<>();
    private Set<MethodSpec> methods = new LinkedHashSet<>();

    public EndpointSpecBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public EndpointSpecBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public EndpointSpecBuilder addDescription(String description) {
        this.description = description;
        return this;
    }

    public EndpointSpecBuilder addAttribute(String attribute) {
        this.attributes.add(attribute);
        return this;
    }

    public EndpointSpecBuilder addMethods(Set<MethodSpec> methods) {
        this.methods.addAll(methods);
        return this;
    }

    public EndpointSpecBuilder addMethod(MethodSpec method) {
        this.methods.add(method);
        return this;
    }

    public EndpointSpec build() {

        if (path == null || path.isEmpty() || methods.isEmpty()) {
            throw new RuntimeException("Endpoint bad input");
        }

        return new EndpointSpecImpl(path, label, description, attributes, Collections.unmodifiableSet(methods));

    }

}