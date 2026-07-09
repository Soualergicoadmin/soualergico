package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Single profile
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val bloodType: String = "",
    val emergencyContactName: String = "",
    val emergencyContactPhone: String = "",
    val emergencyContactRelation: String = "",
    val isAlertButtonEnabled: Boolean = true,
    val preferredLanguage: String = "Português (Brasil)"
)

@Entity(tableName = "allergies")
data class Allergy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val allergen: String,
    val severity: String, // "Mild", "Moderate", "Severe"
    val symptoms: String,
    val rescueMedication: String = "",
    val prescriptionFilePath: String? = null
)

@Entity(tableName = "contacts")
data class EmergencyContact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val relation: String
)

@Entity(tableName = "incidents")
data class IncidentLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val allergen: String,
    val symptoms: String,
    val treatmentUsed: String,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)
