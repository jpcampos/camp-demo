= Api Documentation


[[_overview]]
== Overview
Api Documentation


=== Version information
[%hardbreaks]
__Version__ : 1.0


=== License information
[%hardbreaks]
__License__ : Apache 2.0
__License URL__ : http://www.apache.org/licenses/LICENSE-2.0
__Terms of service__ : urn:tos


=== URI scheme
[%hardbreaks]
__Host__ : localhost:8080
__BasePath__ : /


=== Tags

* basic-error-controller : Basic Error Controller
* booking-controller : Booking Controller




[[_paths]]
== Paths

[[_makebookingusingpost]]
=== makeBooking
....
POST /campdemo/booking/
....


==== Parameters

[options="header", cols=".^2a,.^3a,.^9a,.^4a"]
|===
|Type|Name|Description|Schema
|**Body**|**bookingDto** +
__required__|bookingDto|<<_bookingdto,BookingDto>>
|===


==== Responses

[options="header", cols=".^2a,.^14a,.^4a"]
|===
|HTTP Code|Description|Schema
|**200**|OK|integer (int64)
|**201**|Created|No Content
|**401**|Unauthorized|No Content
|**403**|Forbidden|No Content
|**404**|Not Found|No Content
|===


==== Consumes

* `application/json`


==== Produces

* `application/json`


==== Tags

* booking-controller


[[_updatebookingusingput]]
=== updateBooking
....
PUT /campdemo/booking/{id}
....


==== Parameters

[options="header", cols=".^2a,.^3a,.^9a,.^4a"]
|===
|Type|Name|Description|Schema
|**Path**|**id** +
__required__|id|integer (int64)
|**Body**|**bookingDto** +
__required__|bookingDto|<<_bookingdto,BookingDto>>
|===


==== Responses

[options="header", cols=".^2a,.^14a,.^4a"]
|===
|HTTP Code|Description|Schema
|**200**|OK|integer (int64)
|**201**|Created|No Content
|**401**|Unauthorized|No Content
|**403**|Forbidden|No Content
|**404**|Not Found|No Content
|===


==== Consumes

* `application/json`


==== Produces

* `application/json`


==== Tags

* booking-controller


[[_deletebookingusingdelete]]
=== deleteBooking
....
DELETE /campdemo/booking/{id}
....


==== Parameters

[options="header", cols=".^2a,.^3a,.^9a,.^4a"]
|===
|Type|Name|Description|Schema
|**Path**|**id** +
__required__|id|integer (int64)
|===


==== Responses

[options="header", cols=".^2a,.^14a,.^4a"]
|===
|HTTP Code|Description|Schema
|**200**|OK|No Content
|**204**|No Content|No Content
|**401**|Unauthorized|No Content
|**403**|Forbidden|No Content
|===


==== Produces

* `application/json`


==== Tags

* booking-controller


[[_listavailabledatesusingget]]
=== listAvailableDates
....
GET /campdemo/bookingdates
....


==== Parameters

[options="header", cols=".^2a,.^3a,.^9a,.^4a"]
|===
|Type|Name|Description|Schema
|**Query**|**arrivalDate** +
__required__|arrivalDate|string (date)
|**Query**|**departureDate** +
__required__|departureDate|string (date)
|===


==== Responses

[options="header", cols=".^2a,.^14a,.^4a"]
|===
|HTTP Code|Description|Schema
|**200**|OK|< <<_localdate,LocalDate>> > array
|**401**|Unauthorized|No Content
|**403**|Forbidden|No Content
|**404**|Not Found|No Content
|===


==== Produces

* `application/json`


==== Tags

* booking-controller


[[_errorhtmlusingpost]]
=== errorHtml
....
POST /error
....


==== Responses

[options="header", cols=".^2a,.^14a,.^4a"]
|===
|HTTP Code|Description|Schema
|**200**|OK|<<_modelandview,ModelAndView>>
|**201**|Created|No Content
|**401**|Unauthorized|No Content
|**403**|Forbidden|No Content
|**404**|Not Found|No Content
|===


==== Consumes

* `application/json`


==== Produces

* `text/html`


==== Tags

* basic-error-controller


[[_errorhtmlusingget]]
=== errorHtml
....
GET /error
....


==== Responses

[options="header", cols=".^2a,.^14a,.^4a"]
|===
|HTTP Code|Description|Schema
|**200**|OK|<<_modelandview,ModelAndView>>
|**401**|Unauthorized|No Content
|**403**|Forbidden|No Content
|**404**|Not Found|No Content
|===


==== Produces

* `text/html`


==== Tags

* basic-error-controller


[[_errorhtmlusingput]]
=== errorHtml
....
PUT /error
....


==== Responses

[options="header", cols=".^2a,.^14a,.^4a"]
|===
|HTTP Code|Description|Schema
|**200**|OK|<<_modelandview,ModelAndView>>
|**201**|Created|No Content
|**401**|Unauthorized|No Content
|**403**|Forbidden|No Content
|**404**|Not Found|No Content
|===


==== Consumes

* `application/json`


==== Produces

* `text/html`


==== Tags

* basic-error-controller


[[_errorhtmlusingdelete]]
=== errorHtml
....
DELETE /error
....


==== Responses

[options="header", cols=".^2a,.^14a,.^4a"]
|===
|HTTP Code|Description|Schema
|**200**|OK|<<_modelandview,ModelAndView>>
|**204**|No Content|No Content
|**401**|Unauthorized|No Content
|**403**|Forbidden|No Content
|===


==== Produces

