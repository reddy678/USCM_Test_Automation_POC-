package com.uscm.testautomation;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WeatherMapImpl {

	private static Logger logger = LogManager.getLogger(WeatherMapImpl.class);

	private static final String OPEN_WEATHER_API_URI 					= "https://api.openweathermap.org";
	private static final String API_VERSION 							= "/data/2.5";
	private static final String WEATHER_API_WITH_QUERY_PARAMETER 		= "/weather?q=";
	private static final String APPID 									= "&APPID=";

	/**
	 * As this is POC project api key is hard coded which in real time should be
	 * retrieved for every api call
	 * @return
	 */
	public String getApiKey() {
		String apiKey = "04a566a0cde4b45f95a7128fd9217c0d";
		return apiKey;
	}

	/**
	 * 
	 * @param cityName
	 * @param httpMethod
	 * @param apiKey
	 * @return
	 */
	public Response callOpenWeatherApi(String cityName, String httpMethod, String apiKey) {
		try{
			Response response = null;
			String restServiceUrl = API_VERSION + WEATHER_API_WITH_QUERY_PARAMETER + cityName + APPID + apiKey;
			System.out.println("Constructed URI is: " + restServiceUrl);
			RestAssured.baseURI = OPEN_WEATHER_API_URI;
			RequestSpecification requestSpecification = RestAssured.given();
			requestSpecification.header("cache-control", "no-cache");
			if (httpMethod.equals("get")) {
				response = requestSpecification.relaxedHTTPSValidation().when().get(restServiceUrl);
			}
			return response;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param expectedStatusLine
	 * @param expectedStatusCode
	 * @param actualResponseObject
	 */
	public void validateSuccessResponse(String expectedStatusLine, int expectedStatusCode,
			Response actualResponseObject) {
		int actualStatusCode;
		String actualStatusLine = null;
		if (!actualResponseObject.equals(null)) {
			actualStatusCode = actualResponseObject.getStatusCode();
			actualStatusLine = actualResponseObject.getStatusLine();
			if (expectedStatusCode != actualStatusCode) {
				LogFail("Expected Status code doesn't match the actual: " + actualStatusCode);
			}
			if (!expectedStatusLine.equals(actualStatusLine)) {
				LogFail("Expected Status Line doesn't match the actual: " + actualStatusLine);
			}
		}
	}
	
	/**
	 * 
	 * @param expectedStatusLine
	 * @param responseString
	 * @param actualResponseObject
	 */
	public void validateFailureResponseStatus(String expectedStatusLine, List<String> responseString,
			Response actualResponseObject) {
		int index = 0;
		String expectedResponseString = responseString.get(index).trim();
		String actualStatusLine = null;
		if (!actualResponseObject.equals(null)) {
			actualStatusLine = actualResponseObject.getStatusLine();
			if (!expectedStatusLine.equals(actualStatusLine)) {
				LogFail("Expected Status Line doesn't match the actual: " + actualStatusLine);
			}
			String actualResponseString = actualResponseObject.getBody().asString().trim();
			if (!actualResponseString.equals(expectedResponseString)) {
				LogFail("Expected Response: " + expectedResponseString + ", String doesn't match the actual: "
						+ actualResponseString);
			}
		}
	}

	/**
	 * Below Method can be best validated by using POJOs and its own Json
	 * object. I just tried to check wether its a JSON object or not.
	 * 
	 * @param payloadType
	 * @param actualResponseObject
	 */
	public void validateResponseType(String payloadType, Response actualResponseObject) {
		if (!actualResponseObject.equals(null)) {
			String responseString = actualResponseObject.getBody().asString();
			if (!isJSONValid(responseString)) {
				LogFail("Response payload is not in Well-formed JSON Format");
			}
		}
	}

	/**
	 * 
	 * @param responseString
	 * @return
	 */
	public boolean isJSONValid(String responseString) {
		try {
			new JSONObject(responseString);
		} catch (JSONException ex) {
			try {
				new JSONArray(responseString);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param fieldName
	 * @param actualResponseObject
	 */
	public void validateResponseFieldValues(List<String> fieldName, Response actualResponseObject) {
		if (!actualResponseObject.equals(null)) {
			String jsonString = actualResponseObject.getBody().asString();
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				String fieldValue = jsonObject.getJSONObject("main").getString(fieldName.get(0)).trim();
				Double actualResult = Double.parseDouble(fieldValue);
				Double expectedResult = Double.parseDouble(fieldName.get(1));
				if (actualResult > expectedResult + 10 || actualResult < expectedResult - 10) {
					LogFail(" Expected Temparature:  " + expectedResult + " not in range, actual found: "
							+ actualResult);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * 
	 * @param failureMessage
	 */
	private void LogFail(String failureMessage) {
		BasicConfigurator.configure();
		logger.error(failureMessage);
		assertTrue("FAILED:: " + LocalDateTime.now() + " , " + failureMessage, false);
	}
}
