package com.lifebestpractices.morningbooster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.lifebestpractices.morningbooster.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val timerBinding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(timerBinding.root)
        val timers: MutableList<PractiseTimer> = getAndPrepareTimers()
        startTimers(timers, timerBinding)
    }

    private fun getAndPrepareTimers(): MutableList<PractiseTimer> {
        val firstTimer = intent.getSerializableExtra(INTENT_PARAM_KEY_FIRST_TIMER) as PractiseTimer
        val secondTimer =
            intent.getSerializableExtra(INTENT_PARAM_KEY_SECOND_TIMER) as PractiseTimer
        val thirdTimer = intent.getSerializableExtra(INTENT_PARAM_KEY_THIRD_TIMER) as PractiseTimer
        val forthTimer = intent.getSerializableExtra(INTENT_PARAM_KEY_FOURTH_TIMER) as PractiseTimer
        val fifthTimer = intent.getSerializableExtra(INTENT_PARAM_KEY_FIFTH_TIMER) as PractiseTimer
        val sixthTimer = intent.getSerializableExtra(INTENT_PARAM_KEY_SIXTH_TIMER) as PractiseTimer
        val timers: MutableList<PractiseTimer> =
            mutableListOf(firstTimer, secondTimer, thirdTimer, forthTimer, fifthTimer, sixthTimer)
        timers.removeAll {
            !it.enabled
        }
        timers.sortBy {
            it.position
        }
        return timers
    }

    private fun startTimers(timers: MutableList<PractiseTimer>, timerBinding: ActivityTimerBinding) {
        if (timers.isNotEmpty()) {
            Log.d(TAG, "Start ${timers.first().name} timer")
            object : CountDownTimer(timers.first().minutes, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timerBinding.timerTitle.text = timers.first().name
                    timerBinding.timer.text = getString(R.string.timer, remainingMinutes(millisUntilFinished),
                        remainingSeconds(millisUntilFinished))
                }

                override fun onFinish() {
                    timers.removeAt(0)
                    startTimers(timers, timerBinding)
                }
            }.start()
        } else {
            timerBinding.timerTitle.text = "Done"
            timerBinding.timer.text = ""
            Log.d(TAG, "The timers finished")
        }
    }

    private fun remainingMinutes(millisUntilFinished: Long) =
        millisUntilFinished / 60000

    private fun remainingSeconds(millisUntilFinished: Long) =
        (millisUntilFinished / 1000) % 60
}
