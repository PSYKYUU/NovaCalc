package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [CalculationHistoryEntity::class, VariableEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AlexCalcDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    abstract fun variableDao(): VariableDao

    companion object {
        @Volatile
        private var INSTANCE: AlexCalcDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AlexCalcDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlexCalcDatabase::class.java,
                    "alexcalc_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialVariables(database.variableDao())
                    }
                }
            }

            private suspend fun populateInitialVariables(dao: VariableDao) {
                val initialConstants = listOf(
                    VariableEntity(
                        name = "π",
                        valueFormatted = "3.14159265",
                        realPart = Math.PI,
                        imagPart = 0.0,
                        isComplex = false,
                        isConstant = true,
                        description = "Archimedes' constant pi"
                    ),
                    VariableEntity(
                        name = "e",
                        valueFormatted = "2.71828183",
                        realPart = Math.E,
                        imagPart = 0.0,
                        isComplex = false,
                        isConstant = true,
                        description = "Euler's number e"
                    ),
                    VariableEntity(
                        name = "i",
                        valueFormatted = "i",
                        realPart = 0.0,
                        imagPart = 1.0,
                        isComplex = true,
                        isConstant = true,
                        description = "Imaginary unit (√-1)"
                    ),
                    VariableEntity(
                        name = "φ",
                        valueFormatted = "1.61803399",
                        realPart = (1.0 + kotlin.math.sqrt(5.0)) / 2.0,
                        imagPart = 0.0,
                        isComplex = false,
                        isConstant = true,
                        description = "Golden ratio phi"
                    ),
                    VariableEntity(
                        name = "ans",
                        valueFormatted = "0",
                        realPart = 0.0,
                        imagPart = 0.0,
                        isComplex = false,
                        isConstant = false,
                        description = "Last calculated answer"
                    )
                )
                for (v in initialConstants) {
                    dao.insertOrUpdate(v)
                }
            }
        }
    }
}
