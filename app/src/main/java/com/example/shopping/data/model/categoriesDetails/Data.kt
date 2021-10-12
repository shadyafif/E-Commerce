package com.example.shopping.data.model.categoriesDetails

import android.os.Parcel
import android.os.Parcelable


data class Data(
    val current_page: Int,
    val data: List<CategoryDatum>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val path: String,
    val per_page: Int,
    val to: Int,
    val total: Int
) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(CategoryDatum)!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(current_page)
        parcel.writeTypedList(data)
        parcel.writeString(first_page_url)
        parcel.writeInt(from)
        parcel.writeInt(last_page)
        parcel.writeString(last_page_url)
        parcel.writeString(path)
        parcel.writeInt(per_page)
        parcel.writeInt(to)
        parcel.writeInt(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }
}

