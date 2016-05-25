package de.valoplus.web.rest

import com.fasterxml.jackson.databind.ObjectMapper
import de.valoplus.channel.*
import de.valoplus.converter.JsonConverter
import de.valoplus.dto.RequestWrapper
import de.valoplus.service.ControllerMock
import de.valoplus.web.rest.dto.ChannelRequestDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Created by tom on 23.02.16.
 */
@RestController
@RequestMapping("/api")
class ChannelRessouce {
    var ledController: ControllerMock

    @Autowired
    constructor(ledController: ControllerMock) {
        this.ledController = ledController
    }

    @RequestMapping(value = "/channel", method = arrayOf(RequestMethod.GET))
    fun getAllChannel(@RequestParam clientId: String): ResponseEntity<List<Channel>>  {
        return ResponseEntity.ok(ledController.channel)
    }

    @RequestMapping(value = "/channel", method = arrayOf(RequestMethod.POST))
    fun postChannel(@RequestBody request: ChannelRequestDTO): ResponseEntity<Void> {

        ledController.channel.forEach {
            if (it.name?.equals(request.name)!!) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build()
            }
        }

        ledController.channel.add(JsonConverter.parseChannelType(request))

        return ResponseEntity.ok().build()
    }

    @RequestMapping(value = "/channel", method = arrayOf(RequestMethod.DELETE))
    fun deleteChannel(@RequestParam channelName: String, @RequestParam clientId: String): ResponseEntity<Void> {
        if (!ledController.knownDevices.contains(clientId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val channels = ledController.channel
        for(i in channels.indices) {
            if (channels[i].name?.equals(channelName)!!) {
                channels.removeAt(i)
                return ResponseEntity.ok().build()
            }
        }

        return ResponseEntity.notFound().build()
    }

    /**
     * Updates the channel.
     */
    @RequestMapping(value = "/channel", method = arrayOf(RequestMethod.PUT))
    fun updateChannel(@RequestBody request: RequestWrapper<ChannelRequestDTO>): ResponseEntity<Void> {
        if (!request.isAutorized()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val channels = ledController.channel
        for(i in channels.indices) {
            if (channels.get(i).name?.equals(request.content?.name)!!) {
                channels.removeAt(i)
                channels.add(JsonConverter.parseChannelType(request.content))
                return ResponseEntity.ok().build()
            }
        }

        return ResponseEntity.notFound().build()
    }

    fun RequestWrapper<ChannelRequestDTO>.isAutorized(): Boolean {
        return ledController.knownDevices.contains(this.clientId)
    }

    fun JsonConverter.parseChannelType(old : ChannelRequestDTO?) : Channel {
        val channel = de.valoplus.channel.Channel()
        channel.name = old?.name
        channel.type = old?.type

        val type: ChannelType
        val mapper = ObjectMapper()
        when (de.valoplus.channel.ChannelTypes.valueOf(old?.type!!)) {
            de.valoplus.channel.ChannelTypes.WS2812 -> type = mapper.treeToValue(old?.typedef, ChannelTypeWS2812::class.java)
            de.valoplus.channel.ChannelTypes.LED_STRIP_RGB -> type = mapper.treeToValue(old?.typedef, ChannelTypeRGB::class.java)
            de.valoplus.channel.ChannelTypes.LED_STRIP -> type = mapper.treeToValue(old?.typedef, ChannelTypeSingleColor::class.java)
            else -> throw RuntimeException()
        }
        channel.typedef = type
        return channel
    }

    /*
    Alle
    Falsche clientId
    -> UNAUTHORIZED


    @RequestMapping(value = "/channel", method = RequestMethod.DELETE)
    ResponseEntity deleteChannel(@RequestParam String channelName, @RequestParam String clientId) {
        // Channel mit dem Namen löschen
        // -> 200
        // Channel nicht gefunden
        // -> 404

    }

    @RequestMapping(value = "/channel", method = RequestMethod.POST)
    ResponseEntity updateChannel(@RequestBody Channel channel) {
        // Channel mit dem Namen ersetzen
        // -> 201
        // Channel nicht gefunden
        // -> 404
    }

    @RequestMapping(value = "/channel", method = RequestMethod.PUT)
    ResponseEntity putChannel(@RequestBody Channel channel) {
        // Channel mit dem Namen speichern
        // -> 200
        // Channel existiert bereits mit dem Namen / Pin
        // -> 409
    }
     */
}
