package com.stringconcat.ddd.order.usecase.providers

import com.stringconcat.ddd.order.usecase.TestMealExtractor
import com.stringconcat.ddd.order.usecase.meal
import com.stringconcat.ddd.order.usecase.mealId
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

class MealPriceProviderImplTest {

    @Test
    fun `price has been provided`() {
        val meal = meal()

        val extractor = TestMealExtractor().apply {
            this[meal.id] = meal
        }

        val provider = MealPriceProviderImpl(extractor)
        val result = provider.price(meal.id)
        result shouldBe meal.price
    }

    @Test
    fun `meal not found`() {
        val extractor = TestMealExtractor()
        val provider = MealPriceProviderImpl(extractor)
        shouldThrow<IllegalStateException> {
            provider.price(mealId())
        }
    }
}
