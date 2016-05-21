package de.valoplus.channel

import de.valoplus.color.ColorStateWS2812
import de.valoplus.color.DummyState
import de.valoplus.color.State
import kotlin.reflect.KClass

/**
 * Created by tom on 20.02.16.
 */
enum class ChannelTypes(val rep: String, val clz: KClass<out ChannelType>, val state: KClass<out State>) {
    WS2812("WS2812", ChannelTypeWS2812::class, ColorStateWS2812::class),
    LED_STRIP_RGB("RGB Led Leiste", ChannelTypeRGB::class, ColorStateWS2812::class),
    LED_STRIP("Einfahrbige LED Leiste", ChannelTypeSingleColor::class, ColorStateWS2812::class),
    DUMMY("dummy", ChannelTypeWS2812::class, DummyState::class)
}