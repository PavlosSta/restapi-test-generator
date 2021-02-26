package org.pavlos.restapispec.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pavlos.restapispec.interfaces.EndpointSpec;
import org.pavlos.restapispec.interfaces.MethodSpec;

public class EndpointSpecBuilder {

    private String path;
    private String label;
    private String description;
    private final Set<String> attributes = new LinkedHashSet<>();
    private final Set<MethodSpec> methods = new LinkedHashSet<>();

    public EndpointSpecBuilder setPath(String path) {
        this.path = path;

        Pattern p = Pattern.compile("\\{[a-zA-Z]+}");
        Matcher m1 = p.matcher(path);

        while (m1.find()) {
            this.attributes.add(m1.group().replace("{","").replace("}",""));
        }
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

        System.out.println(attributes);
        return new EndpointSpecImpl(path, label, description, attributes, Collections.unmodifiableSet(methods));

    }

}