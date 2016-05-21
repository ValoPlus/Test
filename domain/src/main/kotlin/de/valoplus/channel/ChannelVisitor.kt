package de.valoplus.channel

/**
 * Created by tom on 30.03.16.
 */
interface ChannelVisitor<T> {
    fun visit(channel: ChannelTypeRGB): T
    fun visit(channel: ChannelTypeSingleColor): T
    fun visit(channel: ChannelTypeWS2812): T
}