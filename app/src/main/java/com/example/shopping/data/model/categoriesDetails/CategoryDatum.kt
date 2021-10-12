package com.example.shopping.data.model.categoriesDetails

import android.os.Parcel
import android.os.Parcelable

data class CategoryDatum(
    val id: Int,
    val image: String,
    val name: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryDatum> {
        override fun createFromParcel(parcel: Parcel): CategoryDatum {
            return CategoryDatum(parcel)
        }

        override fun newArray(size: Int): Array<CategoryDatum?> {
            return arrayOfNulls(size)
        }
    }
}
