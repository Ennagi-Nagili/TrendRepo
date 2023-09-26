package com.annaginagili.trendrepo.ui.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annaginagili.trendrepo.api.RetrofitClient
import com.annaginagili.trendrepo.model.ItemsModel
import com.annaginagili.trendrepo.model.RepoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class HomeFragmentViewModel: ViewModel() {
    private val loading = MutableLiveData<Boolean>()
    private val reposList = MutableLiveData<ArrayList<ItemsModel>>()
    private val list = ArrayList<ItemsModel>()

    fun getRepos(date: LocalDate, page: Int) {
        Log.e("hello", date.toString())
        RetrofitClient.getInstance().getApi().getLastDay(date.toString(), "stars",
            "desc", page.toString()).enqueue(object : Callback<RepoModel> {
            override fun onResponse(call: Call<RepoModel>, response: Response<RepoModel>) {
                if (response.body() != null) {
                    loading.postValue(false)
                    list += response.body()!!.items as ArrayList<ItemsModel>
                    reposList.postValue(list)
                }
            }

            override fun onFailure(call: Call<RepoModel>, t: Throwable) {
                Log.e("hello", t.message.toString())
            }
        })
    }

    fun observeLoading(): LiveData<Boolean> {
        return loading
    }

    fun observeRepoList(): LiveData<ArrayList<ItemsModel>> {
        return reposList
    }
}