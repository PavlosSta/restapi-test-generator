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
        this.path = path;
        return this;
    }

    public EndpointSpecBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public EndpointSpecBuilder addDocs(Set<String> docs) {
        this.docs.addAll(docs);
        return this;
    }

    public EndpointSpecBuilder addDoc(String doc) {
        this.docs.add(doc);
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

        //TODO validate

        return new EndpointSpecImpl(path, label, docs, methods);
    }


}