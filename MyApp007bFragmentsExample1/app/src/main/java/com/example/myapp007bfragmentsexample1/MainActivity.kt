package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapp007bfragmentsexample1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ošetření systémových lišt
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Po spuštění se ukáže galerie (jen poprvé, ne po otočení displeje)
        if (savedInstanceState == null) {
            showFragment(GalleryFragment())
        }

        // Menu dole přepíná fragmenty
        binding.btnGallery.setOnClickListener { showFragment(GalleryFragment()) }
        binding.btnInfo.setOnClickListener { showFragment(InfoFragment()) }
        binding.btnQuiz.setOnClickListener { showFragment(QuizFragment()) }
    }

    // Vymění obsah kontejneru za zadaný fragment
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
