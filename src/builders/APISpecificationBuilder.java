package builders;

import java.util.Set;

import interfaces.APISpecification;
import interfaces.Endpoint;
import implementations.APISpecificationImpl;

public class APISpecificationBuilder {

    private String baseUrl; // eg. "https://www.myapi.com/api/"
    private String label;   // eg. "API name"

    private Set<Endpoint> endpoints;

    public APISpecificationBuilder baseUrl(String baseUrl) {
        
        // validate baseUrl
        this.baseUrl = baseUrl;
        return this;
    }

    public APISpecificationBuilder label(String label) {
        
        // validate label
        this.label = label;
        return this;
    }

    public APISpecificationBuilder endpoints(Set<Endpoint> endpoints) {
        
        // validate endpoints
        this.endpoints = endpoints;
        return this;
    }

    public APISpecification build() {
        return new APISpecificationImpl(baseUrl, label, endpoints);
    }

}
