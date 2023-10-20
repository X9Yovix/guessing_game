package mpdam.android.guessinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var playerName: EditText
    private lateinit var beginnerMode: RadioButton
    private lateinit var hardMode: RadioButton
    private lateinit var startGame: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerName = findViewById(R.id.playerNameInputET)
        beginnerMode = findViewById(R.id.beginnerR)
        hardMode = findViewById(R.id.hardR)
        startGame = findViewById(R.id.startGameBtn)

        //val sharedPreferences = getSharedPreferences("game_data", Context.MODE_PRIVATE)
        //val editor = sharedPreferences.edit()
        //editor.clear()
        //editor.apply()

        startGame.setOnClickListener {

            if (!verifyEmpty()) {
                val getPlayerName = playerName.text.toString()
                var selectedOption: String

                if (beginnerMode.isChecked) {
                    selectedOption = "B"
                } else {
                    selectedOption = "H"
                }

                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("PLAYER_NAME", getPlayerName)
                intent.putExtra("SELECTED_OPTION", selectedOption)
                startActivity(intent)
            }else{
                showAlert("Error", "Please fill in the player name and choose a game mode")
            }
        }
    }

    private fun verifyEmpty(): Boolean {
        if (playerName.text.isEmpty() || (!beginnerMode.isChecked && !hardMode.isChecked)) {
            return true
        }
        return false
    }

    private fun showAlert(title: String, msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}