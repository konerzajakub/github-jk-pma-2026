package com.example.myapp005objednavkav2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp005objednavkav2.databinding.ActivityMainBinding

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

        // Cena hned po spuštění, ještě před první změnou
        updatePrice()

        // Změna varianty přepočítá cenu
        binding.rgVariant.setOnCheckedChangeListener { _, _ -> updatePrice() }

        // Každý doplněk přepočítá cenu po kliknutí
        binding.cbPokeBall.setOnClickListener { updatePrice() }
        binding.cbGiftBox.setOnClickListener { updatePrice() }
        binding.cbExpress.setOnClickListener { updatePrice() }

        // Každá varianta přepne obrázek
        binding.rbCurly.setOnClickListener {
            binding.ivTatsugiri.setImageResource(R.drawable.img_tatsugiri_curly)
        }
        binding.rbDroopy.setOnClickListener {
            binding.ivTatsugiri.setImageResource(R.drawable.img_tatsugiri_droopy)
        }
        binding.rbStretchy.setOnClickListener {
            binding.ivTatsugiri.setImageResource(R.drawable.img_tatsugiri_stretchy)
        }
    }

    // Základní cena + příplatek za variantu + příplatky za doplňky
    private fun updatePrice() {
        var total = 500

        total += when (binding.rgVariant.checkedRadioButtonId) {
            binding.rbDroopy.id -> 100
            binding.rbStretchy.id -> 200
            else -> 0
        }

        if (binding.cbPokeBall.isChecked) total += 50
        if (binding.cbGiftBox.isChecked) total += 80
        if (binding.cbExpress.isChecked) total += 150

        binding.tvPrice.text = getString(R.string.price_total, total)
    }
}
