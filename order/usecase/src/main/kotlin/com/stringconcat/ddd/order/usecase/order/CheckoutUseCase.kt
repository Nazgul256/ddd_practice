package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.common.types.common.CreateAddressError
import com.stringconcat.ddd.order.domain.order.CheckoutError
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderIdGenerator
import com.stringconcat.ddd.order.domain.providers.MealPriceProvider
import com.stringconcat.ddd.order.domain.rules.CustomerHasActiveOrderRule
import com.stringconcat.ddd.order.usecase.cart.CartExtractor

class CheckoutUseCase(
    private val idGenerator: CustomerOrderIdGenerator,
    private val cartExtractor: CartExtractor,
    private val activeOrderRule: CustomerHasActiveOrderRule,
    private val priceProvider: MealPriceProvider,
    private val paymentUrlProvider: PaymentUrlProvider,
    private val customerOrderPersister: CustomerOrderPersister
) : Checkout {

    override fun execute(request: CheckoutRequest): Either<CheckoutUseCaseError, PaymentInfo> =

        cartExtractor
            .getCart(forCustomer = request.customerId)
            .rightIfNotNull { CheckoutUseCaseError.CartNotFound }
        .flatMap { cart ->
            CustomerOrder.checkout(
                idGenerator = idGenerator,
                activeOrder = activeOrderRule,
                priceProvider = priceProvider,
                address = request.deliveryTo,
                cart = cart
            ).mapLeft { err -> err.toError() }
        }.map { order ->
            customerOrderPersister.save(order)
            PaymentInfo(
                orderId = order.id,
                price = order.totalPrice(),
                paymentURL = paymentUrlProvider.provideUrl(order.id, order.totalPrice())
            )
        }
}

fun CreateAddressError.toError(): CheckoutUseCaseError {
    return when (this) {
        CreateAddressError.EmptyString -> CheckoutUseCaseError.InvalidAddress("Empty street")
        CreateAddressError.NonPositiveBuilding -> CheckoutUseCaseError.InvalidAddress("Negative value")
    }
}

fun CheckoutError.toError(): CheckoutUseCaseError {
    return when (this) {
        CheckoutError.AlreadyHasActiveOrder -> CheckoutUseCaseError.AlreadyHasActiveOrder
        CheckoutError.EmptyCart -> CheckoutUseCaseError.EmptyCart
    }
}