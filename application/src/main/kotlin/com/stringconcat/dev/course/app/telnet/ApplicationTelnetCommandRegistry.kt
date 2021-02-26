package com.stringconcat.dev.course.app.telnet

import com.khubla.telnet.shell.command.AbstractTelnetCommandRegistry
import com.khubla.telnet.shell.command.TelnetCommand

class ApplicationTelnetCommandRegistry(commands: List<TelnetCommand>) : AbstractTelnetCommandRegistry() {
    init {
        commands.forEach { addCommand(it) }
    }
}