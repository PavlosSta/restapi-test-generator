package implementations;

import java.util.Set;

import interfaces.APISpec;
import interfaces.EndpointSpec;

public class APISpecBuilder {

    private String baseUrl; // eg. "https://www.myapi.com/api/"
    private String label;   // eg. "API name"
    private Set<EndpointSpec> endpoints;

    public APISpecBuilder baseUrl(String baseUrl) {
        
        // validate baseUrl
        this.baseUrl = baseUrl;
        return this;
    }

    public APISpecBuilder label(String label) {
        
        // validate label
        this.label = label;
        return this;
    }

    public APISpecBuilder endpoints(Set<EndpointSpec> endpoints) {
        
        // validate endpoints
        this.endpoints = endpoints;
        return this;
    }

    public APISpec build() {
        return new APISpecImpl(baseUrl, label, endpoints);
    }

}
