package com.example.shopping.data.model.cartDetails

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cartTable")
data class CartDatum(
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
    val price: Float?,
    var quantity: Int? = 0
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(description)
        parcel.writeValue(discount)
        parcel.writeString(image)
        parcel.writeStringList(images)
        parcel.writeValue(in_cart)
        parcel.writeValue(in_favorites)
        parcel.writeString(name)
        parcel.writeValue(old_price)
        parcel.writeValue(price)
        parcel.writeValue(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartDatum> {
        override fun createFromParcel(parcel: Parcel): CartDatum {
            return CartDatum(parcel)
        }

        override fun newArray(size: Int): Array<CartDatum?> {
            return arrayOfNulls(size)
        }
    }
}