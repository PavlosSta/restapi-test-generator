# Σημειώσεις

Ας βάζουμε εδώ κάθε τι που μπορεί να θεωρούμε χρήσιμο/σχετικό :)

## Υποστηριζόμενα Endpoints από Frameworks

* Spring Boot: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-endpoints
* Micronaut: https://docs.micronaut.io/latest/guide/index.html#providedEndpoints

## Βιβλιοθήκες / frameworks για testing

Που μπορεί να μας ενδιαφέρουν, π.χ. για να παράγουμε κώδικα "γραμμένο" για κάποιο από αυτά

* Junit
* Spock
* Rest-assured

## Gradle

* Java Plugin: https://docs.gradle.org/current/userguide/java_plugin.html

## Δομή API

* Ένα APISpec έχει:
    * Ένα Label (ίσως)
    * Ένα BaseUrl
    * Τουλάχιστον ένα Endpoints
    
* Ένα Endpoint έχει:
    * Ένα Path
    * Ένα Label (ίσως)
    * Ένα Attribute (ίσως)
    * Τουλάχιστον μία μέθοδο
    
* Μία Method έχει:
    * Έναν Type
    * Ένα Request
    * Ένα Response
    
* Ένα Request έχει:
    * Ένα Request Body (ίσως)
    * Κάποιους Headers (ίσως)
    * Κάποιες Query Parameters (ίσως)
    
* Ένα Response έχει:
    * Ένα Response Body (ίσως)
    * Κάποιους Headers (ίσως)
    * Ένα status (200 ή 201)
    
* Ένας Header έχει:
    * Ένα Name
    * Μία Value
    * Αν είναι υποχρεωτικός ή όχι
    * Αν είναι υποχρεωτικός, τότε ένα *Default Value If Optional And Missing*

* Μία Query Parameter έχει:
    * Ένα Name
    * Έναν Type (String, Integer etc.)
    * Μία Value
    * Μία Default Value
    * Αν είναι υποχρεωτική ή όχι


## Παραδοχές
1. Όλα JSON
2. Ένα Endpoint μπορεί να έχει είτε κανένα, είτε ένα attribute
3. PUT, PATCH, DELETE οπωσδήποτε attribute
4. POST χωρίς attribute
5. Ελέγχουμε μόνο 200 positive status, όχι error handling behaviour, μόνο success
6. URL Encoded Request, 

request bodyParams content types
request bodyParams encoding του url encoded, www-form-urlencoded --> request bodyParams parameters

https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/POST