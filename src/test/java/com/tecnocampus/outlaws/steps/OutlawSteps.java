package com.tecnocampus.outlaws.steps;

import com.tecnocampus.outlaws.persistence.UserRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.awaitility.Awaitility;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OutlawSteps {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private ResultActions response;
    private String outlawId;


    @Before
    public void cleanBBDD() {
        userRepository.removeAll();
    }

    @When("I create an outlaw with:")
    public void iCreateAnOutlawWith(io.cucumber.datatable.DataTable dataTable) throws Exception {
        response = mockMvc.perform(post("/outlaws")
                .contentType(MediaType.APPLICATION_JSON)
                .content(initJsonFromDataTable(dataTable)));
    }

    @Then("the response status should be {string}")
    public void theResponseStatusShouldBe(String status) throws Exception {
        response.andExpect(status().is(Integer.parseInt(status)));
    }

    @And("the response body should contain:")
    public void theResponseBodyShouldContain(io.cucumber.datatable.DataTable dataTable) throws Exception {
        for (Map<String, String> map : dataTable.asMaps(String.class, String.class)) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                response.andExpect(jsonPath("$." + entry.getKey()).value(entry.getValue()));
            }
        }
    }

    @And("the response body should have a generated {string}")
    public void theResponseBodyShouldHaveAGenerated(String field) throws Exception {
        response.andExpect(jsonPath("$." + field).exists());
    }

    @Given("an outlaw exists with:")
    public void anOutlawExistsWith(io.cucumber.datatable.DataTable dataTable) throws Exception {
        ResultActions response = mockMvc.perform(post("/outlaws")
                .contentType(MediaType.APPLICATION_JSON)
                .content(initJsonFromDataTable(dataTable)));

        JSONObject json = new JSONObject(response.andReturn().getResponse().getContentAsString());
        outlawId = json.getString("id");
    }

    @When("I retrieve the outlaw by ID")
    public void iRetrieveTheOutlawById() throws Exception {
        response = mockMvc.perform(get("/outlaws/" + outlawId));
    }

    @When("I retrieve an outlaw with an invalid ID")
    public void iRetrieveAnOutlawWithInvalidID() throws Exception {
        response = mockMvc.perform(get("/outlaws/invalid-id"));
    }

    @When("I list all outlaws")
    public void iListAllOutlaws() throws Exception {
        response = mockMvc.perform(get("/outlaws"));
    }

    @When("I update the outlaw with:")
    public void iUpdateTheOutlawWith(io.cucumber.datatable.DataTable dataTable) throws Exception {
        response = mockMvc.perform(put("/outlaws/" + outlawId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(initJsonFromDataTable(dataTable)));
    }

    private String initJsonFromDataTable(DataTable dataTable) {
        return new JSONObject(dataTable.asMaps(String.class, String.class).get(0)).toString();
    }


    @When("I update the outlaw bounty with:")
    public void iUpdateTheOutlawBountyWith(io.cucumber.datatable.DataTable dataTable) throws Exception {
        response = mockMvc.perform(patch("/outlaws/" + outlawId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(initJsonFromDataTable(dataTable)));
    }


    @When("I request the most wanted outlaw")
    public void iRequestTheMostWantedOutlaw() throws Exception {
        response = mockMvc.perform(get("/outlaws/most-wanted"));
    }

    @When("I delete the outlaw")
    public void iDeleteTheOutlaw() throws Exception {
        response = mockMvc.perform(delete("/outlaws/" + outlawId));
    }

    @Given("the outlaw ID is generated")
    public void the_outlaw_id_is_generated() {
        assert outlawId != null && !outlawId.isEmpty() : "Generated outlaw ID is empty";
    }

    @Given("outlaws exist:")
    public void outlaws_exist(io.cucumber.datatable.DataTable dataTable) throws Exception {
        for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
            response = mockMvc.perform(post("/outlaws")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new JSONObject(data).toString()));
        }
    }

    @Then("the response should contain a list with {int} items")
    public void the_response_should_contain_a_list_with_items(Integer expectedCount) throws Exception {
        response.andExpect(jsonPath("$.length()").value(expectedCount));
    }

    @When("I find the most wanted outlaw")
    public void i_find_the_most_wanted_outlaw() throws Exception {
        response = mockMvc.perform(get("/outlaws/most-wanted"));
    }

}

