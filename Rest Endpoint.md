# Request pattern

## Methods
GET, PUT, POST, PATCH, DELETE

## URI

http(s)://uri(:port)/baseUrl/endpoint?param1=value1&param2=value2

## Headers (Key: value)

Content-type: application/json

Accept: application/json

Authorization: Bearer token

## Body
GET, DELETE: None

PUT, POST, PATCH:
{
    "var1": "val1",
    "var2": "val2",
    ...
    "vanN": "valN"
}
