package com.example.shopping.data.model.categoriesDetails


import android.os.Parcel
import android.os.Parcelable


data class MainModel(
    val `data`: Data,
    val message: String,
    val status: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Data::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(data, flags)
        parcel.writeString(message)
        parcel.writeByte(if (status) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainModel> {
        override fun createFromParcel(parcel: Parcel): MainModel {
            return MainModel(parcel)
        }

        override fun newArray(size: Int): Array<MainModel?> {
            return arrayOfNulls(size)
        }
    }
}