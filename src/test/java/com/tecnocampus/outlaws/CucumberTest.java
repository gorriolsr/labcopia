package com.tecnocampus.outlaws;


import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com.tecnocampus.outlaws.cucumberspringboot")
@ConfigurationParameter(key = "cucumber.glue", value = "com.tecnocampus.outlaws.steps") // Step definitions package
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty, summary")
public class CucumberTest {
}