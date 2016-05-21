package de.valoplus.dto

/**
 * Created by tom on 28.03.16.
 */

class InitResponseDTO() {
    var controllerType: String? = null
    var availableChannel: Int? = null

    constructor(controllerType: String, availableChannel: Int) : this() {
        this.controllerType = controllerType
        this.availableChannel = availableChannel
    }
}