package com.naveenapps.expensemanager.core.domain.usecase.category

import com.naveenapps.expensemanager.core.model.Category
import com.naveenapps.expensemanager.core.model.Resource
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val repository: com.naveenapps.expensemanager.core.data.repository.CategoryRepository,
    private val checkCategoryValidationUseCase: CheckCategoryValidationUseCase
) {

    suspend operator fun invoke(category: Category): Resource<Boolean> {
        return when (val validationResult = checkCategoryValidationUseCase(category)) {
            is Resource.Error -> {
                validationResult
            }

            is Resource.Success -> {
                repository.deleteCategory(category)
            }
        }
    }
}