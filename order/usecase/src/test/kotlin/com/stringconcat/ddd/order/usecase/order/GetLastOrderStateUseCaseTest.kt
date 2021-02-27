package com.stringconcat.ddd.order.usecase.order

import com.stringconcat.ddd.order.usecase.TestCustomerOrderExtractor
import com.stringconcat.ddd.order.usecase.customerId
import com.stringconcat.ddd.order.usecase.order
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import org.junit.jupiter.api.Test

class GetLastOrderStateUseCaseTest {

    @Test
    fun `status successfully received`() {
        val order = order()
        val orderExtractor = TestCustomerOrderExtractor().apply {
            this[order.id] = order
        }
        val useCase = GetLastOrderStateUseCase(orderExtractor)
        val result = useCase.execute(order.customerId)
        result shouldBeRight order.state
    }

    @Test
    fun `order not found`() {
        val orderExtractor = TestCustomerOrderExtractor()
        val useCase = GetLastOrderStateUseCase(orderExtractor)
        val result = useCase.execute(customerId())
        result shouldBeLeft GetLastOrderStateUseCaseError.OrderNotFound
    }
}