package de.valoplus.dto

import com.google.gson.JsonObject

/**
 * Created by tom on 12.02.16.
 */
class StatusResponseDTO {
    val channel: String
    val active: Boolean
    val type: String
    val state: JsonObject

    constructor(channel: String, active: Boolean, type: String, state: JsonObject) {
        this.channel = channel
        this.active = active
        this.state = state
        this.type = type
    }

    constructor() : this("", false, "", JsonObject())
}
