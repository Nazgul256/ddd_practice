package com.stringconcat.ddd.kitchen.usecase.order

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId

class CookOrderUseCase(
    private val kitchenOrderExtractor: KitchenOrderExtractor,
    private val kitchenOrderPersister: KitchenOrderPersister
) : CookOrder {

    override fun execute(orderId: KitchenOrderId): Either<CookOrderUseCaseError, Unit> {
        return kitchenOrderExtractor.getById(orderId)
            .rightIfNotNull { CookOrderUseCaseError.OrderNotFound }
            .map { order ->
                order.cook()
                kitchenOrderPersister.save(order)
            }
    }
}