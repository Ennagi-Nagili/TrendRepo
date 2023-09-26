package com.annaginagili.trendrepo.ui.fragments.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.annaginagili.trendrepo.dataImpl.FavDao
import com.annaginagili.trendrepo.database.AppDatabase
import com.annaginagili.trendrepo.model.Favorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel: ViewModel() {
    private var favs = MutableLiveData<List<Favorite>>()

    fun getFaves(context: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            val db = Room.databaseBuilder(context, AppDatabase::class.java, "Favorite")
                .build()
            favs.postValue(db.favDao().getAll())
        }
    }

    fun observeFaves(): LiveData<List<Favorite>> {
        return favs
    }
}