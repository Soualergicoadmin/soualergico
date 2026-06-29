package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allergies")
data class Allergy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "alimento" or "medicamento"
    val name: String,
    val severity: String, // e.g. "Grave", "Intolerância", "Medicamentosa", "Respiratória"
    val symptoms: String,
    val instructions: String,
    val emergencyMedicationName: String,
    val emergencyMedicationDose: String,
    val emergencyMedicationUrgent: Boolean
)

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dosage: String,
    val isContinuous: Boolean,
    val frequency: String, // e.g. "Uma vez ao dia", "A cada 8 horas", "A cada 12 horas", "Personalizado"
    val nextDoseTime: String, // e.g. "08:00"
    val startedDate: String, // e.g. "12 Out"
    val durationDays: Int, // e.g. 7
    val currentDay: Int, // e.g. 3
    val notificationEnabled: Boolean
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "João Silva",
    val email: String = "joao.silva@email.com",
    val phone: String = "(11) 98765-4321",
    val address: String = "Rua das Flores, 123, Bloco B - Apto 45",
    val state: String = "SP",
    val city: String = "São Paulo",
    val contact1Name: String = "Maria Silva",
    val contact1Phone: String = "(11) 99988-7766",
    val contact2Name: String = "Carlos Oliveira",
    val contact2Phone: String = "(11) 91122-3344"
)
