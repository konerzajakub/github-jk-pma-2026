package com.example.myapp004objednavka

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp004objednavka.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ošetření systémových lišt – použije se přímo binding.main nebo binding.root
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Změna varianty přepne obrázek
        binding.rgVariant.setOnCheckedChangeListener { _, checkedId ->
            val image = when (checkedId) {
                binding.rbDroopy.id -> R.drawable.img_tatsugiri_droopy
                binding.rbStretchy.id -> R.drawable.img_tatsugiri_stretchy
                else -> R.drawable.img_tatsugiri_curly
            }
            binding.ivTatsugiri.setImageResource(image)
        }

        binding.btnOrder.setOnClickListener { showSummary() }
    }

    // Vypsání vybrané varianty a zaškrtnutých doplňků do souhrnu
    private fun showSummary() {
        val variant = when (binding.rgVariant.checkedRadioButtonId) {
            binding.rbDroopy.id -> binding.rbDroopy.text
            binding.rbStretchy.id -> binding.rbStretchy.text
            else -> binding.rbCurly.text
        }

        val extras = listOf(binding.cbPokeBall, binding.cbGiftBox, binding.cbExpress)
            .filter { it.isChecked }
            .joinToString(getString(R.string.extras_separator)) { it.text }
            .ifEmpty { getString(R.string.summary_no_extras) }

        binding.tvSummary.text = getString(R.string.summary, variant, extras)
    }
}
