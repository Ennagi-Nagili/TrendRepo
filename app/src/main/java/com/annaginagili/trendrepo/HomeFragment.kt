package com.annaginagili.trendrepo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.annaginagili.trendrepo.adapter.RepoAdapter
import com.annaginagili.trendrepo.api.RetrofitClient
import com.annaginagili.trendrepo.databinding.FragmentHomeBinding
import com.annaginagili.trendrepo.model.ItemsModel
import com.annaginagili.trendrepo.model.RepoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var repos: RecyclerView
    lateinit var spinner: Spinner
    var date: LocalDate = LocalDate.now()
    lateinit var reposList: ArrayList<ItemsModel>
    lateinit var adapter: RepoAdapter
    var page = 1
    lateinit var loading: ProgressBar
    var actualPosition = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater)
        repos = binding.repos
        spinner = binding.spinner
        reposList = ArrayList()
        loading = binding.loading

        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
            listOf("Last Day", "Last Week", "Last Month"))

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2) {
                    0 -> date = LocalDate.now()
                    1 -> date = LocalDate.now().minusWeeks(1)
                    2 -> date = LocalDate.now().minusMonths(1)
                }

                getRepos()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                getRepos()
            }
        }

        repos.setHasFixedSize(true)
        repos.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        repos.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val position = (repos.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                Log.e("hello", position.toString())

                if (position + 7 == reposList.size) {
                    getRepos()
                }
            }
        })

        binding.fav.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteFragment())
        }

        return binding.root
    }

    fun getRepos() {
        loading.visibility = View.VISIBLE

        RetrofitClient.getInstance().getApi().getLastDay(date.toString(), "stars",
            "desc", page.toString()).enqueue(object : Callback<RepoModel> {
            override fun onResponse(call: Call<RepoModel>, response: Response<RepoModel>) {
                if (response.body() != null) {
                    loading.visibility = View.INVISIBLE
                    reposList += response.body()!!.items as ArrayList<ItemsModel>
                    adapter = RepoAdapter(requireContext(), reposList)

                    adapter.setOnItemClickListener(object : RepoAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            Log.e("hello", position.toString())

                            findNavController().navigate(HomeFragmentDirections
                                .actionHomeFragmentToDetailsFragment(reposList[position]))
                        }
                    })

                    repos.adapter = adapter
                    page++
                    repos.scrollToPosition(actualPosition)
                    actualPosition = reposList.size-1
                    Log.e("hello", "size: " + reposList.size)
                }
            }

            override fun onFailure(call: Call<RepoModel>, t: Throwable) {
                Log.e("hello", t.message.toString())
            }
        })
    }
}