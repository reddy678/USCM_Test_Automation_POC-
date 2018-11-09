package com.uscm.testautomation;

import java.util.List;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;

public class WeatherMapSteps {

	private String apiKey = "";
	private Response actualResponseObject;

	/**
	 * TODO:: Best way is to call an interface class which will be implemented by an implementation class by using Spring Beans and Spring Context Configuration, 
	 * as this is a Sample POC test framework much logic has not been added.
	 */
	private WeatherMapImpl weatherMap = new WeatherMapImpl();

	@Given("^test user authenticates and obtains an api key to make a call$")
	public void getApiKey() {
		apiKey = weatherMap.getApiKey();
		System.out.println("Api Key used for this Session : " + apiKey);
	}

	@And("^user checks the weather in \"([^\"]*)\" city using \"([^\"]*)\" method on open weather api$")
	public void callOpenWeatherApi(String cityName, String httpMethod) {
		actualResponseObject = weatherMap.callOpenWeatherApi(cityName, httpMethod, apiKey);
	}

	@And("^user validates response from the above api call is well-formed \"([^\"]*)\"$")
	public void validateResponseType(String payloadType) {
		weatherMap.validateResponseType(payloadType, actualResponseObject);
	}

	@Then("^user validates the success response status is \"([^\"]*)\" and status code is (\\d+)$")
	public void validateSuccessResponse(String statusMessage, int statusCode) {
		weatherMap.validateSuccessResponse(statusMessage, statusCode, actualResponseObject);
	}

	@Then("^user validates the error response code is \"([^\"]*)\" and has the following error message:$")
	public void validateFailureResponseStatus(String statusCode, List<String> responseString) {
		weatherMap.validateFailureResponseStatus(statusCode, responseString, actualResponseObject);
	}

	@And("^user validates response from the above api call contains following data:$")
	public void validateResponseFieldValues(List<String> fieldName) {
		weatherMap.validateResponseFieldValues(fieldName, actualResponseObject);
	}

}
