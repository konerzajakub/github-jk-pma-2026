package com.example.myapp003xml

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Unicode symboly pro hodnoty kostky 1-6
    private val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")

    // Počet náhodných změn během animace a prodleva mezi nimi
    private val animationSteps = 10
    private val stepDelayMs = 250L

    // Kolik posledních hodů se ukazuje
    private val historySize = 5

    // Počet hodů a posledních pár výsledků (nejnovější první)
    private var rollCount = 0
    private var history = listOf<String>()

    private lateinit var tvDice: TextView
    private lateinit var btnRoll: Button
    private lateinit var tvRollCount: TextView
    private lateinit var tvHistory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Odsazení obsahu od systémových lišt.
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.llMain)
        ) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Získání prvků z rozvržení podle ID
        tvDice = findViewById(R.id.tvDice)
        btnRoll = findViewById(R.id.btnRoll)
        tvRollCount = findViewById(R.id.tvRollCount)
        tvHistory = findViewById(R.id.tvHistory)

        showStats()
        btnRoll.setOnClickListener { rollDice() }
    }

    // Animace hodu: několik náhodných změn, pak výsledný hod
    private fun rollDice() {
        lifecycleScope.launch {
            // Během animace nelze hodit znovu
            btnRoll.isEnabled = false

            repeat(animationSteps) {
                tvDice.text = diceSymbols.random()
                delay(stepDelayMs)
            }

            // Výsledný hod
            val result = diceSymbols.random()
            tvDice.text = result

            // Výsledek se přičte a přidá na začátek historie
            rollCount++
            history = (listOf(result) + history).take(historySize)
            showStats()

            btnRoll.isEnabled = true
        }
    }

    // Vypsání počtu hodů a historie, texty se musí přepsat ručně
    private fun showStats() {
        tvRollCount.text = getString(R.string.roll_count, rollCount)
        tvHistory.text = history.joinToString(" ")
    }
}
