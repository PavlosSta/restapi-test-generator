package implementations;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import freemarker.core.ReturnInstruction;
import interfaces.EndpointSpec;
import interfaces.MethodSpec;

public class EndpointSpecBuilder {

    private String path;            // eg. "prices"
    private String label;           // eg. "endpoint for prices"
    private String description;       // notes for using this endpoint
    private String attribute;
    private Set<MethodSpec> methods = new LinkedHashSet<>();     // endpoint 1-N methods

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
        this.attribute = attribute;
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

        if (this.path.isEmpty() || this.methods.isEmpty() || !validateMethodsWithAttribute(this.attribute, this.methods)) {
            throw new RuntimeException();
        }

        return new EndpointSpecImpl(path, label, description, attribute, Collections.unmodifiableSet(methods));

    }

    private boolean validateMethodsWithAttribute(String attribute, Set<MethodSpec> methods) {

        if (attribute == null) {
            for (MethodSpec method: methods) {
                if (method.getType().name() == "PUT" || method.getType().name() == "PATCH" || method.getType().name() == "DELETE") {
                    return false;
                }

            }
        }
        else {
            for (MethodSpec method: methods) {
                if (method.getType().name() == "POST") {
                    return false;
                }

            }
        }

        return true;
    }


}