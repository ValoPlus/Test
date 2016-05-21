package de.valoplus.converter

import com.google.gson.Gson
import de.valoplus.channel.Channel
import de.valoplus.channel.ChannelType
import de.valoplus.channel.ChannelTypes
import de.valoplus.color.State
import de.valoplus.controller.Status
import de.valoplus.dto.ChannelResponseDTO
import de.valoplus.dto.StatusResponseDTO

/**
 * A converter to convert Json into Java with GSON.
 * Has implementations for polymorphic domain structures.
 *
 * @author Tom Schmidt
 */
object JsonConverter {
    val gson: Gson = Gson();

    fun parseChannelType(channel: ChannelResponseDTO): Channel {
        val type: ChannelTypes = ChannelTypes.valueOf(channel.type)

        var typedef: ChannelType = gson.fromJson(channel.typedef, type.clz.java)

        return Channel(channel.name, channel.type, typedef)
    }

    fun parseStateType(state: StatusResponseDTO): Status {
        val type: ChannelTypes = ChannelTypes.valueOf(state.type)

        val typedef: State = gson.fromJson(state.state, type.state.java)

        return Status(state.channel, state.active, state.type, typedef)
    }
}
