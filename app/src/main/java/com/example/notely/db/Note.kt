package com.example.notely.db


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
@Entity
@Parcelize
@Keep class Note(val title: String, val note: String, var isRemainder: Boolean, var remainderTime: Long, val noteDate:String): Parcelable {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}