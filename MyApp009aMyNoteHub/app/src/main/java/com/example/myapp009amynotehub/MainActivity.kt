package com.example.myapp009amynotehub

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp009amynotehub.data.Note
import com.example.myapp009amynotehub.data.NoteDao
import com.example.myapp009amynotehub.data.NoteHubDatabaseInstance
import com.example.myapp009amynotehub.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteDao: NoteDao
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Text, podle kterého se hledá
        val searchQuery = MutableStateFlow("")

        // Po kliknutí na FAB otevře AddNoteActivity
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        // DAO
        noteDao = NoteHubDatabaseInstance.getDatabase(applicationContext).noteDao()

        // Adapter s callbacky pro editaci a mazání
        adapter = NoteAdapter(
            onEditClick = { note ->
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra("note_id", note.id)
                startActivity(intent)
            },
            onDeleteClick = { note ->
                askDeleteNote(note)
            }
        )

        // RecyclerView
        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotes.adapter = adapter

        // Změna textu ve vyhledávání změní hodnotu searchQuery
        binding.searchViewNotes.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery.value = newText ?: ""
                return true
            }
        })

        // Sleduje text vyhledávání a podle něj načítá poznámky z databáze.
        // Flow z Room se samo obnoví, když se data v databázi změní.
        lifecycleScope.launch {
            searchQuery.collectLatest { text ->
                if (text.isEmpty()) {
                    // Zobrazit všechny poznámky
                    noteDao.getAllNotes().collectLatest { notes ->
                        adapter.submitList(notes)
                    }
                } else {
                    // Zobrazit jen nalezené poznámky
                    noteDao.searchNotes(text).collectLatest { notes ->
                        adapter.submitList(notes)
                    }
                }
            }
        }
    }

    // Před smazáním se zeptá, jestli to uživatel opravdu chce
    private fun askDeleteNote(note: Note) {
        AlertDialog.Builder(this)
            .setMessage(R.string.delete_question)
            .setPositiveButton(R.string.yes) { _, _ ->
                deleteNote(note)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun deleteNote(note: Note) {
        // Mazání běží na vlákně pro práci s databází, aby se nezablokovalo UI
        lifecycleScope.launch(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }
}
