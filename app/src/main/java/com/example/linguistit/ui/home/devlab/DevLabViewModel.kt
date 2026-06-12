package com.example.linguistit.ui.home.devlab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linguistit.model.SavedTerm
import com.example.linguistit.model.SprintResult
import com.example.linguistit.repository.DevLabRepository

class DevLabViewModel : ViewModel() {

    private val repository = DevLabRepository()

    private val _sprintHistory = MutableLiveData<List<SprintResult>?>()
    val sprintHistory: LiveData<List<SprintResult>?> get() = _sprintHistory

    private val _savedTerms = MutableLiveData<List<SavedTerm>?>()
    val savedTerms: LiveData<List<SavedTerm>?> get() = _savedTerms

    private val _sprintSaveSuccess = MutableLiveData<Boolean>()
    val sprintSaveSuccess: LiveData<Boolean> get() = _sprintSaveSuccess

    private val _termSaveSuccess = MutableLiveData<Boolean>()
    val termSaveSuccess: LiveData<Boolean> get() = _termSaveSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadSprintHistory() {
        _isLoading.value = true
        repository.getSprintHistory { sprints ->
            _sprintHistory.value = sprints
            _isLoading.value = false
        }
    }

    fun loadSavedTerms() {
        _isLoading.value = true
        repository.getSavedTerms { terms ->
            _savedTerms.value = terms
            _isLoading.value = false
        }
    }

    fun saveSprintResult(sprintId: String, sprint: SprintResult) {
        _isLoading.value = true
        repository.saveSprintResult(sprintId, sprint) { success ->
            _sprintSaveSuccess.value = success
            _isLoading.value = false
            if (success) {
                loadSprintHistory()
            }
        }
    }

    fun saveTechnicalTerm(term: SavedTerm) {
        _isLoading.value = true
        repository.saveTechnicalTerm(term) { success ->
            _termSaveSuccess.value = success
            _isLoading.value = false
            if (success) {
                loadSavedTerms()
            }
        }
    }

    fun resetSaveStates() {
        _sprintSaveSuccess.value = false
        _termSaveSuccess.value = false
    }
}