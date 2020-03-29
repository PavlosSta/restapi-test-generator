package implementations;

import java.util.Set;

import interfaces.APISpecification;
import interfaces.Endpoint;

// this is an API implementation
public class APISpecificationImpl implements APISpecification {

	String baseUrl; // eg. "https://www.myapi.com/api/"
	String label; 	// eg. "API name"

	Set<Endpoint> endpoints;

	public APISpecificationImpl(String baseUrl, String label, Set<Endpoint> endpoints) {
		this.baseUrl = baseUrl;
		this.label = label;
		this.endpoints = endpoints;
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public String getLabel() {
		return this.label;
	}

	public Set<Endpoint> getEndpoints() {
		return this.endpoints;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setEndpoints(Set<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

}
