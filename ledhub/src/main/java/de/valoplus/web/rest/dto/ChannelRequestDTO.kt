package de.valoplus.web.rest.dto

import com.fasterxml.jackson.databind.JsonNode

/**
 * Created by tom on 28.02.16.
 */
class ChannelRequestDTO() {
    lateinit var name: String;
    lateinit var type: String;
    lateinit var typedef: JsonNode;
}
