package implementations;

import java.util.Set;

import interfaces.APISpec;
import interfaces.EndpointSpec;

public class APISpecBuilder {

    private String baseUrl; // eg. "https://www.myapi.com/api/"
    private String label;   // eg. "API name"
    private Set<EndpointSpec> endpoints;

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

    public APISpec build() {

        //TODO validate

        return new APISpecImpl(baseUrl, label, endpoints);
    }

}
