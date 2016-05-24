package de.valoplus.web.rest

import de.valoplus.channel.*
import de.valoplus.dto.ControllerRequestDTO
import de.valoplus.dto.InitRequestDTO
import de.valoplus.dto.InitResponseDTO
import de.valoplus.service.ControllerMock
import de.valoplus.web.rest.errors.ErrorDTO
import de.valoplus.wlan.Wlan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Created by tom on 05.02.16.
 */
@RestController
@RequestMapping("/api")
class LedResource {
    var ledController: ControllerMock

    @Autowired
    constructor(ledController: ControllerMock) {
        this.ledController = ledController

        ledController.knownDevices.add("7882ABD9-B905-4ABB-BC90-4E71DE8CC9E4")

        val channel1 = Channel("Channel1", ChannelTypes.WS2812.name, ChannelTypeWS2812(1, 25))
        val channel2 = Channel("Channel2", ChannelTypes.LED_STRIP_RGB.name, ChannelTypeRGB(2, 4, 3))
        val channel3 = Channel("Channel3", ChannelTypes.LED_STRIP.name, ChannelTypeSingleColor(5))
        val channel4 = Channel("Channel4", ChannelTypes.LED_STRIP.name, ChannelTypeSingleColor(7))

        ledController.channel.add(channel1)
        ledController.channel.add(channel2)
        ledController.channel.add(channel3)
        ledController.channel.add(channel4)

    }

    init {

    }

    @ExceptionHandler(Exception::class)
    fun handleError(exception: Exception) {
        exception.printStackTrace();
    }

    @RequestMapping(value = "/init", method = arrayOf(RequestMethod.POST))
    fun getModel(@RequestBody data: InitRequestDTO): ResponseEntity<Any> {
        if (data.key == null || !data.key.equals(ledController.key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body<Any>(ErrorDTO("wrong key"))
        }
        ledController.knownDevices.add(data.clientId!!)

        return ResponseEntity.ok(InitResponseDTO("ESP8266", ledController.availableChannel, ledController.name, true))
    }

    @RequestMapping(value = "/status", method = arrayOf(RequestMethod.GET))
    fun getStatus(): ResponseEntity<Any> {
        //            if (!controller.isConfigured()) {
        //                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).headers(HeaderUtil.createFailureAlert("led-ressource", "notConfigured", "The controller is not yet configured")).body("The controller is not yet configured")
        //            }
        return ResponseEntity.ok<Any>(null)
    }

    @RequestMapping(value = "/settings", method = arrayOf(RequestMethod.GET))
    fun getSettings(@RequestParam clientId: String): ResponseEntity<Any> {
        println("...")
        if (!ledController.knownDevices.contains(clientId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body<Any>(ErrorDTO("device not registered"))
        }
        return ResponseEntity.ok<Any>(
            ControllerRequestDTO(ledController.wlan!!, ledController.name, ""))
    }

    @RequestMapping(value = "/settings/wlan", method = arrayOf(RequestMethod.POST))
    fun setSettings(@RequestBody wlan: Wlan, @RequestHeader("Authorization") clientId: String): ResponseEntity<Any> {
        if (!ledController.knownDevices.contains(clientId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body<Any>(ErrorDTO("device not registered"))
        }
        ledController.wlan = wlan;
        return ResponseEntity.ok<Any>("Wlan settings saved. Controller will try to reconnect.")
    }

    @ExceptionHandler(UninitializedPropertyAccessException::class)
    fun handleLateinitError(ex: UninitializedPropertyAccessException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body<Any>(ErrorDTO("missing required argument"));
    }
}
