package de.valoplus.web.rest

import com.fasterxml.jackson.databind.ObjectMapper
import de.valoplus.channel.*
import de.valoplus.converter.JsonConverter
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
@RequestMapping("/api/channel")
class ChannelRessouce {
    var ledController: ControllerMock

    @Autowired
    constructor(ledController: ControllerMock) {
        this.ledController = ledController
    }

    @GetMapping
    fun getAllChannel(): ResponseEntity<List<Channel>>  {
        return ResponseEntity.ok(ledController.channel)
    }

    @PostMapping
    fun postChannel(@RequestBody request: ChannelRequestDTO): ResponseEntity<Void> {

        ledController.channel.forEach {
            if (it.name?.equals(request.name)!!) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build()
            }
        }

        ledController.channel.add(JsonConverter.parseChannelType(request))

        return ResponseEntity.ok().build()
    }

    @DeleteMapping
    fun deleteChannel(@RequestParam channelName: String): ResponseEntity<Void> {
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
    @PutMapping
    fun updateChannel(@RequestBody request: ChannelRequestDTO): ResponseEntity<Void> {
        val channels = ledController.channel
        for(i in channels.indices) {
            if (channels.get(i).name?.equals(request.name)!!) {
                channels.removeAt(i)
                channels.add(JsonConverter.parseChannelType(request))
                return ResponseEntity.ok().build()
            }
        }

        return ResponseEntity.notFound().build()
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
}
