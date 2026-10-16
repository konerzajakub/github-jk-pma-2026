package com.example.myapp005objednavkav2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp005objednavkav2.databinding.ActivitySummaryBinding

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        // Inicializace ViewBindingu
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ochrana před překryvem systémovými lištami
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Přidá vnitřní okraj (padding) odpovídající velikosti horní a spodní systémové lišty
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Přijetí dat z Intentu
        val variant = intent.getStringExtra("EXTRA_VARIANT") ?: getString(R.string.unknown_variant)
        val totalPrice = intent.getStringExtra("EXTRA_TOTAL_PRICE") ?: ""

        val hasPokeBall = intent.getBooleanExtra("EXTRA_HAS_POKE_BALL", false)
        val hasGiftBox = intent.getBooleanExtra("EXTRA_HAS_GIFT_BOX", false)
        val hasExpress = intent.getBooleanExtra("EXTRA_HAS_EXPRESS", false)

        // Sestavení textu s doplňky
        var extrasText = getString(R.string.summary_extras)
        if (hasPokeBall) extrasText += "\n- " + getString(R.string.extra_poke_ball)
        if (hasGiftBox) extrasText += "\n- " + getString(R.string.extra_gift_box)
        if (hasExpress) extrasText += "\n- " + getString(R.string.extra_express)

        // Uživatel nezaškrtl nic
        if (!hasPokeBall && !hasGiftBox && !hasExpress) {
            extrasText += "\n- " + getString(R.string.summary_no_extras)
        }

        // Zobrazení dat
        binding.tvSelectedBike.text = getString(R.string.summary_variant, variant)
        binding.tvExtras.text = extrasText
        binding.tvFinalPrice.text = totalPrice

        // Návrat na úpravu objednávky
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
