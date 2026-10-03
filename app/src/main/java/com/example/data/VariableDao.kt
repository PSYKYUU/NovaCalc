package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VariableDao {
    @Query("SELECT * FROM variables ORDER BY isConstant DESC, name ASC")
    fun getAllVariables(): Flow<List<VariableEntity>>

    @Query("SELECT * FROM variables WHERE name = :name LIMIT 1")
    suspend fun getVariableByName(name: String): VariableEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(variable: VariableEntity)

    @Query("DELETE FROM variables WHERE name = :name AND isConstant = 0")
    suspend fun deleteVariable(name: String)

    @Query("DELETE FROM variables WHERE isConstant = 0")
    suspend fun clearUserVariables()
}
