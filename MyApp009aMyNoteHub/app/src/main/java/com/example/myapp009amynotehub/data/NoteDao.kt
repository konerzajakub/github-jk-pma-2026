package com.example.myapp009amynotehub.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // CREATE - vloží novou poznámku do databáze
    @Insert
    suspend fun insert(note: Note)

    // READ - načte všechny poznámky jako Flow, takže se seznam sám obnoví při změně
    @Query("SELECT * FROM note_table ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>

    // READ jedné poznámky podle ID (pro editaci)
    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    // UPDATE - uloží změny v existující poznámce
    @Update
    suspend fun update(note: Note)

    // DELETE - smaže poznámku
    @Delete
    suspend fun delete(note: Note)

    // SEARCH - hledá v názvu nebo v obsahu poznámky
    @Query("""
        SELECT *
        FROM note_table
        WHERE title LIKE '%' || :search || '%'
           OR content LIKE '%' || :search || '%'
        ORDER BY id DESC
    """)
    fun searchNotes(search: String): Flow<List<Note>>
}
