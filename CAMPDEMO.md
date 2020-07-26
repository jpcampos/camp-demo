# Api Documentation


<a name="overview"></a>
## Overview
Api Documentation


### Version information
*Version* : 1.0


### License information
*License* : Apache 2.0  
*License URL* : http://www.apache.org/licenses/LICENSE-2.0  
*Terms of service* : urn:tos


### URI scheme
*Host* : localhost:8080  
*BasePath* : /


### Tags

* basic-error-controller : Basic Error Controller
* booking-controller : Booking Controller




<a name="paths"></a>
## Paths

<a name="makebookingusingpost"></a>
### makeBooking
```
POST /campdemo/booking/
```


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**bookingDto**  <br>*required*|bookingDto|[BookingDto](#bookingdto)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|integer (int64)|
|**201**|Created|No Content|
|**400**|Bad Request|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


#### Consumes

* `application/json`


#### Produces

* `application/json`


#### Tags

* booking-controller

#### Examples to make booking post request

Sucessfull request

<img width="1307" alt="Screen Shot 2020-07-25 at 6 17 26 PM" src="https://user-images.githubusercontent.com/876282/88468607-710eae00-cea3-11ea-910c-dc404e801215.png">


Immediate request with the same dates returns error because dates are unavailable

<img width="1297" alt="Screen Shot 2020-07-25 at 6 20 53 PM" src="https://user-images.githubusercontent.com/876282/88468679-4d983300-cea4-11ea-9f2c-5c14c270135b.png">

Request with incorrect date that is more than 3 days of available reservation time

<img width="1573" alt="Screen Shot 2020-07-25 at 6 22 24 PM" src="https://user-images.githubusercontent.com/876282/88468645-cf3b9100-cea3-11ea-8cd4-c28d6367c75a.png">

Valid dates request but guest already has reservation in the same period. A guest is considered to be the same if they have the same first name, last name and email

<img width="1307" alt="Screen Shot 2020-07-25 at 6 23 41 PM" src="https://user-images.githubusercontent.com/876282/88468660-20e41b80-cea4-11ea-97ad-ba18b6f5be6c.png">



<a name="updatebookingusingput"></a>
### updateBooking
```
PUT /campdemo/booking/{id}
```


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer (int64)|
|**Body**|**bookingDto**  <br>*required*|bookingDto|[BookingDto](#bookingdto)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|integer (int64)|
|**201**|Created|No Content|
|**400**|Bad Request|No Content|
|**404**|Not Found|No Content|
|**417**|Expectation Failed|No Content|

#### Consumes

* `application/json`


#### Produces

* `application/json`


#### Tags

* booking-controller

#### Examples of update operation

A successful update to a previous booking. Only dates have changed

<img width="1314" alt="Screen Shot 2020-07-25 at 6 27 27 PM" src="https://user-images.githubusercontent.com/876282/88468704-90f2a180-cea4-11ea-89c4-e91a75173310.png">


<a name="deletebookingusingdelete"></a>
### deleteBooking
```
DELETE /campdemo/booking/{id}
```


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer (int64)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**400**|Bad Request|No Content|
|**404**|Not Found|No Content|
|**417**|Expectation Failed|No Content|


#### Produces

* `application/json`


#### Tags

* booking-controller


<a name="listavailabledatesusingget"></a>
### listAvailableDates
```
GET /campdemo/bookingdates
```


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**arrivalDate**  <br>*required*|arrivalDate|string (date)|
|**Query**|**departureDate**  <br>*required*|departureDate|string (date)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|< [LocalDate](#localdate) > array|
|**400**|Bad Request|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|

#### Examples
Example of a succesful GET request
<img width="1010" alt="Screen Shot 2020-07-25 at 5 37 16 PM" src="https://user-images.githubusercontent.com/876282/88468219-84b71600-ce9d-11ea-8599-9575d166345d.png">


This is a sample of the response

<img width="1459" alt="Screen Shot 2020-07-25 at 5 38 29 PM" src="https://user-images.githubusercontent.com/876282/88468229-af08d380-ce9d-11ea-8973-636670c3e442.png">


Example of a bad request with departure date more than 30 days into the future
<img width="1314" alt="Screen Shot 2020-07-25 at 5 50 31 PM" src="https://user-images.githubusercontent.com/876282/88468350-60f4cf80-ce9f-11ea-8889-912903520ddc.png">



#### Produces

* `application/json`


#### Tags

* booking-controller


<a name="errorhtmlusingpost"></a>
### errorHtml
```
POST /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


#### Consumes

* `application/json`


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusingget"></a>
### errorHtml
```
GET /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusingput"></a>
### errorHtml
```
PUT /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


#### Consumes

* `application/json`


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusingdelete"></a>
### errorHtml
```
DELETE /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusingpatch"></a>
### errorHtml
```
PATCH /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


#### Consumes

* `application/json`


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusinghead"></a>
### errorHtml
```
HEAD /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


#### Consumes

* `application/json`


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusingoptions"></a>
### errorHtml
```
OPTIONS /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


#### Consumes

* `application/json`


#### Produces

* `text/html`


#### Tags

* basic-error-controller




<a name="definitions"></a>
## Definitions

<a name="bookingdto"></a>
### BookingDto

|Name|Schema|
|---|---|
|**arrivalDate**  <br>*required*|string (date)|
|**bookingStatus**  <br>*optional*|enum (ACTIVE, CANCELLED, GUEST_EXISTS, DATES_UNAVAILABLE)|
|**departureDate**  <br>*required*|string (date)|
|**email**  <br>*optional*|string|
|**firstName**  <br>*optional*|string|
|**id**  <br>*optional*|integer (int64)|
|**lastName**  <br>*optional*|string|


<a name="modelandview"></a>
### ModelAndView

|Name|Schema|
|---|---|
|**empty**  <br>*optional*|boolean|
|**model**  <br>*optional*|object|
|**modelMap**  <br>*optional*|< string, object > map|
|**reference**  <br>*optional*|boolean|
|**status**  <br>*optional*|enum (100 CONTINUE, 101 SWITCHING_PROTOCOLS, 102 PROCESSING, 103 CHECKPOINT, 200 OK, 201 CREATED, 202 ACCEPTED, 203 NON_AUTHORITATIVE_INFORMATION, 204 NO_CONTENT, 205 RESET_CONTENT, 206 PARTIAL_CONTENT, 207 MULTI_STATUS, 208 ALREADY_REPORTED, 226 IM_USED, 300 MULTIPLE_CHOICES, 301 MOVED_PERMANENTLY, 302 FOUND, 302 MOVED_TEMPORARILY, 303 SEE_OTHER, 304 NOT_MODIFIED, 305 USE_PROXY, 307 TEMPORARY_REDIRECT, 308 PERMANENT_REDIRECT, 400 BAD_REQUEST, 401 UNAUTHORIZED, 402 PAYMENT_REQUIRED, 403 FORBIDDEN, 404 NOT_FOUND, 405 METHOD_NOT_ALLOWED, 406 NOT_ACCEPTABLE, 407 PROXY_AUTHENTICATION_REQUIRED, 408 REQUEST_TIMEOUT, 409 CONFLICT, 410 GONE, 411 LENGTH_REQUIRED, 412 PRECONDITION_FAILED, 413 PAYLOAD_TOO_LARGE, 413 REQUEST_ENTITY_TOO_LARGE, 414 URI_TOO_LONG, 414 REQUEST_URI_TOO_LONG, 415 UNSUPPORTED_MEDIA_TYPE, 416 REQUESTED_RANGE_NOT_SATISFIABLE, 417 EXPECTATION_FAILED, 418 I_AM_A_TEAPOT, 419 INSUFFICIENT_SPACE_ON_RESOURCE, 420 METHOD_FAILURE, 421 DESTINATION_LOCKED, 422 UNPROCESSABLE_ENTITY, 423 LOCKED, 424 FAILED_DEPENDENCY, 425 TOO_EARLY, 426 UPGRADE_REQUIRED, 428 PRECONDITION_REQUIRED, 429 TOO_MANY_REQUESTS, 431 REQUEST_HEADER_FIELDS_TOO_LARGE, 451 UNAVAILABLE_FOR_LEGAL_REASONS, 500 INTERNAL_SERVER_ERROR, 501 NOT_IMPLEMENTED, 502 BAD_GATEWAY, 503 SERVICE_UNAVAILABLE, 504 GATEWAY_TIMEOUT, 505 HTTP_VERSION_NOT_SUPPORTED, 506 VARIANT_ALSO_NEGOTIATES, 507 INSUFFICIENT_STORAGE, 508 LOOP_DETECTED, 509 BANDWIDTH_LIMIT_EXCEEDED, 510 NOT_EXTENDED, 511 NETWORK_AUTHENTICATION_REQUIRED)|
|**view**  <br>*optional*|[View](#view)|
|**viewName**  <br>*optional*|string|


<a name="view"></a>
### View

|Name|Schema|
|---|---|
|**contentType**  <br>*optional*|string|





