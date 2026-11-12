package com.example.myapp009amynotehub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp009amynotehub.data.Note
import com.example.myapp009amynotehub.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Adapter plní RecyclerView daty. Kliknutí na ikony předá do MainActivity.
class NoteAdapter(
    private val onEditClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // Seznam poznámek, které se mají zobrazit
    private var notes: List<Note> = emptyList()

    // ViewHolder drží binding jedné položky (jedné poznámky)
    class NoteViewHolder(val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Vytvoří grafiku jedné položky v seznamu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NoteViewHolder(binding)
    }

    // Naplní jednu položku daty z konkrétní poznámky
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]

        holder.binding.tvNoteTitle.text = currentNote.title
        holder.binding.tvNoteContent.text = currentNote.content

        // ID, kategorie a datum vytvoření na jednom řádku
        val date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            .format(Date(currentNote.createdAt))
        holder.binding.tvNoteInfo.text =
            "#" + currentNote.id + " · " + currentNote.category + " · " + date

        // Kliknutí na ikonu EDIT
        holder.binding.ivEdit.setOnClickListener {
            onEditClick(currentNote)
        }

        // Kliknutí na ikonu DELETE
        holder.binding.ivDelete.setOnClickListener {
            onDeleteClick(currentNote)
        }
    }

    // Počet poznámek, RecyclerView podle toho ví, kolik řádků vykreslit
    override fun getItemCount(): Int = notes.size

    // Nastaví nový seznam poznámek a překreslí ho
    fun submitList(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
