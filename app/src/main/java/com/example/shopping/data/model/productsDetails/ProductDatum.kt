package com.example.shopping.data.model.productsDetails

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductDetails")
data class ProductDatum(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String?,
    val discount: Int?,
    val image: String?,
    val images: List<String>?,
    val in_cart: Boolean?,
    val in_favorites: Boolean?,
    val name: String?,
    val old_price: Float?,
    val price: Float?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(description)
        parcel.writeInt(discount!!)
        parcel.writeString(image)
        parcel.writeStringList(images)
        parcel.writeByte(if (in_cart!!) 1 else 0)
        parcel.writeByte(if (in_favorites!!) 1 else 0)
        parcel.writeString(name)
        parcel.writeFloat(old_price!!)
        parcel.writeFloat(price!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductDatum> {
        override fun createFromParcel(parcel: Parcel): ProductDatum {
            return ProductDatum(parcel)
        }

        override fun newArray(size: Int): Array<ProductDatum?> {
            return arrayOfNulls(size)
        }
    }
}