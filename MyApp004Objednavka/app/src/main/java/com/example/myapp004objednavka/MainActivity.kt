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

    // Vypsání vybrané varianty, doplňků a ceny do souhrnu
    private fun showSummary() {
        val basePrice = 500
        var total = basePrice

        // Vybraná varianta a příplatek za ni
        var variant = binding.rbCurly.text
        if (binding.rbDroopy.isChecked) {
            variant = binding.rbDroopy.text
            total += 100
        }
        if (binding.rbStretchy.isChecked) {
            variant = binding.rbStretchy.text
            total += 200
        }

        // Zaškrtnuté doplňky a příplatky za ně
        val extras = mutableListOf<String>()
        if (binding.cbPokeBall.isChecked) {
            extras.add(binding.cbPokeBall.text.toString())
            total += 50
        }
        if (binding.cbGiftBox.isChecked) {
            extras.add(binding.cbGiftBox.text.toString())
            total += 80
        }
        if (binding.cbExpress.isChecked) {
            extras.add(binding.cbExpress.text.toString())
            total += 150
        }

        var extrasText = extras.joinToString(getString(R.string.extras_separator))
        if (extras.isEmpty()) {
            extrasText = getString(R.string.summary_no_extras)
        }

        binding.tvSummary.text = getString(R.string.summary, variant, extrasText, basePrice, total)
    }
}
