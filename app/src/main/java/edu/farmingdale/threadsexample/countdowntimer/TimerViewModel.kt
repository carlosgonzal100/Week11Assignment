package edu.farmingdale.threadsexample.countdowntimer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TimerViewModel : ViewModel() {
    private var timerJob: Job? = null

    // Values selected in time picker
    var selectedHour by mutableIntStateOf(0)
        private set
    var selectedMinute by mutableIntStateOf(0)
        private set
    var selectedSecond by mutableIntStateOf(0)
        private set

    // Used to ensure sound only plays after the timer has actually started
    //instead of playing everytime the app starts because of the timer
    //already being at 0
    var hasStarted by mutableStateOf(false)
        private set

    // Total milliseconds when timer starts
    var totalMillis by mutableLongStateOf(0L)
        private set

    // Time that remains
    var remainingMillis by mutableLongStateOf(0L)
        private set

    // Timer's running status
    var isRunning by mutableStateOf(false)
        private set

    fun selectTime(hour: Int, min: Int, sec: Int) {
        selectedHour = hour
        selectedMinute = min
        selectedSecond = sec

        //added this to help display what time you choose
        //on the timer text by recalculating remainingMilis
        remainingMillis = (hour * 3600 + min * 60 + sec) * 1000L

    }

    fun startTimer() {
        // Convert hours, minutes, and seconds to milliseconds
        totalMillis = (selectedHour * 60 * 60 + selectedMinute * 60 + selectedSecond) * 1000L

        // Start coroutine that makes the timer count down
        if (totalMillis > 0) {
            //sets the hasStarted to true
            hasStarted = true
            isRunning = true
            remainingMillis = totalMillis

            timerJob = viewModelScope.launch {
                while (remainingMillis > 0) {
                    delay(1000)
                    remainingMillis -= 1000
                }

                isRunning = false
            }
        }
    }

    fun cancelTimer() {
        if (isRunning) {
            timerJob?.cancel()
            isRunning = false

            // sets the timer to the time the user chose when cacelled
            //instead of automatically going to 0
            remainingMillis = (selectedHour * 3600 +
                    selectedMinute * 60 +
                    selectedSecond) * 1000L

        }


    }

    //Added a reset timer method to reset the timer to the selected time
    fun resetTimer() {

        //stops the timer
        cancelTimer()

        //resets timer text to 0 for everything
        selectedHour = 0
        selectedMinute = 0
        selectedSecond = 0
        remainingMillis = 0L
        isRunning = false

        // restore remaining time to the selected H:M:S total
        val total = (selectedHour * 3600 +
                selectedMinute * 60 +
                selectedSecond) * 1000L
        remainingMillis = total
        isRunning = false

        //sets the has started value on false
        //so when the reset button is pressed and sets
        //the timers text value and everything to 0, the sound
        //does not play
        hasStarted = false

    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}