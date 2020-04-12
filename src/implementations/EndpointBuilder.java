package implementations;

import java.util.Set;

import interfaces.Endpoint;
import interfaces.Method;

public class EndpointBuilder {

    String path;            // eg. "prices"
    String label;           // eg. "endpoint for prices"

    Set<String> docs;       // notes for using this endpoint
    Set<Method> methods;    // endpoint 1-N methods

    public EndpointBuilder path(String path) {

        // validate path
        this.path = path;
        return this;
    }

    public EndpointBuilder label(String label) {

        // validate label
        this.label = label;
        return this;
    }

    public EndpointBuilder docs(Set<String> docs) {

        // validate docs
        this.docs = docs;
        return this;
    }

    public EndpointBuilder methods(Set<Method> methods) {

        // validate methods
        this.methods = methods;
        return this;
    }

    public Endpoint build() {
        return new EndpointImpl(path, label, docs, methods);
    }


}