package com.example.myapp009amynotehub.data

import android.content.Context
import androidx.room3.Room
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

// Objekt, který zajistí, že v aplikaci bude jen jedna instance databáze (singleton)
object NoteHubDatabaseInstance {

    // @Volatile: změnu proměnné uvidí hned všechna vlákna
    @Volatile
    private var INSTANCE: NoteHubDatabase? = null

    // MIGRACE z verze 1 na 2: přidáme sloupce createdAt a category
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override suspend fun migrate(connection: SQLiteConnection) {
            // createdAt nesmí být NULL, proto DEFAULT
            connection.execSQL(
                "ALTER TABLE note_table ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0"
            )

            // category nesmí být NULL, proto DEFAULT
            connection.execSQL(
                "ALTER TABLE note_table ADD COLUMN category TEXT NOT NULL DEFAULT 'General'"
            )
        }
    }

    // Vrátí instanci databáze. Pokud ještě neexistuje, vytvoří ji.
    fun getDatabase(context: Context): NoteHubDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NoteHubDatabase::class.java,
                "notehub_database"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
            INSTANCE = instance
            instance
        }
    }
}
