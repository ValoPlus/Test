package de.valoplus.web.rest

import de.valoplus.channel.ChannelTypes
import de.valoplus.color.ColorStateWS2812
import de.valoplus.color.DummyState
import de.valoplus.controller.Status
import de.valoplus.converter.JsonConverter
import de.valoplus.dto.RequestWrapper
import de.valoplus.dto.StatusResponseDTO
import de.valoplus.service.ControllerMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Created by tom on 12.04.16.
 */
@RestController
@RequestMapping("/api")
class StatusResource {

    var ledController: ControllerMock

    @Autowired
    constructor(ledController: ControllerMock) {
        this.ledController = ledController

        val state1 = Status("Channel1", true, ChannelTypes.WS2812.name, ColorStateWS2812(80, 0, 20, 160))
        val state2 = Status("Channel2", true, ChannelTypes.DUMMY.name, DummyState())
        val state3 = Status("Channel3", true, ChannelTypes.DUMMY.name, DummyState())
        val state4 = Status("Channel4", true, ChannelTypes.DUMMY.name, DummyState())


        ledController.status.add(state1)
        ledController.status.add(state2)
        ledController.status.add(state3)
        ledController.status.add(state4)
    }

    @RequestMapping(value = "/states", method = arrayOf(RequestMethod.GET))
    fun getStatusList(@RequestParam clientId: String): ResponseEntity<*> {
        if (!ledController.knownDevices.contains(clientId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        return ResponseEntity.ok(ledController.status)
    }

    @RequestMapping(value = "/state", method = arrayOf(RequestMethod.GET))
    fun getStatusByName(@RequestParam(required = false) clientId: String, @RequestParam(required = false) name: String): ResponseEntity<*> {
        if (!ledController.knownDevices.contains(clientId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        val status = ledController.status.first { it.channel == name }
        if (status == null) {
            ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(status)
    }

    @RequestMapping(value = "/state", method = arrayOf(RequestMethod.POST))
    fun updateStatus(@RequestBody status: RequestWrapper<StatusResponseDTO>): ResponseEntity<*> {
        if (!ledController.knownDevices.contains(status.clientId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        // Delete existing status
        for ((index, actual) in ledController.status.withIndex()) {
            if (actual.channel == status.content?.channel) {
                ledController.status.removeAt(index)
                break
            }
        }
        ledController.status.add(JsonConverter.parseStateType(status.content!!))
        return ResponseEntity.ok().build();
    }
}
