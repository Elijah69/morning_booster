package com.lifebestpractices.morningbooster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.*
import com.lifebestpractices.morningbooster.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

const val TAG = "MBD"
const val INTENT_PARAM_KEY_FIRST_TIMER = "1 timer"
const val INTENT_PARAM_KEY_SECOND_TIMER = "2 timer"
const val INTENT_PARAM_KEY_THIRD_TIMER = "3 timer"
const val INTENT_PARAM_KEY_FOURTH_TIMER = "4 timer"
const val INTENT_PARAM_KEY_FIFTH_TIMER = "5 timer"
const val INTENT_PARAM_KEY_SIXTH_TIMER = "6 timer"
const val TIMERS_DB_NAME = "morning-booster"


@Entity(indices = [Index(value = ["name"], unique = true)], tableName = "timers") //TODO Make a ViewModel
data class PractiseTimer constructor(@PrimaryKey val name: String, var position : Int) : Serializable{
    var minutes : Long = 60000
    var enabled : Boolean = true
    fun setNewTime(newMinutes : Int){
        minutes = (newMinutes * 60000).toLong()
    }
}

@Dao
interface PracticeTimerDao {

    @Query("SELECT * FROM timers")
    suspend fun getAll() : MutableList<PractiseTimer>

    @Update
    suspend fun updateTimers(vararg timers: PractiseTimer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg practiceTimers: PractiseTimer)
}

