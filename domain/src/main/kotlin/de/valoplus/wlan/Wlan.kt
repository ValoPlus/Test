package de.valoplus.wlan

/**
 * Created by tom on 12.02.16.
 */
class Wlan {
    var ssid: String? = null
    var pass: String? = null
    var wlanSecurity: WLANSecurity? = null

    constructor(ssid: String, pass: String, wlanSecurity: WLANSecurity) {
        this.ssid = ssid
        this.pass = pass
        this.wlanSecurity = wlanSecurity
    }

    constructor() {
    }

    enum class WLANSecurity {
        WPA, WPA2, NONE
    }
}
