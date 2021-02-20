package com.stringconcat.ddd.order.usecase.order

import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

interface CustomerOrderExtractor {
    fun getById(orderId: CustomerOrderId): CustomerOrder?

    fun getLastOrder(customerId: CustomerId): CustomerOrder?
}