package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Allergy::class, Medication::class, UserProfile::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun allergyDao(): AllergyDao
    abstract fun medicationDao(): MedicationDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sou_alergico_database"
                )
                .addCallback(AppDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(db: AppDatabase) {
            // Initial Prepopulation for Sou Alérgico App
            val allergyDao = db.allergyDao()
            val medicationDao = db.medicationDao()
            val userProfileDao = db.userProfileDao()

            // Prepopulate User Profile
            userProfileDao.insertProfile(UserProfile())

            // Prepopulate Allergies
            allergyDao.insertAllergies(
                listOf(
                    Allergy(
                        name = "Amendoim",
                        type = "alimento",
                        severity = "Grave",
                        symptoms = "Ingestão acidental de amendoim, óleos de amendoim prensados a frio, ou alimentos processados em máquinas compartilhadas.\nReações podem ocorrer por contato cutâneo ou inalação de partículas em ambientes fechados.",
                        instructions = "Administrar via oral imediatamente aos primeiros sinais de urticária ou prurido. Se houver dificuldade respiratória, use a caneta de epinefrina e ligue para 192.",
                        emergencyMedicationName = "Anti-histamínico",
                        emergencyMedicationDose = "10mg",
                        emergencyMedicationUrgent = true
                    ),
                    Allergy(
                        name = "Lactose",
                        type = "alimento",
                        severity = "Intolerância",
                        symptoms = "Ingestão de leite de vaca ou derivados contendo lactose. Provoca gases, cólicas fortes, náuseas e inchaço abdominal.",
                        instructions = "Ingerir a enzima lactase com o primeiro gole ou garfada de alimentos com leite.",
                        emergencyMedicationName = "Enzima Lactase",
                        emergencyMedicationDose = "10.000 FCC",
                        emergencyMedicationUrgent = false
                    ),
                    Allergy(
                        name = "Penicilina",
                        type = "medicamento",
                        severity = "Medicamentosa",
                        symptoms = "Reação alérgica medicamentosa à família da Penicilina. Provoca erupções cutâneas graves, coceira, febre e em casos extremos anafilaxia.",
                        instructions = "Interromper imediatamente o uso. Se houver falta de ar ou inchaço nos lábios, utilizar auto-injetor de epinefrina e ir ao hospital.",
                        emergencyMedicationName = "Adrenalina / Corticoide",
                        emergencyMedicationDose = "Dose Única",
                        emergencyMedicationUrgent = true
                    ),
                    Allergy(
                        name = "Poeira e Ácaros",
                        type = "alimento", // respirator/environmental, but mapped to "alimento" tab or generalized
                        severity = "Respiratória",
                        symptoms = "Exposição a poeira, ácaros, estofados antigos e tapetes. Provoca rinite alérgica crônica, espirros frequentes e coceira nos olhos.",
                        instructions = "Manter ambientes bem ventilados, limpos e livres de poeira. Usar sprays preventivos indicados.",
                        emergencyMedicationName = "Anti-histamínico Spray",
                        emergencyMedicationDose = "2 jatos",
                        emergencyMedicationUrgent = false
                    ),
                    Allergy(
                        name = "Glúten",
                        type = "alimento",
                        severity = "Alimentar",
                        symptoms = "Ingestão de alimentos contendo trigo, centeio, cevada. Causa desconforto digestivo, diarreia e fadiga crônica devido a intolerância ou doença celíaca.",
                        instructions = "Eliminar vestígios de trigo e glúten da dieta. Em crises acidentais, repousar e beber bastante líquido.",
                        emergencyMedicationName = "Sintomáticos",
                        emergencyMedicationDose = "1 comprimido",
                        emergencyMedicationUrgent = false
                    )
                )
            )

            // Prepopulate Medications
            medicationDao.insertMedications(
                listOf(
                    Medication(
                        name = "Loratadina",
                        dosage = "10mg",
                        isContinuous = true,
                        frequency = "Uma vez ao dia",
                        nextDoseTime = "08:00",
                        startedDate = "Uso Contínuo",
                        durationDays = 0,
                        currentDay = 0,
                        notificationEnabled = true
                    ),
                    Medication(
                        name = "Vitamin D",
                        dosage = "2000 UI",
                        isContinuous = true,
                        frequency = "Uma vez ao dia",
                        nextDoseTime = "12:00",
                        startedDate = "Suplemento",
                        durationDays = 0,
                        currentDay = 0,
                        notificationEnabled = true
                    ),
                    Medication(
                        name = "Amoxicilina",
                        dosage = "500mg",
                        isContinuous = false,
                        frequency = "A cada 8 horas",
                        nextDoseTime = "14:30",
                        startedDate = "12 Out",
                        durationDays = 7,
                        currentDay = 3,
                        notificationEnabled = true
                    )
                )
            )
        }
    }
}
