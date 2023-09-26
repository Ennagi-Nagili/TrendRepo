package com.annaginagili.trendrepo.ui.fragments.details

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.annaginagili.trendrepo.R
import com.annaginagili.trendrepo.dataImpl.FavDao
import com.annaginagili.trendrepo.database.AppDatabase
import com.annaginagili.trendrepo.model.Favorite
import com.annaginagili.trendrepo.model.ItemsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel() : ViewModel() {
    private val fav = MutableLiveData<Boolean>()
    private var favorite: Favorite? = null

    fun getFaves(context: Context, id: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            val db = Room.databaseBuilder(context, AppDatabase::class.java, "Favorite")
                .build()

            for (i in db.favDao().getAll()) {
                if (i.repo_id == id) {
                    favorite = i
                    fav.postValue(true)
                }
            }
        }
    }

    fun addFav(item: ItemsModel, context: Context, id: Int) {
        Log.e("hello", favorite.toString())
        CoroutineScope(Dispatchers.Default).launch {
            val db = Room.databaseBuilder(context, AppDatabase::class.java, "Favorite")
                .build()

            if (favorite == null) {
                val fv = Favorite(
                    0, item.id, item.owner.avatar_url ?: "", item.name,
                    item.owner.login, item.stargazers_count.toString(), item.description ?: "",
                    item.language ?: "", item.forks.toString(), item.created_at ?: "",
                    item.html_url ?: ""
                )

                db.favDao().addFav(fv)
                fav.postValue(true)
                getFaves(context, id)
            } else {
                db.favDao().delete(favorite!!)
                fav.postValue(false)
                favorite = null
                Log.e("hello", db.favDao().getAll().toString())
            }
        }
    }

    fun observeFav(): LiveData<Boolean> {
        return fav
    }
}