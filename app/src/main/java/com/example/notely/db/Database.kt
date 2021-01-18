package com.example.notely.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.notely.db.Database as db
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 1
)
abstract class Database: RoomDatabase() {

    abstract fun getNoteDao(): NoteDAO

    companion object{
        @Volatile private var instance : db? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            db::class.java,
            "notesDB"
        ).allowMainThreadQueries().build()

    }
}