
package com.example.mcmovilcare

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DecisionCotizacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarDecision(
        decision: DecisionCotizacionEntity
    )

    @Query(
        "SELECT * FROM decisiones_cotizacion " +
                "WHERE atencionId = :atencionId LIMIT 1"
    )
    fun observarDecision(
        atencionId: Int
    ): Flow<DecisionCotizacionEntity?>
}

