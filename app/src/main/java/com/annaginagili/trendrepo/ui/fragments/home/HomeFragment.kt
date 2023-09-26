package com.annaginagili.trendrepo.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.annaginagili.trendrepo.adapter.RepoAdapter
import com.annaginagili.trendrepo.databinding.FragmentHomeBinding
import com.annaginagili.trendrepo.model.ItemsModel
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
    lateinit var viewModel: HomeFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater)
        repos = binding.repos
        spinner = binding.spinner
        reposList = ArrayList()
        loading = binding.loading
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]

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

        setUpLoading()
        setUpRepoList()

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
        viewModel.getRepos(date, page)
    }

    private fun setUpLoading() {
        viewModel.observeLoading().observe(viewLifecycleOwner) {
            if (!it) {
                loading.visibility = View.INVISIBLE
            }
        }
    }

    private fun setUpRepoList() {
        viewModel.observeRepoList().observe(viewLifecycleOwner) {
            adapter = RepoAdapter(requireContext(), it)
            Log.e("hello", it.toString())

            adapter.setOnItemClickListener(object : RepoAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it[position]))
                }
            })

            repos.adapter = adapter
            page++
            repos.scrollToPosition(actualPosition)
            actualPosition = reposList.size-1
        }
    }
}