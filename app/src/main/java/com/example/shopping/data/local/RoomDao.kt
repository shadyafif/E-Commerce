package com.example.shopping.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shopping.data.model.cartDetails.CartDatum
import com.example.shopping.data.model.productsDetails.ProductDatum

@Dao
interface RoomDao {
    //WishListDao
    @Query("SELECT * FROM ProductDetails")
    fun loadProducts(): LiveData<List<ProductDatum>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductFav(ProductEntry: ProductDatum)


    @Delete
    suspend fun deleteProductFav(ProductEntry: ProductDatum?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateProduct(ProductEntry: ProductDatum?)

    @Query("SELECT * FROM ProductDetails WHERE name = :name")
    fun fetchById(name: String?): ProductDatum?

    @Query("DELETE FROM ProductDetails")
    suspend fun deleteAll()

    //CartDao

    @Query("SELECT * FROM cartTable")
    fun loadCartProducts(): LiveData<List<CartDatum>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(ProductEntry: CartDatum)


    @Delete
    suspend fun deleteCartProduct(ProductEntry: CartDatum?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCartProduct(ProductEntry: CartDatum?)

    @Query("SELECT * FROM cartTable WHERE name = :name")
    fun fetchByCartName(name: String?): CartDatum?

    @Query("DELETE FROM cartTable")
    suspend fun deleteAllCart()

    @Query("SELECT Sum (quantity* price) FROM cartTable")
    fun getSum(): Int

    @Update
    fun updateQty(ProductEntry: CartDatum?)
}