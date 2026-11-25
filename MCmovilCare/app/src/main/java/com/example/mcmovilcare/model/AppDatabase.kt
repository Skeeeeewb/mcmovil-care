package com.example.mcmovilcare.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        SolicitudEntity::class,
        DecisionCotizacionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun solicitudDao(): SolicitudDao

    abstract fun decisionCotizacionDao(): DecisionCotizacionDao

    companion object {

        val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE solicitudes ADD COLUMN fotoRuta TEXT")
                db.execSQL("ALTER TABLE decisiones_cotizacion ADD COLUMN evidenciaRuta TEXT")
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mcmovil_care.db"
                ).addMigrations(MIGRACION_1_2).build().also { INSTANCE = it }
            }
        }
    }
}
