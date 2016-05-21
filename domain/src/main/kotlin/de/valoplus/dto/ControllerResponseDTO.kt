package de.valoplus.dto

import de.valoplus.wlan.Wlan
import java.util.*

/**
 * Created by tom on 12.02.16.
 */
class ControllerResponseDTO() {
    var channel: List<ChannelResponseDTO>
    var wlan: Wlan? = null
    var controllerAlias: String? = null

    constructor(channel: List<ChannelResponseDTO>, wlan: Wlan, controllerAlias: String) : this() {
        this.wlan = wlan
        this.controllerAlias = controllerAlias
        this.channel = channel
    }

    init {
        this.channel = ArrayList<ChannelResponseDTO>()
    }
}
