package de.valoplus.dto

import com.google.gson.JsonObject

/**
 * Created by tom on 12.02.16.
 */
class ChannelResponseDTO {
    val name: String
    val type: String
    val typedef: JsonObject

    constructor(name: String, type: String, typedef: JsonObject) {
        this.name = name
        this.type = type
        this.typedef = typedef
    }
}
