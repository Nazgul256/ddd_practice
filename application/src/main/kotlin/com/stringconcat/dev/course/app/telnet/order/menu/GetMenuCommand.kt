package com.stringconcat.dev.course.app.telnet.order.menu

import com.github.freva.asciitable.AsciiTable
import com.github.freva.asciitable.Column
import com.stringconcat.ddd.order.usecase.menu.GetMenu
import com.stringconcat.dev.course.app.telnet.ApplicationTelnetCommand
import java.util.UUID

class GetMenuCommand(private val useCase: GetMenu) : ApplicationTelnetCommand() {

    override fun execute(line: String, sessionParameters: Map<String, Any>, sessionId: UUID): String {
        val menu = useCase.execute()

        return AsciiTable.getTable(
            AsciiTable.FANCY_ASCII, menu, listOf(
                Column().with { meal -> meal.id.toString() },
                Column().header("Name").with { meal -> meal.name },
                Column().header("Description").with { meal -> meal.description },
                Column().header("Price").with { meal -> meal.price.toPlainString() },
            )
        )
    }

    override fun getNames() = arrayOf("menu")

    override fun getDescription() = "Show menu"
}