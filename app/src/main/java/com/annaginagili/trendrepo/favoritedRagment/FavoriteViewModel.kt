package com.annaginagili.trendrepo.favoritedRagment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annaginagili.trendrepo.dataImpl.FavDao
import com.annaginagili.trendrepo.model.Favorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel: ViewModel() {
    private var favs = MutableLiveData<List<Favorite>>()

    fun getFaves(favDao: FavDao) {
        CoroutineScope(Dispatchers.Default).launch {
            val result = favDao.getAll()
            favs.postValue(result)
        }
    }

    fun observeFaves(): LiveData<List<Favorite>> {
        return favs
    }
}