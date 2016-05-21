package converter

import com.google.gson.Gson
import de.valoplus.channel.Channel
import de.valoplus.channel.ChannelTypeWS2812
import de.valoplus.channel.ChannelTypes
import de.valoplus.converter.JsonConverter
import de.valoplus.dto.ChannelResponseDTO
import org.junit.Assert

/**
 * Created by tom on 21.02.16.
 */
class JsonConverterTest {

    @org.junit.Test
    fun parseChannelType() {
        val channel: Channel = Channel("Channel 1", ChannelTypes.WS2812.toString(), ChannelTypeWS2812(0, 50))

        val gson: Gson = Gson()
        val json: String = gson.toJson(channel)

        println(json)

        val obj: ChannelResponseDTO = gson.fromJson(json, ChannelResponseDTO::class.java)

        val result: Channel = JsonConverter.parseChannelType(obj)

        Assert.assertEquals(channel, result)
    }
}