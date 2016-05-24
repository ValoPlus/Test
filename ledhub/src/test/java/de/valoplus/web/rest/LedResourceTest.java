package de.valoplus.web.rest;

import de.valoplus.LedhubApp;
import de.valoplus.wlan.Wlan;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static de.valoplus.web.rest.MapUtil.map;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Created by tom on 12.02.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LedhubApp.class)
@WebAppConfiguration
public class LedResourceTest {

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

    private ResultActions doInit() throws Exception {
        Map<String, String> content = map("key", "123456789abc")
            .last("clientId", "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4");

        return mockMvc.perform(post("/api/init")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(content)));
    }

    private ResultActions postSettings() throws Exception {
        Wlan wlan = new Wlan("neoWlan", "secret", Wlan.WLANSecurity.WPA2);

        return mockMvc.perform(post("/api/settings/wlan").header("Authorization", "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4")
                                                    .contentType(TestUtil.APPLICATION_JSON_UTF8)
                                                    .content(TestUtil.convertObjectToJsonBytes(wlan)));
    }

    @Test
    public void testError() throws Exception {

    }

    @Test
    public void testInit() throws Exception {
        doInit().andExpect(status().isOk())
                .andDo(document("init", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("key")
                            .description("The security-key of the controller")
                            .type(JsonFieldType.STRING),
                        fieldWithPath("clientId")
                            .description("The unique id of the device")
                            .type(JsonFieldType.STRING)),
                    responseFields(
                        fieldWithPath("controllerType")
                            .description("The type of the controller")
                            .type(JsonFieldType.STRING),
                        fieldWithPath("availableChannel")
                            .description("The available channels on the controller")
                            .type(JsonFieldType.NUMBER),
                        fieldWithPath("controllerAlias")
                            .description("The Name of the controller")
                            .type(JsonFieldType.STRING),
                        fieldWithPath("configured")
                            .description("Is the controller already configured?")
                            .type(JsonFieldType.BOOLEAN)
                    )));
    }

    @Test
    public void testInit400() throws Exception {
        Map<String, String> content = map("key", "123456")
            .last("clientId", "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4");

        mockMvc.perform(post("/api/init")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(content)))
               .andExpect(status().isUnauthorized())
               .andDo(document("init-401", preprocessRequest(prettyPrint())));
    }

    @Test
    public void setSettings() throws Exception {
        doInit();

        postSettings()
            .andExpect(status().isOk()).andExpect(content().string("Wlan settings saved. Controller will try to reconnect."))
            .andDo(document("save-settings", preprocessRequest(prettyPrint()),
                requestFields(
                    fieldWithPath("pass")
                        .description("The key for the WLAN")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("ssid")
                        .description("The SSID / name for the WLAN")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("wlanSecurity")
                        .description("The Security System. Values: WPA, WPA2, NONE")
                        .type(JsonFieldType.STRING)
                ),
                requestHeaders(headerWithName("Authorization").description("The unique id of the device"))));
    }

/*    @Test
    public void setSettings412() throws Exception {
        doInit();

        Map<Object, Object> content = new HashMap<>();

        this.mockMvc.perform(post("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(content)))
                    .andExpect(status().isPreconditionFailed())
                    .andDo(document("save-settings-412", preprocessResponse(prettyPrint()),
                        responseFields(
                            fieldWithPath("message")
                                .description("A description of the cause of the error")
                                .type(JsonFieldType.STRING),
                            fieldWithPath("description")
                                .description("A description of the cause of the error")
                                .type(JsonFieldType.STRING),
                            fieldWithPath("fieldErrors")
                                .ignored()
                        )));
    }*/

/*    @Test
    public void setSettings401() throws Exception {
        doInit();

        Map<String, String> content = map("name", "controller2")
            .last("clientId", "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E5");

        this.mockMvc.perform(post("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(content)))
                    .andExpect(status().isUnauthorized())
                    .andDo(document("save-settings-401", preprocessResponse(prettyPrint()),
                        responseFields(
                            fieldWithPath("message")
                                .description("A description of the cause of the error")
                                .type(JsonFieldType.STRING),
                            fieldWithPath("description").ignored(),
                            fieldWithPath("fieldErrors").ignored()
                        )));
    }*/

    @Test
    public void getSettings() throws Exception {
        doInit();
        postSettings();

        this.mockMvc.perform(get("/api/settings").param("clientId", "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("controllerAlias").value("mock"))
                    .andDo(document("get-settings", preprocessResponse(prettyPrint()),
                        requestParameters(
                            parameterWithName("clientId").description("The unique id of the device")),
                        responseFields(
                            fieldWithPath("clientId").ignored(),
                            fieldWithPath("controllerAlias")
                                .description("A name to identify the controller")
                                .type(JsonFieldType.STRING),
                            fieldWithPath("wlan")
                                .description("The authentication for the WLAN")
                                .attributes(optional)
                                .type(JsonFieldType.OBJECT),
                            fieldWithPath("wlan.pass")
                                .description("The key for the WLAN")
                                .type(JsonFieldType.STRING),
                            fieldWithPath("wlan.ssid")
                                .description("The SSID / name for the WLAN")
                                .type(JsonFieldType.STRING),
                            fieldWithPath("wlan.wlanSecurity")
                                .description("The Security System. Values: WPA, WPA2, NONE")
                                .type(JsonFieldType.STRING))));
    }
}
