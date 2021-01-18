package com.example.notely.db

import androidx.room.*

@Dao
interface NoteDAO {
    @Insert
    fun addNote(note: Note)

    @Query("select * from note")
    fun getAllNotes(): MutableList<Note>

    @Delete
    fun deleteNote(delNote: Note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(note: Note)
}