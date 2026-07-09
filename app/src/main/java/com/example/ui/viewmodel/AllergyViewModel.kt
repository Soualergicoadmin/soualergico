package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AllergyViewModel(private val repository: AppRepository) : ViewModel() {

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val allergies: StateFlow<List<Allergy>> = repository.allAllergies
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val contacts: StateFlow<List<EmergencyContact>> = repository.allContacts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val incidents: StateFlow<List<IncidentLog>> = repository.allIncidents
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveProfile(
        name: String,
        phone: String,
        address: String,
        bloodType: String,
        emergencyContactName: String,
        emergencyContactPhone: String,
        emergencyContactRelation: String,
        isAlertButtonEnabled: Boolean,
        preferredLanguage: String
    ) {
        viewModelScope.launch {
            repository.saveUserProfile(
                UserProfile(
                    name = name,
                    phone = phone,
                    address = address,
                    bloodType = bloodType,
                    emergencyContactName = emergencyContactName,
                    emergencyContactPhone = emergencyContactPhone,
                    emergencyContactRelation = emergencyContactRelation,
                    isAlertButtonEnabled = isAlertButtonEnabled,
                    preferredLanguage = preferredLanguage
                )
            )
        }
    }

    fun addAllergy(allergen: String, severity: String, symptoms: String, rescueMedication: String, prescriptionFilePath: String? = null) {
        viewModelScope.launch {
            repository.insertAllergy(Allergy(allergen = allergen, severity = severity, symptoms = symptoms, rescueMedication = rescueMedication, prescriptionFilePath = prescriptionFilePath))
        }
    }

    fun removeAllergy(allergy: Allergy) {
        viewModelScope.launch {
            repository.deleteAllergy(allergy)
        }
    }

    fun addContact(name: String, phone: String, relation: String) {
        viewModelScope.launch {
            repository.insertContact(EmergencyContact(name = name, phone = phone, relation = relation))
        }
    }

    fun removeContact(contact: EmergencyContact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }

    fun addIncident(allergen: String, symptoms: String, treatmentUsed: String, notes: String) {
        viewModelScope.launch {
            repository.insertIncident(
                IncidentLog(
                    allergen = allergen,
                    symptoms = symptoms,
                    treatmentUsed = treatmentUsed,
                    notes = notes
                )
            )
        }
    }

    fun removeIncident(incident: IncidentLog) {
        viewModelScope.launch {
            repository.deleteIncident(incident)
        }
    }
}

class AllergyViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllergyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AllergyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
