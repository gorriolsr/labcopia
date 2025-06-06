package com.tecnocampus.tecnomeet;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com.tecnocampus.tecnomeet.cucumberspringboot")
@ConfigurationParameter(key="cucumber.glue", value="com.tecnocampus.tecnomeet.steps")
@ConfigurationParameter(key="cucumber.plugin", value="pretty")
public class CucumberTest {
}
