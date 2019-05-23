package com.yogacahya.morsetranslator

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Morse")
data class Morse(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "alphabet") val alphabet: String,
    @ColumnInfo(name = "morse") val morse: String
)