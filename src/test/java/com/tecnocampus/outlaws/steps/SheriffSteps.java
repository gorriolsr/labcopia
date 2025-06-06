package com.tecnocampus.outlaws.steps;

import com.tecnocampus.outlaws.persistence.UserRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SheriffSteps {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private ResultActions response;
    private String sheriffId;
    private String outlawId;

    @Before
    public void cleanBBDD() {
        userRepository.removeAll();
    }


    @When("I create a sheriff with:")
    public void iCreateASheriffWith(io.cucumber.datatable.DataTable dataTable) throws Exception {
        response = mockMvc.perform(post("/sheriffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(initJsonFromDataTable(dataTable)));
    }

    private String initJsonFromDataTable(DataTable dataTable) throws JSONException {
        Map<String, String> data = dataTable.asMaps(String.class, String.class).get(0);
        JSONObject json = new JSONObject();
        if (data.containsKey("name")) json.put("name", data.get("name"));
        if (data.containsKey("salary")) json.put("salary", Integer.valueOf(data.get("salary")));
        if (data.containsKey("captures")) json.put("captures", Integer.valueOf(data.get("captures")));
        if (data.containsKey("eliminations")) json.put("eliminations", Integer.valueOf(data.get("eliminations")));
        return json.toString();
    }


    @Then("the response status is {string}")
    public void theResponseStatusIs(String status) throws Exception {
        response.andExpect(status().is(Integer.parseInt(status)));
    }

    @And("the response body contains:")
    public void theResponseBodyContains(io.cucumber.datatable.DataTable dataTable) throws Exception {
        for (Map<String, String> map : dataTable.asMaps(String.class, String.class)) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                response.andExpect(jsonPath("$." + entry.getKey()).value(entry.getValue()));
            }
        }
    }

    @And("the response body should have {string}")
    public void theResponseBodyShouldHave(String field) throws Exception {
        response.andExpect(jsonPath("$." + field).exists());
    }


    @When("I retrieve the sheriff by ID")
    public void iRetrieveTheSheriffById() throws Exception {
        response = mockMvc.perform(get("/sheriffs/" + sheriffId));
    }

    @When("I delete the sheriff")
    public void iDeleteTheSheriff() throws Exception {
        response = mockMvc.perform(delete("/sheriffs/" + sheriffId));
    }

    @When("I retrieve a sheriff with an invalid ID")
    public void iRetrieveASheriffWithInvalidID() throws Exception {
        response = mockMvc.perform(get("/sheriffs/invalid-id"));
    }

    @Given("an outlaw created with:")
    public void anOutlawCreatedWith(io.cucumber.datatable.DataTable dataTable) throws Exception {
        List<Map<String, String>> outlaws = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> outlawData : outlaws) {
            response = mockMvc.perform(post("/outlaws")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new JSONObject(outlawData).toString()));
            JSONObject json = new JSONObject(response.andReturn().getResponse().getContentAsString());
            outlawId = json.getString("id");
            assert outlawId != null && !outlawId.isEmpty() : "Generated outlaw ID is empty";
        }
    }

    @When("I list all sheriffs")
    public void iListAllSheriffs() throws Exception {
        response = mockMvc.perform(get("/sheriffs"));
    }

    @When("I update the sheriff with:")
    public void iUpdateTheSheriffWith(io.cucumber.datatable.DataTable dataTable) throws Exception {
        response = mockMvc.perform(patch("/sheriffs/" + sheriffId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(initJsonFromDataTable(dataTable)));
    }

    @When("I delete a non-existing sheriff")
    public void iDeleteANonExistingSheriff() throws Exception {
        response = mockMvc.perform(delete("/sheriffs/invalid-id"));
    }

    @When("the sheriff captures the outlaw")
    public void theSheriffCapturesTheOutlaw() throws Exception {
        response = mockMvc.perform(post("/sheriffs/" + sheriffId + "/capture/" + outlawId));
    }


    @When("the sheriff tries to capture a non-existing outlaw")
    public void the_sheriff_tries_to_capture_a_non_existing_outlaw() throws Exception {
        response = mockMvc.perform(post("/sheriffs/" + sheriffId + "/capture/" + "invalidId"));
    }

    @When("the sheriff tries to eliminate a non-existing outlaw")
    public void the_sheriff_tries_to_eliminate_a_non_existing_outlaw() throws Exception {
        response = mockMvc.perform(post("/sheriffs/" + sheriffId + "/eliminate/" + "invalidId"));
    }

    @When("the sheriff eliminates the outlaw")
    public void theSheriffEliminatesTheOutlaw() throws Exception {
        response = mockMvc.perform(post("/sheriffs/" + sheriffId + "/eliminate/" + outlawId));
        ;
    }


    @When("I update a sheriff that does not exist with:")
    public void i_update_a_sheriff_that_does_not_exist_with(io.cucumber.datatable.DataTable dataTable) throws
            Exception {
        response = mockMvc.perform(put("/sheriffs/invalid-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(initJsonFromDataTable(dataTable)));
    }

    @Given("a sheriff exists with:")
    public void aSheriffExistsWith(io.cucumber.datatable.DataTable dataTable) throws Exception {
        List<Map<String, String>> sheriffs = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> sheriffData : sheriffs) {
            ResultActions response = mockMvc.perform(post("/sheriffs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new JSONObject(sheriffData).toString()));

            JSONObject json = new JSONObject(response.andReturn().getResponse().getContentAsString());
            sheriffId = json.getString("id");

            assert sheriffId != null && !sheriffId.isEmpty() : "Generated sheriff ID is empty";
        }
    }


    @When("I delete the sheriff with ID for Wyatt Earp")
    public void i_delete_the_sheriff_with_id_for_wyatt_earp() throws Exception {
        response = mockMvc.perform(delete("/sheriffs/" + sheriffId));
    }

    @Then("the response should contain a list with {int} item")
    public void the_response_should_contain_a_list_with_item(Integer expectedCount) throws Exception {
        response.andExpect(jsonPath("$.length()").value(expectedCount));
    }


    @Then("the response body should contain the sheriff resource with:")
    public void the_response_body_should_contain_the_sheriff_resource_with(io.cucumber.datatable.DataTable
                                                                                   dataTable) throws Exception {
        for (Map<String, String> entry : dataTable.asMaps(String.class, String.class)) {
            for (Map.Entry<String, String> field : entry.entrySet()) {
                response.andExpect(jsonPath("$." + field.getKey()).value(field.getValue()));
            }
        }
    }

}
