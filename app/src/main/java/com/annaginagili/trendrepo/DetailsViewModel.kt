package com.annaginagili.trendrepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.annaginagili.trendrepo.dataImpl.FavDao
import com.annaginagili.trendrepo.model.Favorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel() {
    private var favs = MutableLiveData<List<Favorite>>()

    fun getFaves(favDao: FavDao) {
        CoroutineScope(Dispatchers.Default).launch {
            val result = favDao.getAll()
            favs.postValue(result)
        }
    }

    fun addFav(favDao: FavDao, favorite: Favorite) {
        CoroutineScope(Dispatchers.Default).launch {
            favDao.addFav(favorite)
        }
    }

    fun observeFaves(): LiveData<List<Favorite>> {
        return favs
    }
}