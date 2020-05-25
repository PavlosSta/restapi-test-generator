
api = new APISpec;

<#list api.endpoints as endpoint>
    ${endpoint.path}
</#list>