package com.example.shopping.data.model.productsDetails

import android.os.Parcel
import android.os.Parcelable

data class Data(
    val current_page: Int,
    val `data`: List<ProductDatum>?,
    val first_page_url: String?,
    val from: Int?,
    val last_page: Int?,
    val last_page_url: String?,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Int,
    val to: Int?,
    val total: Int?
) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(ProductDatum),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(current_page)
        parcel.writeTypedList(data)
        parcel.writeString(first_page_url)
        parcel.writeValue(from)
        parcel.writeValue(last_page)
        parcel.writeString(last_page_url)
        parcel.writeString(path)
        parcel.writeInt(per_page)
        parcel.writeValue(to)
        parcel.writeValue(total)
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