package de.valoplus.web.rest;

import de.valoplus.LedhubApp;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Created by tom on 12.02.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LedhubApp.class)
@WebAppConfiguration
public class StatusResourceTest {
    private final static String DEVICE_ID = "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private Attributes.Attribute optional = key("optional").value(true);

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(this.restDocumentation)).build();
    }

    @Test
    public void getStatus() throws Exception {
        mockMvc.perform(
            get("/api/states").param("clientId", DEVICE_ID))
               .andExpect(status().isOk())
               .andDo(document("state-get", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                   requestParameters(
                       parameterWithName("clientId").description("The unique id of the device")),
                   responseFields(
                       fieldWithPath("[].channel")
                           .description("The unique name of the Channel")
                           .type(JsonFieldType.STRING),
                       fieldWithPath("[].active")
                           .description("is the channel on or off")
                           .type(JsonFieldType.BOOLEAN),
                       fieldWithPath("[].state")
                           .description("The specialized structure of this state")
                           .type(JsonFieldType.OBJECT),
                       fieldWithPath("[].type")
                           .description("The type for the state")
                           .type(JsonFieldType.OBJECT)))
               );
    }

    @Test
    public void getSingleStatus() throws Exception {
        mockMvc.perform(
            get("/api/state").param("clientId", DEVICE_ID).param("name", "Channel1"))
               .andExpect(status().isOk())
               .andDo(document("one-state-get", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                   requestParameters(
                       parameterWithName("clientId").description("The unique id of the device"),
                       parameterWithName("name").description("The name of the channel to get the state")),
                   responseFields(
                       fieldWithPath("channel")
                           .description("The unique name of the Channel")
                           .type(JsonFieldType.STRING),
                       fieldWithPath("active")
                           .description("is the channel on or off")
                           .type(JsonFieldType.BOOLEAN),
                       fieldWithPath("state")
                           .description("The specialized structure of this state")
                           .type(JsonFieldType.OBJECT),
                       fieldWithPath("type")
                           .description("The type for the state")
                           .type(JsonFieldType.OBJECT)))
               );
    }
}
