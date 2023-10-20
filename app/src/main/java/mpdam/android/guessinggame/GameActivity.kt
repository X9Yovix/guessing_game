package mpdam.android.guessinggame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var playerNameLabel: TextView
    private lateinit var playerAnswer: EditText
    private lateinit var submit: Button
    private lateinit var playerHistory: ListView
    private lateinit var showTimer: TextView
    private lateinit var returnHome : ImageButton

    private val answerList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    private var playerName: String = ""
    private var selectedOption: String = ""

    private val randomNumber = Random.nextInt(1, 100)
    private var attempts: Int = 0

    private lateinit var countDownTimer: CountDownTimer
    private var remainingTime = 20000L //in ms


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        playerName = intent.getStringExtra("PLAYER_NAME").toString()
        selectedOption = intent.getStringExtra("SELECTED_OPTION").toString()

        println(selectedOption)

        playerNameLabel = findViewById(R.id.playerNameTV)
        playerAnswer = findViewById(R.id.playerAnswerET)
        submit = findViewById(R.id.submitBtn)
        playerHistory = findViewById(R.id.playerHistoryLV)
        showTimer = findViewById(R.id.showTimerTV)
        returnHome = findViewById(R.id.returnHomeBtnGameActivity)

        playerNameLabel.setText(playerName)

        if (selectedOption == "B") {
            playerHistory.visibility = ListView.VISIBLE
        } else {
            playerHistory.visibility = ListView.GONE
            initializeTimer()
        }


        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, answerList)

        playerHistory.adapter = adapter

        submit.setOnClickListener {
            println("correct answer: $randomNumber")
            val answer = playerAnswer.text.toString()
            println("element")
            println(answer)
            println("list")
            println(answerList)
            val anserParsed = answer.toInt()
            val res: Boolean = checkAnswer(anserParsed)
            attempts++
            if (!res) {
                if (selectedOption == "B") {
                    answerList.add(0,answer)
                    adapter.notifyDataSetChanged()
                }
                playerAnswer.text.clear()
            }
        }

        returnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkAnswer(answer: Int): Boolean {
        var msgFailedAttempt : String


        if (selectedOption == "H") {
            val difference = answer - randomNumber
            val hintMsg = if (difference < 0) {
                "Try a higher number"
            } else {
                "Try a lower number"
            }
            msgFailedAttempt = "Incorrect Answer, $hintMsg"
        } else {
            msgFailedAttempt = "Incorrect Answer"
        }
        if (answer == randomNumber) {
            showAlert("Correct", "Good job, You guessed the correct number")
            return true
        } else {
            showAlert("Incorrect", msgFailedAttempt)
            return false
        }
    }

    private fun showAlert(title: String, msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(msg)
        if (title == "Correct") {
            if (selectedOption == "H") {
                countDownTimer.cancel()
            }
            builder.setPositiveButton("OK") { dialog, _ ->
                saveGameData(playerName, attempts, selectedOption)
                val intent = Intent(this, HighScoreActivity::class.java)
                startActivity(intent)
            }

        } else {
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun saveGameData(playerName: String, attempts: Int, selectedOption: String) {
        val sharedPreferences = getSharedPreferences("game_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val existingHighScores = sharedPreferences.getString("highScores", "") ?: ""
        val newHighScoreEntry = "$playerName - Attempts: $attempts, Option: $selectedOption"

        val updatedHighScores =
            if (existingHighScores.isNotEmpty()) "$existingHighScores|$newHighScoreEntry" else newHighScoreEntry

        editor.putString("highScores", updatedHighScores)
        editor.apply()
    }

    private fun initializeTimer() {
        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                showTimer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                val builder = AlertDialog.Builder(this@GameActivity)
                builder.setTitle("Time's up")
                builder.setMessage("You lost. You've run out of time")
                builder.setPositiveButton("OK") { dialog, _ ->
                    val intent = Intent(this@GameActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                builder.setOnCancelListener {
                    val intent = Intent(this@GameActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
        countDownTimer.start()
    }
}