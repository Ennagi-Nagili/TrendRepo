package com.annaginagili.trendrepo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.annaginagili.trendrepo.dataImpl.FavDao
import com.annaginagili.trendrepo.model.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favDao(): FavDao
}