package com.example.shopping.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shopping.data.model.cartDetails.CartDatum
import com.example.shopping.data.model.productsDetails.ProductDatum


@Database(entities = [ProductDatum::class, CartDatum::class], version = 2, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRoomDao(): RoomDao

}
