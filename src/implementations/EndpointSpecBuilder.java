package implementations;

import java.util.Set;

import interfaces.EndpointSpec;
import interfaces.MethodSpec;

public class EndpointSpecBuilder {

    private String path;            // eg. "prices"
    private String label;           // eg. "endpoint for prices"

    private Set<String> docs;       // notes for using this endpoint
    private Set<MethodSpec> methods;    // endpoint 1-N methods

    public EndpointSpecBuilder path(String path) {

        // validate path
        this.path = path;
        return this;
    }

    public EndpointSpecBuilder label(String label) {

        // validate label
        this.label = label;
        return this;
    }

    public EndpointSpecBuilder docs(Set<String> docs) {

        // validate docs
        this.docs = docs;
        return this;
    }

    public EndpointSpecBuilder methods(Set<MethodSpec> methods) {

        // validate methods
        this.methods = methods;
        return this;
    }

    public EndpointSpec build() {
        return new EndpointSpecImpl(path, label, docs, methods);
    }


}