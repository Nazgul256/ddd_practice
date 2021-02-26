package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

class ConfirmOrderUseCase(
    private val customerOrderExtractor: CustomerOrderExtractor,
    private val customerOrderPersister: CustomerOrderPersister
) : ConfirmOrder {

    override fun execute(orderId: Long): Either<ConfirmOrderUseCaseError, Unit> {
        return customerOrderExtractor.getById(CustomerOrderId(orderId))
            .rightIfNotNull { ConfirmOrderUseCaseError.OrderNotFound }
            .flatMap { order ->
                order.confirm().map {
                    customerOrderPersister.save(order)
                }.mapLeft { ConfirmOrderUseCaseError.InvalidOrderState }
            }
    }
}