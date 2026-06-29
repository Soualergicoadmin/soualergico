package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = AppRepository(
        database.allergyDao(),
        database.medicationDao(),
        database.userProfileDao()
    )

    // Flows for database state
    val allergies: StateFlow<List<Allergy>> = repository.allAllergies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val medications: StateFlow<List<Medication>> = repository.allMedications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val profile: StateFlow<UserProfile> = repository.userProfile
        .map { it ?: UserProfile() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    // UI state flows
    private val _selectedAllergy = MutableStateFlow<Allergy?>(null)
    val selectedAllergy: StateFlow<Allergy?> = _selectedAllergy.asStateFlow()

    private val _isAllergyModalVisible = MutableStateFlow(false)
    val isAllergyModalVisible: StateFlow<Boolean> = _isAllergyModalVisible.asStateFlow()

    private val _selectedMedication = MutableStateFlow<Medication?>(null)
    val selectedMedication: StateFlow<Medication?> = _selectedMedication.asStateFlow()

    private val _isMedicationModalVisible = MutableStateFlow(false)
    val isMedicationModalVisible: StateFlow<Boolean> = _isMedicationModalVisible.asStateFlow()

    // Multi-selection for deletions
    private val _selectedAllergiesToDelete = MutableStateFlow<Set<Int>>(emptySet())
    val selectedAllergiesToDelete: StateFlow<Set<Int>> = _selectedAllergiesToDelete.asStateFlow()

    private val _selectedMedicationsToDelete = MutableStateFlow<Set<Int>>(emptySet())
    val selectedMedicationsToDelete: StateFlow<Set<Int>> = _selectedMedicationsToDelete.asStateFlow()

    // Status / message display
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Current navigation state
    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    fun login() {
        _isUserLoggedIn.value = true
    }

    fun logout() {
        _isUserLoggedIn.value = false
    }

    // Allergy actions
    fun selectAllergy(allergy: Allergy) {
        _selectedAllergy.value = allergy
        _isAllergyModalVisible.value = true
    }

    fun closeAllergyModal() {
        _isAllergyModalVisible.value = false
        _selectedAllergy.value = null
    }

    fun toggleAllergySelection(allergyId: Int) {
        val current = _selectedAllergiesToDelete.value
        _selectedAllergiesToDelete.value = if (current.contains(allergyId)) {
            current - allergyId
        } else {
            current + allergyId
        }
    }

    fun deleteAllergy(allergyId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllergy(allergyId)
            showToast("Alergia removida!")
        }
    }

    fun deleteSelectedAllergies() {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedAllergiesToDelete.value.forEach { id ->
                repository.deleteAllergy(id)
            }
            _selectedAllergiesToDelete.value = emptySet()
            showToast("Alergias selecionadas excluídas!")
        }
    }

    fun saveAllergy(
        name: String,
        type: String,
        severity: String,
        symptoms: String,
        instructions: String,
        medicationName: String,
        medicationDose: String,
        urgent: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newAllergy = Allergy(
                name = name,
                type = type,
                severity = severity,
                symptoms = symptoms,
                instructions = instructions,
                emergencyMedicationName = medicationName,
                emergencyMedicationDose = medicationDose,
                emergencyMedicationUrgent = urgent
            )
            repository.insertAllergy(newAllergy)
            showToast("Nova alergia salva!")
        }
    }

    // Medication actions
    fun selectMedication(medication: Medication) {
        _selectedMedication.value = medication
        _isMedicationModalVisible.value = true
    }

    fun closeMedicationModal() {
        _isMedicationModalVisible.value = false
        _selectedMedication.value = null
    }

    fun toggleMedicationSelection(medicationId: Int) {
        val current = _selectedMedicationsToDelete.value
        _selectedMedicationsToDelete.value = if (current.contains(medicationId)) {
            current - medicationId
        } else {
            current + medicationId
        }
    }

    fun deleteMedication(medicationId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMedication(medicationId)
            showToast("Medicamento removido!")
        }
    }

    fun deleteSelectedMedications() {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedMedicationsToDelete.value.forEach { id ->
                repository.deleteMedication(id)
            }
            _selectedMedicationsToDelete.value = emptySet()
            showToast("Medicamentos excluídos!")
        }
    }

    fun saveMedication(
        name: String,
        dosage: String,
        isContinuous: Boolean,
        frequency: String,
        nextDoseTime: String,
        startedDate: String,
        durationDays: Int,
        notificationEnabled: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newMed = Medication(
                name = name,
                dosage = dosage,
                isContinuous = isContinuous,
                frequency = frequency,
                nextDoseTime = nextDoseTime,
                startedDate = if (isContinuous) "Uso Contínuo" else startedDate,
                durationDays = if (isContinuous) 0 else durationDays,
                currentDay = if (isContinuous) 0 else 1,
                notificationEnabled = notificationEnabled
            )
            repository.insertMedication(newMed)
            showToast("Medicamento salvo!")
        }
    }

    // User Profile Actions
    fun saveProfile(updatedProfile: UserProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfile(updatedProfile)
            showToast("Alterações salvas!")
        }
    }

    // Toast control
    private fun showToast(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _toastMessage.value = message
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
