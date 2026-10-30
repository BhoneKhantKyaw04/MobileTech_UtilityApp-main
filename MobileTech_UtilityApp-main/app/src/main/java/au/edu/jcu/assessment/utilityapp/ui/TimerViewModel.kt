package au.edu.jcu.assessment.utilityapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import au.edu.jcu.assessment.utilityapp.data.AdviceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimerUiState(
    val focusMinutes: Int = 25,
    val breakMinutes: Int = 5,
    val dailyGoal: Int = 4,
    val secondsLeft: Int = 25 * 60,
    val isRunning: Boolean = false,
    val isBreak: Boolean = false,
    val sessionsCompleted: Int = 0,
    val advice: String = "Loading advice..."
)

class TimerViewModel(private val repository: AdviceRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadAdvice()
    }

    fun startPause() {
        if (_uiState.value.isRunning) pause() else start()
    }

    private fun start() {
        _uiState.update { it.copy(isRunning = true) }
        timerJob = viewModelScope.launch {
            while (_uiState.value.secondsLeft > 0) {
                delay(1000)
                _uiState.update { it.copy(secondsLeft = it.secondsLeft - 1) }
            }
            onTimerFinished()
        }
    }

    private fun pause() {
        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
    }

    private fun onTimerFinished() {
        if (_uiState.value.isBreak) {
            // Break is over, back to focus
            _uiState.update {
                it.copy(isBreak = false, isRunning = false, secondsLeft = it.focusMinutes * 60)
            }
        } else {
            // Focus session done, count it and start the break
            _uiState.update {
                it.copy(
                    isBreak = true,
                    isRunning = false,
                    secondsLeft = it.breakMinutes * 60,
                    sessionsCompleted = it.sessionsCompleted + 1
                )
            }
            loadAdvice()
        }
    }

    fun reset() {
        timerJob?.cancel()
        _uiState.update {
            it.copy(isRunning = false, isBreak = false, secondsLeft = it.focusMinutes * 60)
        }
    }

    fun loadAdvice() {
        viewModelScope.launch {
            repository.getAdvice()
                .onSuccess { text -> _uiState.update { it.copy(advice = text) } }
                .onFailure { _uiState.update { it.copy(advice = "Couldn't load advice. Check your internet.") } }
        }
    }
}

class TimerViewModelFactory(private val repository: AdviceRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TimerViewModel(repository) as T
    }
}