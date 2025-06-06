package com.tecnocampus.tecnomeet.steps;

import com.tecnocampus.tecnomeet.persistence.StudentRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StudentSteps {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StudentRepository repository;
    private ResultActions response;

    @Before
    public void clean() {
        repository.removeAll();
    }

    @When("I register a student with:")
    public void iRegisterAStudentWith(DataTable dataTable) throws Exception {
        response = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(dataTable)));
    }

    @Then("the registration status should be {string}")
    public void theRegistrationStatusShouldBe(String code) throws Exception {
        response.andExpect(status().is(Integer.parseInt(code)));
    }

    private String toJson(DataTable table) {
        JSONObject json = new JSONObject();
        for (Map<String, String> map : table.asMaps(String.class, String.class)) {
            for (var e : map.entrySet()) {
                json.put(e.getKey(), e.getValue());
            }
        }
        return json.toString();
    }
}
