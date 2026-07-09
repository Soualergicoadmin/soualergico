package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {

    val userProfile: Flow<UserProfile?> = appDao.getUserProfile()
    val allAllergies: Flow<List<Allergy>> = appDao.getAllAllergies()
    val allContacts: Flow<List<EmergencyContact>> = appDao.getAllContacts()
    val allIncidents: Flow<List<IncidentLog>> = appDao.getAllIncidents()

    suspend fun saveUserProfile(profile: UserProfile) {
        appDao.insertUserProfile(profile)
    }

    suspend fun insertAllergy(allergy: Allergy) {
        appDao.insertAllergy(allergy)
    }

    suspend fun deleteAllergy(allergy: Allergy) {
        appDao.deleteAllergy(allergy)
    }

    suspend fun insertContact(contact: EmergencyContact) {
        appDao.insertContact(contact)
    }

    suspend fun deleteContact(contact: EmergencyContact) {
        appDao.deleteContact(contact)
    }

    suspend fun insertIncident(incident: IncidentLog) {
        appDao.insertIncident(incident)
    }

    suspend fun deleteIncident(incident: IncidentLog) {
        appDao.deleteIncident(incident)
    }
}