@Database(entities = [PractiseTimer::class], version = 1)
abstract class MorningBoosterDatabase : RoomDatabase() {
    abstract fun practiceTimerDao() : PracticeTimerDao
}


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        GlobalScope.launch(Dispatchers.Main) {
            val timersDB = Room.databaseBuilder(this@MainActivity, MorningBoosterDatabase::class.java, TIMERS_DB_NAME)
                .build()
            var timers = mutableListOf<PractiseTimer>()
            withContext(Dispatchers.IO) {
                timers = timersDB.practiceTimerDao().getAll()
            }
            Log.d(TAG, "Getting timers from db job completed")
                if (timers.size != 0) {
                    Log.d(TAG, "The information upload completed")
                    timers.sortBy { it.position }
                    setTimersView(mainBinding, timers)
                } else {
                    Log.d(TAG, "The timers db is empty, initializing default timers")
                    timers = initializeDefaultTimers(timers)
                    setTimersView(mainBinding, timers)
                    withContext(Dispatchers.IO) {
                        timersDB.practiceTimerDao().insertAll(
                            timers[0],
                            timers[1],
                            timers[2],
                            timers[3],
                            timers[4],
                            timers[5]
                        )
                    }
                    Log.d(TAG, "The information inserted in db")
                }

            setTimersView(mainBinding, timers)

            setSwitchesLogic(mainBinding, timers)

            setBoostButtonLogic(mainBinding, timers)

            setSettingsButtonLogic(mainBinding)
        }
    }

    private fun initializeDefaultTimers(timers: MutableList<PractiseTimer>): MutableList<PractiseTimer> {
        var timers1 = timers
        val firstTimer = PractiseTimer("affirmation", 1)
        val secondTimer = PractiseTimer("visualization", 2)
        val thirdTimer = PractiseTimer("physical", 3)
        val fourthTimer = PractiseTimer("reading", 4)
        val fifthTimer = PractiseTimer("silence", 5)
        val sixthTimer = PractiseTimer("diary", 6)
        timers1 = mutableListOf(
            firstTimer,
            secondTimer,
            thirdTimer,
            fourthTimer,
            fifthTimer,
            sixthTimer
        )
        return timers1
    }

    private fun setSettingsButtonLogic(mainBinding: ActivityMainBinding) {
        mainBinding.openSettingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setBoostButtonLogic(
        mainBinding: ActivityMainBinding,
        timers: MutableList<PractiseTimer>
    ) {
        mainBinding.boostButton.setOnClickListener {
            val totalTime = countTotalTime(timers)
            if (totalTime > 0) {
                val intent = Intent(this, TimerActivity::class.java).apply {
                    putExtra(INTENT_PARAM_KEY_FIRST_TIMER, timers[0])
                    putExtra(INTENT_PARAM_KEY_SECOND_TIMER, timers[1])
                    putExtra(INTENT_PARAM_KEY_THIRD_TIMER, timers[2])
                    putExtra(INTENT_PARAM_KEY_FOURTH_TIMER, timers[3])
                    putExtra(INTENT_PARAM_KEY_FIFTH_TIMER, timers[4])
                    putExtra(INTENT_PARAM_KEY_SIXTH_TIMER, timers[5])
                }
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "You have nothing to do, please, chose some practice element",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setSwitchesLogic(
        mainBinding: ActivityMainBinding,
        timers: MutableList<PractiseTimer>
    ) {
        mainBinding.firstTimerSwitch.setOnCheckedChangeListener { _, isChecked ->
            timers[0].enabled = isChecked
            setTotalTime(timers, mainBinding)
        }

        mainBinding.secondTimerSwitch.setOnCheckedChangeListener { _, isChecked ->
            timers[1].enabled = isChecked
            setTotalTime(timers, mainBinding)
        }

        mainBinding.thirdTimerSwitch.setOnCheckedChangeListener { _, isChecked ->
            timers[2].enabled = isChecked
            setTotalTime(timers, mainBinding)
        }

        mainBinding.fourthTimerSwitch.setOnCheckedChangeListener { _, isChecked ->
            timers[3].enabled = isChecked
            setTotalTime(timers, mainBinding)
        }

        mainBinding.fifthTimerSwitch.setOnCheckedChangeListener { _, isChecked ->
            timers[4].enabled = isChecked
            setTotalTime(timers, mainBinding)
        }

        mainBinding.sixthTimerSwitch.setOnCheckedChangeListener { _, isChecked ->
            timers[5].enabled = isChecked
            setTotalTime(timers, mainBinding)
        }
    }

    private fun countTotalTime(timers: MutableList<PractiseTimer>) : Int {
        var totalTime = 0
        timers.forEach {
            if (it.enabled) totalTime += (it.minutes / 60000).toInt()
        }
        return totalTime
    }

    private fun setTimersView(
        mainBinding: ActivityMainBinding,
        timers: MutableList<PractiseTimer>
    ) {
        setFirstTimerView(mainBinding, timers)
        setSecondTimerView(mainBinding, timers)
        setThirdTimerView(mainBinding, timers)
        setFourthTimerView(mainBinding, timers)
        setFifthTimerView(mainBinding, timers)
        setSixthTimerView(mainBinding, timers)
        setTotalTime(timers, mainBinding)

    }

    private fun setTotalTime(
        timers: MutableList<PractiseTimer>,
        mainBinding: ActivityMainBinding
    ) {
        val totalTime = countTotalTime(timers)
        mainBinding.totalTimeTimer.text = resources.getQuantityString(
            R.plurals.minutes,
            totalTime, totalTime
        )

    }

    private fun setSixthTimerView(
        mainBinding: ActivityMainBinding,
        timers: MutableList<PractiseTimer>
    ) {
        mainBinding.sixthTimerLabel.text = getString(R.string.timer_label, timers[5].name)
        mainBinding.sixthTimerTime.text = resources.getQuantityString(
            R.plurals.minutes,
            (timers[5].minutes / 60000).toInt(), (timers[5].minutes / 60000).toInt()
        )
        mainBinding.sixthTimerSwitch.isChecked = timers[5].enabled
    }

    private fun setFifthTimerView(
        mainBinding: ActivityMainBinding,
        timers: MutableList<PractiseTimer>
    ) {
        mainBinding.fifthTimerLabel.text = getString(R.string.timer_label, timers[4].name)
        mainBinding.fifthTimerTime.text = resources.getQuantityString(
            R.plurals.minutes,
            (timers[4].minutes / 60000).toInt(), (timers[4].minutes / 60000).toInt()
        )
        mainBinding.fifthTimerSwitch.isChecked = timers[4].enabled
    }

    private fun setFourthTimerView(
        mainBinding: ActivityMainBinding,
        timers: MutableList<PractiseTimer>
    ) {
        mainBinding.fourthTimerLabel.text = getString(R.string.timer_label, timers[3].name)
        mainBinding.fourthTimerTime.text = resources.getQuantityString(
            R.plurals.minutes,
            (timers[3].minutes / 60000).toInt(), (timers[3].minutes / 60000).toInt()
        )
        mainBinding.fourthTimerSwitch.isChecked = timers[3].enabled
    }

    private fun setThirdTimerView(
        mainBinding: ActivityMainBinding,
        timers: MutableList<PractiseTimer>
    ) {
        mainBinding.thirdTimerLabel.text = getString(R.string.timer_label, timers[2].name)
        mainBinding.thirdTimerTime.text = resources.getQuantityString(
            R.plurals.minutes,
            (timers[2].minutes / 60000).toInt(), (timers[2].minutes / 60000).toInt()
        )
        mainBinding.thirdTimerSwitch.isChecked = timers[2].enabled
    }

    private fun setSecondTimerView(
        mainBinding: ActivityMainBinding,
        timers: MutableList<PractiseTimer>
    ) {
        mainBinding.secondTimerLabel.text = getString(R.string.timer_label, timers[1].name)
        mainBinding.secondTimerTime.text = resources.getQuantityString(
            R.plurals.minutes,
            (timers[1].minutes / 60000).toInt(), (timers[1].minutes / 60000).toInt()
        )
        mainBinding.secondTimerSwitch.isChecked = timers[1].enabled
    }

    private fun setFirstTimerView(
        mainBinding: ActivityMainBinding,
        timers: MutableList<PractiseTimer>
    ) {
        mainBinding.firstTimerLabel.text = getString(R.string.timer_label, timers[0].name)
        mainBinding.firstTimerTime.text = resources.getQuantityString(
            R.plurals.minutes,
            (timers[0].minutes / 60000).toInt(), (timers[0].minutes / 60000).toInt()
        )
        mainBinding.firstTimerSwitch.isChecked = timers[0].enabled
    }


}
