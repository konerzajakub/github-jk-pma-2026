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
import com.example.myapp009amynotehub.data.NoteDao
import com.example.myapp009amynotehub.data.NoteHubDatabaseInstance
import com.example.myapp009amynotehub.databinding.ActivityEditNoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var noteDao: NoteDao
    private var noteId: Int = -1

    // Datum vytvoření se při editaci nemění, jen si ho zapamatujeme
    private var createdAt: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        noteDao = NoteHubDatabaseInstance.getDatabase(this).noteDao()

        // Kategorie do spinneru
        val categories = resources.getStringArray(R.array.categories)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = spinnerAdapter

        // ID poznámky z Intentu
        noteId = intent.getIntExtra("note_id", -1)
        binding.tvNoteId.text = getString(R.string.note_id, noteId)

        // Načteme poznámku z databáze a vyplníme pole
        lifecycleScope.launch {
            val note = noteDao.getNoteById(noteId)
            if (note != null) {
                binding.etEditTitle.setText(note.title)
                binding.etEditContent.setText(note.content)
                createdAt = note.createdAt

                val formatted = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                    .format(Date(note.createdAt))
                binding.textCreatedAtInfo.text = getString(R.string.created_at, formatted)

                // Ve spinneru vybereme kategorii poznámky
                for (i in categories.indices) {
                    if (categories[i] == note.category) {
                        binding.spinnerCategory.setSelection(i)
                    }
                }
            }
        }

        // Kliknutí na Uložit změny
        binding.btnSaveChanges.setOnClickListener {
            val updatedTitle = binding.etEditTitle.text.toString()
            val updatedContent = binding.etEditContent.text.toString()
            val updatedCategory = binding.spinnerCategory.selectedItem.toString()

            if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
                Toast.makeText(this, R.string.fill_both, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedNote = Note(
                id = noteId,
                title = updatedTitle,
                content = updatedContent,
                createdAt = createdAt,
                category = updatedCategory
            )

            lifecycleScope.launch(Dispatchers.IO) {
                noteDao.update(updatedNote)
                finish()
            }
        }
    }
}
