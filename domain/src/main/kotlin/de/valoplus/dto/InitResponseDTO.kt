package de.valoplus.dto

/**
 * Created by tom on 28.03.16.
 */

class InitResponseDTO(var controllerType: String = "",
                      var availableChannel: Int = 0,
                      var controllerAlias: String = "",
                      val configured: Boolean = false) {
}