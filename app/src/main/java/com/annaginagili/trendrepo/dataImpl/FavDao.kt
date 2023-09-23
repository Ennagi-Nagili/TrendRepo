package com.annaginagili.trendrepo.dataImpl

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.annaginagili.trendrepo.model.Favorite

@Dao
interface FavDao {
    @Query("Select * from Favorite")
    fun getAll(): List<Favorite>

    @Insert
    fun addFav(vararg fav: Favorite)

    @Delete
    fun delete(fav: Favorite)
}