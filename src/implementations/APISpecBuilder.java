package implementations;

import java.util.Set;

import interfaces.APISpec;
import interfaces.EndpointSpec;

public class APISpecBuilder {

    private String baseUrl; // eg. "https://www.myapi.com/api/"
    private String label;   // eg. "API name"
    private Set<EndpointSpec> endpoints;

    public APISpecBuilder setBaseUrl(String baseUrl) {
        
        // validate baseUrl
        this.baseUrl = baseUrl;
        return this;
    }

    public APISpecBuilder setLabel(String label) {
        
        // validate label
        this.label = label;
        return this;
    }

    public APISpecBuilder setEndpoints(Set<EndpointSpec> endpoints) {

        // validate endpoints
        this.endpoints = endpoints;
        return this;
    }

    public APISpecBuilder addEndpoints(Set<EndpointSpec> endpoints) {
        
        // validate endpoints
        this.endpoints.addAll(endpoints);
        return this;
    }

    public APISpecBuilder addEndpoints(EndpointSpec endpoint) {

        // validate endpoints
        this.endpoints.add(endpoint);
        return this;
    }

    public APISpec build() {
        return new APISpecImpl(baseUrl, label, endpoints);
    }

}
