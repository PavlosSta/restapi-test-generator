package org.pavlos.implementations;

import java.util.Set;

import org.pavlos.interfaces.APISpec;
import org.pavlos.interfaces.EndpointSpec;

// this is an API implementation
public class APISpecImpl implements APISpec {

	private final String baseUrl; // eg. "https://www.myapi.com/api/"
	private final String label; 	// eg. "API name"
	private final Set<EndpointSpec> endpoints;

	APISpecImpl(String baseUrl, String label, Set<EndpointSpec> endpoints) {
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

	public Set<EndpointSpec> getEndpoints() {
		return this.endpoints;
	}

}
