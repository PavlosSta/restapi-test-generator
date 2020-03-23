package api;

public class Endpoint {

    String path;    // eg. "prices"
    String label;   // eg. "endpoint for prices"

    Collection<String> docs;    // notes for using this endpoint

    Collection<Method> methods; // endpoint 1-N methods

}
