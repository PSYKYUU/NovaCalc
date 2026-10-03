package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "variables")
data class VariableEntity(
    @PrimaryKey
    val name: String,
    val valueFormatted: String,
    val realPart: Double,
    val imagPart: Double,
    val isComplex: Boolean,
    val isConstant: Boolean = false,
    val description: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
