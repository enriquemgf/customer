# Customer API
> Write a HTTP endpoint API to manage customers and addresses.

## Business Requirements
Customer contains document id, name, age, registration date, last update info and one or more addresses.
addresses contains zip code, number and it can belong to one or more customers.

We should be able to insert, update, delete and query customers along with their dependencies We should be able to query customers by zip code.
## Assumptions
* document id from customer is unique;
* supports more than one customer by address;
* one customer can have more than one address ;
* zip code must have a mask 99999-999.

## Technical Spec
Write a standalone Java Application exposing either REST or GraphQL API
* Use ORM implementation as persistency;
* It must be written in Java 8 or higher.

## Areas to improve
I would've like to add the following to the application:
* Cloud support
* Rest Assured for integration tests
* API documentation with Swagger


## Sample Request
[\<hostname\>/publicApi/v1/customer](http://localhost:8080/publicApi/v1/customer)
```json
{
    "documentId": "123123123-15",
    "name": "Batman",
    "age": 31,
    "address": [{
        "zipCode": "53540-000",
        "number": "1007"
    }]
}
```