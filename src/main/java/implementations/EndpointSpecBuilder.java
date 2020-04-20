package implementations;

import java.util.Set;

import interfaces.EndpointSpec;
import interfaces.MethodSpec;

public class EndpointSpecBuilder {

    private String path;            // eg. "prices"
    private String label;           // eg. "endpoint for prices"

    private Set<String> docs;       // notes for using this endpoint
    private Set<MethodSpec> methods;    // endpoint 1-N methods

    public EndpointSpecBuilder setPath(String path) {

        // validate path
        this.path = path;
        return this;
    }

    public EndpointSpecBuilder setLabel(String label) {

        // validate label
        this.label = label;
        return this;
    }

    public EndpointSpecBuilder setDocs(Set<String> docs) {

        // validate docs
        this.docs = docs;
        return this;
    }

    public EndpointSpecBuilder addDocs(Set<String> docs) {

        // validate docs
        this.docs.addAll(docs);
        return this;
    }

    public EndpointSpecBuilder addDocs(String doc) {

        // validate doc
        this.docs.add(doc);
        return this;
    }

    public EndpointSpecBuilder setMethods(Set<MethodSpec> methods) {

        // validate methods
        this.methods = methods;
        return this;
    }

    public EndpointSpecBuilder addMethods(Set<MethodSpec> methods) {

        // validate methods
        this.methods.addAll(methods);
        return this;
    }

    public EndpointSpecBuilder addMethods(MethodSpec method) {

        // validate methods
        this.methods.add(method);
        return this;
    }

    public EndpointSpec build() {
        return new EndpointSpecImpl(path, label, docs, methods);
    }


}