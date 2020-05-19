package com.lifebestpractices.morningbooster

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lifebestpractices.morningbooster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        val affirmationTime = 7
        val visualizationTime = 8
        val physicalTime = 9
        val readingTime = 11
        val silenceTime = 12
        val diaryTime = 13
        mainBinding.affirmationTime.text = "$affirmationTime minutes"
        mainBinding.visualizationTime.text = "$visualizationTime minutes"
        mainBinding.physicalTime.text = "$physicalTime minutes"
        mainBinding.readingTime.text = "$readingTime minutes"
        mainBinding.silenceTime.text = "$silenceTime minutes"
        mainBinding.diaryTime.text = "$diaryTime minutes"

        mainBinding.boostButton.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java).apply {
                putExtra("affirmation time", affirmationTime)
                putExtra("visualization time", visualizationTime)
                putExtra("physical time", physicalTime)
                putExtra("reading time", readingTime)
                putExtra("silence time", silenceTime)
                putExtra("diary time", diaryTime)
            }
            startActivity(intent)
        }

        mainBinding.openSettingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }


}
