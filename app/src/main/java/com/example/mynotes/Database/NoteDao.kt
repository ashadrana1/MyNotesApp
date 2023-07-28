package com.example.mynotes.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mynotes.Models.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)
    //we have used here replace coz if there is already an entry jisko hm insert krr rha hain, to fir nyi entry fir replace
    //krdegi old entry ko

    @Delete
    suspend fun delete(note: Note)

    @Query("Select * from notes_table order by id asc")
    fun getAllNotes() : LiveData<List<Note>>

    @Query("UPDATE notes_table SET title = :title, note = :note where id = :id")
    suspend fun update(id: Int?, title : String?, note: String?)
}
