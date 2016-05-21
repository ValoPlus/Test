package de.valoplus.web.rest.dto

import de.valoplus.wlan.Wlan


/**
 * Created by tom on 28.02.16.
 */
class SettingsRequestDTO() {
    // lateinit var channel: List<ChannelRequestDTO>
    lateinit var controllerAlias: String
    lateinit var clientId: String
    lateinit var wlan: Wlan
}
