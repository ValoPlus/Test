package de.valoplus.service

import de.valoplus.controller.Controller
import de.valoplus.wlan.Wlan
import org.springframework.stereotype.Service
import java.util.*

/**
 * A mock of the LED-Controller
 */
@Service
class ControllerMock : Controller() {
    var name: String = "mock"
    val key: String = "123456789abc"

    init {
        wlan = Wlan("", "", Wlan.WLANSecurity.NONE)
    }


    val knownDevices: MutableList<String> = ArrayList()

    /**
     * Resets the controller for better testing.
     */
    fun reset() {
        wlan = Wlan("", "", Wlan.WLANSecurity.NONE)
        name = "mock"
        channel.clear()
        knownDevices.clear()
    }
}
