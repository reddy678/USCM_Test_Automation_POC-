package com.uscm.testautomation;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/main/java/com/uscm/testautomation/features"},
		glue = {"com.uscm.testautomation"},
		name = {"^USCM IT Products QR Coding Challenge$"},
		dryRun = false )
public class WeatherMapTestRunner {

}

