package implementations;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import interfaces.APISpec;
import interfaces.EndpointSpec;

public class APISpecBuilder {

    private String baseUrl; // eg. "https://www.myapi.com/api/"
    private String label;   // eg. "API name"
    private Set<EndpointSpec> endpoints = new LinkedHashSet<>();

    public APISpecBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public APISpecBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public APISpecBuilder addEndpoints(Set<EndpointSpec> endpoints) {
        this.endpoints.addAll(endpoints);
        return this;
    }

    public APISpecBuilder addEndpoint(EndpointSpec endpoint) {
        this.endpoints.add(endpoint);
        return this;
    }

    public Boolean validateUrl(String baseUrl) {

        UrlValidator urlValidator = new UrlValidator();

        return urlValidator.isValid("https://myapi.com" + baseUrl);

    }

    public APISpec build() {

        if (!validateUrl(baseUrl) || endpoints.isEmpty()) {
            throw new RuntimeException("API bad input");
        }

        return new APISpecImpl(baseUrl, label, Collections.unmodifiableSet(endpoints));

    }

}
