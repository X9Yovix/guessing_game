package mpdam.android.guessinggame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton

import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class HighScoreActivity : AppCompatActivity() {

    private lateinit var highScoreList : ListView
    private lateinit var returnHomeBtn : ImageButton

    private val savedList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        highScoreList = findViewById(R.id.highScoreLV)
        returnHomeBtn = findViewById(R.id.returnHomeBtnGameHighScoreActivity)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, savedList)
        highScoreList.adapter = adapter

        loadHighScores()

        returnHomeBtn.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadHighScores() {
        val sharedPreferences = getSharedPreferences("game_data", Context.MODE_PRIVATE)
        val highScoresString = sharedPreferences.getString("highScores", "") ?: ""
        val highScoreEntries = highScoresString.split("|")
        savedList.clear()
        savedList.addAll(highScoreEntries)
        adapter.notifyDataSetChanged()
    }
}