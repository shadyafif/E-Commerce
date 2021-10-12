package com.example.shopping.data.model.productsDetails

import android.os.Parcel
import android.os.Parcelable

data class ProductModel(
    val `data`: Data,
    val message: String,
    val status: Boolean
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Data::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<ProductModel> {
        override fun createFromParcel(parcel: Parcel): ProductModel {
            return ProductModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductModel?> {
            return arrayOfNulls(size)
        }
    }
}
