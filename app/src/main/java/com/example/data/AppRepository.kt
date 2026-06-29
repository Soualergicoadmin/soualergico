package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val allergyDao: AllergyDao,
    private val medicationDao: MedicationDao,
    private val userProfileDao: UserProfileDao
) {
    val allAllergies: Flow<List<Allergy>> = allergyDao.getAllAllergies()
    val allMedications: Flow<List<Medication>> = medicationDao.getAllMedications()
    val userProfile: Flow<UserProfile?> = userProfileDao.getProfile()

    suspend fun insertAllergy(allergy: Allergy) {
        allergyDao.insertAllergy(allergy)
    }

    suspend fun deleteAllergy(id: Int) {
        allergyDao.deleteById(id)
    }

    suspend fun insertMedication(medication: Medication) {
        medicationDao.insertMedication(medication)
    }

    suspend fun deleteMedication(id: Int) {
        medicationDao.deleteById(id)
    }

    suspend fun updateProfile(profile: UserProfile) {
        userProfileDao.insertProfile(profile)
    }
}
