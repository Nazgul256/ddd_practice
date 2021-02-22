package com.stringconcat.ddd.order.persistence.order

import com.stringconcat.ddd.order.domain.order.CustomerOrderCompletedDomainEvent
import com.stringconcat.ddd.order.persistence.TestEventPublisher
import com.stringconcat.ddd.order.persistence.customerId
import com.stringconcat.ddd.order.persistence.order
import com.stringconcat.ddd.order.persistence.orderWithEvents
import com.stringconcat.ddd.order.persistence.orderId
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Test

internal class InMemoryCustomerOrderRepositoryTest {
    private val eventPublisher = TestEventPublisher()

    @Test
    fun `saving order - order doesn't exist`() {
        val repository = InMemoryCustomerOrderRepository(eventPublisher)
        val order = orderWithEvents()

        repository.save(order)

        val storedOrder = repository.storage[order.id]
        storedOrder shouldBeSameInstanceAs order
        eventPublisher.storage.shouldHaveSize(1)

        val event = eventPublisher.storage.first()
        event.shouldBeInstanceOf<CustomerOrderCompletedDomainEvent>()
        event.orderId shouldBe order.id
    }

    @Test
    fun `saving order - order exists`() {

        val id = orderId()
        val existingOrder = order(id = id)

        val repository = InMemoryCustomerOrderRepository(eventPublisher)
        repository.storage[existingOrder.id] = existingOrder

        val updatedOrder = orderWithEvents(id)
        repository.save(updatedOrder)

        val event = eventPublisher.storage.first()
        event.shouldBeInstanceOf<CustomerOrderCompletedDomainEvent>()
        event.orderId shouldBe updatedOrder.id
    }

    @Test
    fun `get by id - order exists`() {
        val existingOrder = order()

        val repository = InMemoryCustomerOrderRepository(eventPublisher)
        repository.storage[existingOrder.id] = existingOrder

        val order = repository.getById(existingOrder.id)
        order shouldBeSameInstanceAs existingOrder
    }

    @Test
    fun `get by id - order doesn't exist`() {
        val repository = InMemoryCustomerOrderRepository(eventPublisher)
        val order = repository.getById(orderId())
        order.shouldBeNull()
    }

    @Test
    fun `get last - repository is empty`() {
        val repository = InMemoryCustomerOrderRepository(eventPublisher)
        val order = repository.getLastOrder(customerId())
        order.shouldBeNull()
    }

    @Test
    fun `get last - success`() {

        val customerId = customerId()
        val firstOrder = order(customerId = customerId)
        val lastOrder = order(customerId = customerId)

        val repository = InMemoryCustomerOrderRepository(eventPublisher)
        repository.save(firstOrder)
        repository.save(lastOrder)

        val order = repository.getLastOrder(customerId)
        order shouldBeSameInstanceAs lastOrder
    }
}