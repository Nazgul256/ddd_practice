package com.stringconcat.ddd.order.usecase.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.error.BusinessError
import com.stringconcat.ddd.order.domain.menu.MealId

class RemoveMealFromMenuUseCase(
    private val mealExtractor: MealExtractor,
    private val mealPersister: MealPersister
) {

    fun removeMealFromMenu(id: Long): Either<MealNotFound, Unit> {
        val meal = mealExtractor.getById(MealId(id))
        return if (meal != null) {
            meal.removeMealFromMenu()
            mealPersister.save(meal).right()
        } else {
            MealNotFound.left()
        }
    }
}

object MealNotFound : BusinessError