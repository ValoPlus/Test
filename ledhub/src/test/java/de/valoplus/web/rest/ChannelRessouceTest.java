package de.valoplus.web.rest;

/**
 * Created by tom on 23.02.16.
 */

import com.google.common.collect.Lists;
import de.valoplus.LedhubApp;
import de.valoplus.channel.Channel;
import de.valoplus.channel.ChannelTypeWS2812;
import de.valoplus.channel.ChannelTypes;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LedhubApp.class)
@WebAppConfiguration
public class ChannelRessouceTest {
    private MockMvc mockMvc;

    private final static String DEVICE_ID = "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4";

    @Autowired
    private WebApplicationContext webApplicationContext;
    private Attributes.Attribute optional = key("optional").value(true);

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");


    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).apply(documentationConfiguration(restDocumentation))
                                                                .build();
    }

    @Test
    public void testChannelController() throws Exception {
        postChannel();
        updateChannel();
        deleteChannel();
        getChannel();
    }

    public void getChannel() throws Exception {
        mockMvc.perform(
            get("/api/channel").header("Authorization", "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4"))
               .andExpect(status().isOk())
               .andDo(document("channel-get", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                   responseFields(
                       fieldWithPath("[].name")
                           .description("The unique name of the Channel")
                           .type(JsonFieldType.STRING),
                       fieldWithPath("[].type")
                           .description("A type to interpret the typedef")
                           .type(JsonFieldType.STRING),
                       fieldWithPath("[].typedef")
                           .description("The specialized structure of this Type")
                           .type(JsonFieldType.OBJECT)))
               );
    }

    public void postChannel() throws Exception {
        Channel channel = new Channel("42", ChannelTypes.WS2812.name(), new ChannelTypeWS2812(7, 42));

        mockMvc.perform(
            post("/api/channel").header("Authorization", "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4").contentType(TestUtil.APPLICATION_JSON_UTF8)
                                .content(TestUtil.convertObjectToJsonBytes(channel)))
               .andExpect(status().isOk())
               .andDo(document("channel-post", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));

        // mockMvc.perform(post("/api/init").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(content))).andExpect(status().isUnauthorized).andDo(document("init-401", preprocessRequest(prettyPrint())))
    }

    public void deleteChannel() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("channelName", Lists.newArrayList("42"));
        mockMvc.perform(
            delete("/api/channel").header("Authorization", "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4").params(params))
               .andExpect(status().isOk())
               .andDo(document("channel-delete", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));

    }

    public void updateChannel() throws Exception {
        Channel channel = new Channel("42", ChannelTypes.WS2812.name(), new ChannelTypeWS2812(7, 47));

        mockMvc.perform(
            put("/api/channel").header("Authorization", "7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4").contentType(TestUtil.APPLICATION_JSON_UTF8)
                               .content(TestUtil.convertObjectToJsonBytes(channel)))
               .andExpect(status().isOk())
               .andDo(document("channel-put", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }
}
