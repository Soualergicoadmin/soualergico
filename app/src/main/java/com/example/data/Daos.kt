package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AllergyDao {
    @Query("SELECT * FROM allergies ORDER BY name ASC")
    fun getAllAllergies(): Flow<List<Allergy>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllergy(allergy: Allergy)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllergies(allergies: List<Allergy>)

    @Query("DELETE FROM allergies WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM allergies")
    suspend fun clearAll()
}

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications ORDER BY name ASC")
    fun getAllMedications(): Flow<List<Medication>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: Medication)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedications(medications: List<Medication>)

    @Query("DELETE FROM medications WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM medications")
    suspend fun clearAll()
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)
}
