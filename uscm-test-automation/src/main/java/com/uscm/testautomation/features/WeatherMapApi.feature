Feature: Open Weather Api Basic Tests

@Tag1
Scenario: USCM IT Products QR Coding Challenge
	Given test user authenticates and obtains an api key to make a call
	Then user checks the weather in "London" city using "get" method on open weather api
	And user validates the success response status is "HTTP/1.1 200 OK" and status code is 200
	And user validates response from the above api call is well-formed "JSON" 
	Then user checks the weather in "fakecity" city using "get" method on open weather api
	And user validates the error response code is "HTTP/1.1 404 Not Found" and has the following error message:
		| {"cod":"404","message":"city not found"} |
	Then user checks the weather in "Dover,NH,USA" city using "get" method on open weather api		 
	And user validates response from the above api call contains following data:
		| temp |  286.87 |