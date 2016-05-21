package de.valoplus.dto

import de.valoplus.wlan.Wlan

/**
 * Created by tom on 12.02.16.
 */
class ControllerRequestDTO(
        // var channel: List<Channel> = ArrayList<Channel>(),
        var wlan: Wlan,
        var controllerAlias: String,
        var clientId: String
) {}
