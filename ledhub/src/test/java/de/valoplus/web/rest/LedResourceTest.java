package de.valoplus.web.rest;

import de.valoplus.LedhubApp;
import de.valoplus.channel.*;
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

import java.util.HashMap;
import java.util.Map;

import static de.valoplus.web.rest.MapUtil.map;
import static de.valoplus.web.rest.MapUtil.oMap;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
        Wlan wlan = new Wlan("neoWlan", "secret2", Wlan.WLANSecurity.WPA2);

        Channel channel1 = new Channel("Channel1", ChannelTypes.WS2812.name(), new ChannelTypeWS2812(1, 25));
        Channel channel2 = new Channel("Channel2", ChannelTypes.LED_STRIP_RGB.name(), new ChannelTypeRGB(2, 4, 3));
        Channel channel3 = new Channel("Channel3", ChannelTypes.LED_STRIP.name(), new ChannelTypeSingleColor(5));

        Map<Object, Object> content = oMap("controllerAlias", "mock")
            .put("clientId", "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4")
            .last("wlan", wlan)
            /*.last("channel", Lists.newArrayList(channel1, channel2, channel3))*/;

        return mockMvc.perform(post("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(content)));
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
                            .type(JsonFieldType.NUMBER)
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
            .andExpect(status().isOk())
            .andDo(document("save-settings", preprocessRequest(prettyPrint()),
                requestFields(
                    fieldWithPath("controllerAlias")
                        .description("A name to identify the controller")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("clientId")
                        .description("The unique id of the device")
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
                        .type(JsonFieldType.STRING)
                    /*,fieldWithPath("channel")
                        .description("A List of all channels")
                        .attributes(optional)
                        .type(JsonFieldType.ARRAY)*/)));
    }

    @Test
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
    }

    @Test
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
    }

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
