package com.example.mynotes.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mynotes.Models.Note
import com.example.mynotes.utilities.DATABASE_NAME


@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    //Singleton pattern in which we want a single object of this object created and if already the object is created
    //, then it will return the reference to the same object, so here we have created a variable named INSTANCE(of Note
    // Database) so whenever getDatabase function is called then it will check if the instance is null then it will
    //execute code from line 25 to 29, otherwise it will return the refernce to the same object that is that this
    //INSTANCE variable is pointing to

    //synchronized means that if one operation is being carried out on this class means no two operations should
    //be carried out at a single point of time
    companion object{
        @Volatile
        private var INSTANCE : NoteDatabase? = null

        fun getDatabase(context: Context):NoteDatabase{

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                ).build()

                 INSTANCE = instance

                instance
            }
        }
    }
}
