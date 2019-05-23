package com.yogacahya.morsetranslator

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Morse::class), version = 1)
abstract class MorseDatabase : RoomDatabase() {
    abstract fun morseDao(): MorseDao

    companion object {
        private var INSTANCE: MorseDatabase? = null

        fun getInstance(c: Context): MorseDatabase? {
            if (INSTANCE == null) {
                synchronized(MorseDatabase::class.java) {
                    INSTANCE =
                        Room.databaseBuilder(c, MorseDatabase::class.java, "morse.db").allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroy() {
            INSTANCE = null
        }
    }
}