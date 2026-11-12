package com.example.myapp009amynotehub

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapp009amynotehub.data.Note
import com.example.myapp009amynotehub.data.NoteHubDatabaseInstance
import com.example.myapp009amynotehub.databinding.ActivityAddNoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = NoteHubDatabaseInstance.getDatabase(this)
        val noteDao = db.noteDao()

        // ---------- Kategorie (Spinner) ----------
        val categories = resources.getStringArray(R.array.categories)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = spinnerAdapter

        // ---------- Datum vytvoření ----------
        val now = System.currentTimeMillis()
        val formatted = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            .format(Date(now))
        binding.textCreatedAtInfo.text = getString(R.string.created_at, formatted)

        // ---------- Uložení poznámky ----------
        binding.btnSaveNote.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val content = binding.editTextContent.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, R.string.fill_both, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = Note(
                title = title,
                content = content,
                category = category,
                createdAt = now
            )

            // Uložení běží na vlákně pro práci s databází (IO), aby se nezablokovalo UI
            lifecycleScope.launch(Dispatchers.IO) {
                noteDao.insert(note)

                // Po uložení zavře obrazovku a vrátí se na seznam
                finish()
            }
        }
    }
}