* `text/html`


==== Tags

* basic-error-controller


[[_errorhtmlusingpatch]]
=== errorHtml
....
PATCH /error
....


==== Responses

[options="header", cols=".^2a,.^14a,.^4a"]
|===
|HTTP Code|Description|Schema
|**200**|OK|<<_modelandview,ModelAndView>>
|**204**|No Content|No Content
|**401**|Unauthorized|No Content
|**403**|Forbidden|No Content
|===


==== Consumes

* `application/json`


==== Produces

* `text/html`


==== Tags

* basic-error-controller


[[_errorhtmlusinghead]]
=== errorHtml
....
HEAD /error
....


==== Responses

[options="header", cols=".^2a,.^14a,.^4a"]
|===
|HTTP Code|Description|Schema
|**200**|OK|<<_modelandview,ModelAndView>>
|**204**|No Content|No Content
|**401**|Unauthorized|No Content
|**403**|Forbidden|No Content
|===


==== Consumes

* `application/json`


==== Produces

* `text/html`


==== Tags

* basic-error-controller


[[_errorhtmlusingoptions]]
=== errorHtml
....
OPTIONS /error
....


==== Responses

[options="header", cols=".^2a,.^14a,.^4a"]
|===
|HTTP Code|Description|Schema
|**200**|OK|<<_modelandview,ModelAndView>>
|**204**|No Content|No Content
|**401**|Unauthorized|No Content
|**403**|Forbidden|No Content
|===


==== Consumes

* `application/json`


==== Produces

* `text/html`


==== Tags

* basic-error-controller




[[_definitions]]
== Definitions

[[_bookingdto]]
=== BookingDto

[options="header", cols=".^3a,.^4a"]
|===
|Name|Schema
|**arrivalDate** +
__required__|string (date)
|**bookingStatus** +
__optional__|enum (ACTIVE, CANCELLED, GUEST_EXISTS, DATES_UNAVAILABLE)
|**departureDate** +
__required__|string (date)
|**email** +
__optional__|string
|**firstName** +
__optional__|string
|**id** +
__optional__|integer (int64)
|**lastName** +
__optional__|string
|===


[[_modelandview]]
=== ModelAndView

[options="header", cols=".^3a,.^4a"]
|===
|Name|Schema
|**empty** +
__optional__|boolean
|**model** +
__optional__|object
|**modelMap** +
__optional__|< string, object > map
|**reference** +
__optional__|boolean
|**status** +
__optional__|enum (100 CONTINUE, 101 SWITCHING_PROTOCOLS, 102 PROCESSING, 103 CHECKPOINT, 200 OK, 201 CREATED, 202 ACCEPTED, 203 NON_AUTHORITATIVE_INFORMATION, 204 NO_CONTENT, 205 RESET_CONTENT, 206 PARTIAL_CONTENT, 207 MULTI_STATUS, 208 ALREADY_REPORTED, 226 IM_USED, 300 MULTIPLE_CHOICES, 301 MOVED_PERMANENTLY, 302 FOUND, 302 MOVED_TEMPORARILY, 303 SEE_OTHER, 304 NOT_MODIFIED, 305 USE_PROXY, 307 TEMPORARY_REDIRECT, 308 PERMANENT_REDIRECT, 400 BAD_REQUEST, 401 UNAUTHORIZED, 402 PAYMENT_REQUIRED, 403 FORBIDDEN, 404 NOT_FOUND, 405 METHOD_NOT_ALLOWED, 406 NOT_ACCEPTABLE, 407 PROXY_AUTHENTICATION_REQUIRED, 408 REQUEST_TIMEOUT, 409 CONFLICT, 410 GONE, 411 LENGTH_REQUIRED, 412 PRECONDITION_FAILED, 413 PAYLOAD_TOO_LARGE, 413 REQUEST_ENTITY_TOO_LARGE, 414 URI_TOO_LONG, 414 REQUEST_URI_TOO_LONG, 415 UNSUPPORTED_MEDIA_TYPE, 416 REQUESTED_RANGE_NOT_SATISFIABLE, 417 EXPECTATION_FAILED, 418 I_AM_A_TEAPOT, 419 INSUFFICIENT_SPACE_ON_RESOURCE, 420 METHOD_FAILURE, 421 DESTINATION_LOCKED, 422 UNPROCESSABLE_ENTITY, 423 LOCKED, 424 FAILED_DEPENDENCY, 425 TOO_EARLY, 426 UPGRADE_REQUIRED, 428 PRECONDITION_REQUIRED, 429 TOO_MANY_REQUESTS, 431 REQUEST_HEADER_FIELDS_TOO_LARGE, 451 UNAVAILABLE_FOR_LEGAL_REASONS, 500 INTERNAL_SERVER_ERROR, 501 NOT_IMPLEMENTED, 502 BAD_GATEWAY, 503 SERVICE_UNAVAILABLE, 504 GATEWAY_TIMEOUT, 505 HTTP_VERSION_NOT_SUPPORTED, 506 VARIANT_ALSO_NEGOTIATES, 507 INSUFFICIENT_STORAGE, 508 LOOP_DETECTED, 509 BANDWIDTH_LIMIT_EXCEEDED, 510 NOT_EXTENDED, 511 NETWORK_AUTHENTICATION_REQUIRED)
|**view** +
__optional__|<<_view,View>>
|**viewName** +
__optional__|string
|===


[[_view]]
=== View

[options="header", cols=".^3a,.^4a"]
|===
|Name|Schema
|**contentType** +
__optional__|string
|===





