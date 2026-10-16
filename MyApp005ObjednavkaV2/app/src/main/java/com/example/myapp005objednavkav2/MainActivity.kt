package com.example.myapp005objednavkav2

import android.content.Intent
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

        binding.btnOrder.setOnClickListener {
            // Název vybrané varianty
            val variant = when (binding.rgVariant.checkedRadioButtonId) {
                binding.rbDroopy.id -> binding.rbDroopy.text.toString()
                binding.rbStretchy.id -> binding.rbStretchy.text.toString()
                else -> binding.rbCurly.text.toString()
            }

            // Intent na druhou obrazovku, data se přibalí jako klíč-hodnota
            val intent = Intent(this, SummaryActivity::class.java)
            intent.putExtra("EXTRA_VARIANT", variant)
            intent.putExtra("EXTRA_TOTAL_PRICE", binding.tvPrice.text.toString())
            intent.putExtra("EXTRA_HAS_POKE_BALL", binding.cbPokeBall.isChecked)
            intent.putExtra("EXTRA_HAS_GIFT_BOX", binding.cbGiftBox.isChecked)
            intent.putExtra("EXTRA_HAS_EXPRESS", binding.cbExpress.isChecked)

            startActivity(intent)
        }

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
