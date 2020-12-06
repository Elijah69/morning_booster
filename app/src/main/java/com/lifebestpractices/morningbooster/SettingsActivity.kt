package com.lifebestpractices.morningbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.lifebestpractices.morningbooster.databinding.ActivitySettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settingsBinding.root)
        GlobalScope.launch(Dispatchers.Main) {
            val timersDB = Room.databaseBuilder(this@SettingsActivity, MorningBoosterDatabase::class.java, TIMERS_DB_NAME)
                .build()
            var timers = mutableListOf<PractiseTimer>()
            withContext(Dispatchers.IO) {
                timers = timersDB.practiceTimerDao().getAll()
            }
            val positionArray = arrayOf(1, 2, 3, 4, 5, 6)


            settingsBinding.saveSettingsButton.setOnClickListener {
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
