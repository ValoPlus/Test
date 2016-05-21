package de.valoplus.controller

import de.valoplus.color.State

/**
 * A status descriptor for every [Channel] of an [Controller]
 * - channel -> the unique channel name to match with an channel
 * - active -> is the channel on or off?
 * - state -> the typed state of the channel
 */
class Status(
        val channel: String,
        var active: Boolean = false,
        val type: String,
        val state: State
) {}