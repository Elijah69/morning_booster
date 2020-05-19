package com.lifebestpractices.morningbooster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.lifebestpractices.morningbooster.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val timerBinding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(timerBinding.root)
        val affirmationTime = intent.getIntExtra("affirmation time", 10)
        val visualizationTime = intent.getIntExtra("visualization time", 10)
        val physicalTime = intent.getIntExtra("physical time", 10)
        val readingTime = intent.getIntExtra("reading time", 10)
        val silenceTime = intent.getIntExtra("silence time", 10)
        val diaryTime = intent.getIntExtra("diary time", 10)
        createCountDownTimer("affirmation", affirmationTime)

    }

    private fun createCountDownTimer(timerType: String,affirmationTime: Int): CountDownTimer {
        val toMinutes = 60000
        return object : CountDownTimer(affirmationTime.toLong() * toMinutes, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingMinutes = millisUntilFinished / toMinutes
                val remainingSeconds = (millisUntilFinished / 1000) % 60
                Toast.makeText(
                    this@TimerActivity,
                    "Second for $timerType remaining: $remainingMinutes : $remainingSeconds",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFinish() {
                Toast.makeText(this@TimerActivity, "done!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
