package com.example.data

import kotlinx.coroutines.flow.Flow

class AlexCalcRepository(
    private val historyDao: HistoryDao,
    private val variableDao: VariableDao
) {

    val allHistory: Flow<List<CalculationHistoryEntity>> = historyDao.getAllHistory()
    val favoriteHistory: Flow<List<CalculationHistoryEntity>> = historyDao.getFavoriteHistory()
    val allVariables: Flow<List<VariableEntity>> = variableDao.getAllVariables()

    fun searchHistory(query: String): Flow<List<CalculationHistoryEntity>> =
        historyDao.searchHistory(query)

    suspend fun saveHistory(
        expression: String,
        resultFormatted: String,
        realPart: Double,
        imagPart: Double,
        isComplex: Boolean,
        angleMode: String,
        note: String = ""
    ): Long {
        val entry = CalculationHistoryEntity(
            expression = expression,
            resultFormatted = resultFormatted,
            realPart = realPart,
            imagPart = imagPart,
            isComplex = isComplex,
            angleMode = angleMode,
            note = note
        )
        return historyDao.insertHistory(entry)
    }

    suspend fun toggleFavorite(id: Long) {
        historyDao.toggleFavorite(id)
    }

    suspend fun deleteHistory(id: Long) {
        historyDao.deleteHistoryById(id)
    }

    suspend fun clearHistory() {
        historyDao.clearAllHistory()
    }

    suspend fun saveVariable(
        name: String,
        valueFormatted: String,
        realPart: Double,
        imagPart: Double,
        isComplex: Boolean,
        isConstant: Boolean = false,
        description: String = ""
    ) {
        val entity = VariableEntity(
            name = name,
            valueFormatted = valueFormatted,
            realPart = realPart,
            imagPart = imagPart,
            isComplex = isComplex,
            isConstant = isConstant,
            description = description,
            updatedAt = System.currentTimeMillis()
        )
        variableDao.insertOrUpdate(entity)
    }

    suspend fun deleteVariable(name: String) {
        variableDao.deleteVariable(name)
    }

    suspend fun clearUserVariables() {
        variableDao.clearUserVariables()
    }
}
