package com.lifebestpractices.morningbooster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lifebestpractices.morningbooster.databinding.ActivityMainBinding
import java.io.Serializable

const val TAG = "MBD"
const val INTENT_PARAM_KEY_FIRST_TIMER = "1 timer"
const val INTENT_PARAM_KEY_SECOND_TIMER = "2 timer"
const val INTENT_PARAM_KEY_THIRD_TIMER = "3 timer"
const val INTENT_PARAM_KEY_FOURTH_TIMER = "4 timer"
const val INTENT_PARAM_KEY_FIFTH_TIMER = "5 timer"
const val INTENT_PARAM_KEY_SIXTH_TIMER = "6 timer"

class PractiseTimer constructor(val name: String, var position : Int) : Serializable{
    var minutes : Long = 60000
    var enabled : Boolean = true
    fun setNewTime(newMinutes : Int){
        minutes = (newMinutes * 60000).toLong()
    }
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        val firstTimer = PractiseTimer("affirmation", 2)
        firstTimer.setNewTime(1)
        val secondTimer = PractiseTimer("visualization", 1)
        secondTimer.setNewTime(1)
        val thirdTimer = PractiseTimer("physical", 3)
        thirdTimer.setNewTime(1)
        thirdTimer.enabled = false
        val fourthTimer = PractiseTimer("reading", 4)
        fourthTimer.setNewTime(1)
        val fifthTimer = PractiseTimer("silence", 5)
        fifthTimer.setNewTime(1)
        val sixthTimer = PractiseTimer("diary", 6)
        sixthTimer.setNewTime(1)
        val timers: MutableList<PractiseTimer> = mutableListOf(firstTimer, secondTimer, thirdTimer, fourthTimer, fifthTimer, sixthTimer)
        timers.sortBy {
            it.position
        }
        mainBinding.affirmationTime.text = "${timers[0].minutes / 60000} minutes"
        mainBinding.visualizationTime.text = "${timers[1].minutes / 60000} minutes"
        mainBinding.physicalTime.text = "${timers[2].minutes / 60000} minutes"
        mainBinding.readingTime.text = "${timers[3].minutes / 60000} minutes"
        mainBinding.silenceTime.text = "${timers[4].minutes / 60000} minutes"
        mainBinding.diaryTime.text = "${timers[5].minutes / 60000} minutes"

        mainBinding.boostButton.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java).apply {
                putExtra(INTENT_PARAM_KEY_FIRST_TIMER, firstTimer)
                putExtra(INTENT_PARAM_KEY_SECOND_TIMER, secondTimer)
                putExtra(INTENT_PARAM_KEY_THIRD_TIMER, thirdTimer)
                putExtra(INTENT_PARAM_KEY_FOURTH_TIMER, fourthTimer)
                putExtra(INTENT_PARAM_KEY_FIFTH_TIMER, fifthTimer)
                putExtra(INTENT_PARAM_KEY_SIXTH_TIMER, sixthTimer)
            }
            startActivity(intent)
        }

        mainBinding.openSettingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }


}
