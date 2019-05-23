package com.yogacahya.morsetranslator

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MorseDao {
    @Query("Select * from Morse where alphabet=:alphabet")
    fun selectMorseByAlphabet(alphabet: String): Morse

    @Query("Select * from Morse where morse=:morse")
    fun selectMorseByMorse(morse: String): Morse

    @Insert
    fun insertMorse(morse: Morse)

    @Query("Delete from Morse")
    fun deleteAll()

    @Query("Select * from Morse")
    fun checkMorse(): MutableList<Morse>
}