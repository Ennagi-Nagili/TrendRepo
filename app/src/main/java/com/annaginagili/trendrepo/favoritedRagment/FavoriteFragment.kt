package com.annaginagili.trendrepo.favoritedRagment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.annaginagili.FavoriteFragmentDirections
import com.annaginagili.trendrepo.adapter.FavAdapter
import com.annaginagili.trendrepo.database.AppDatabase
import com.annaginagili.trendrepo.databinding.FragmentFavoriteBinding
import com.annaginagili.trendrepo.model.Favorite
import com.annaginagili.trendrepo.model.ItemsModel
import com.annaginagili.trendrepo.model.OwnerModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    lateinit var binding: FragmentFavoriteBinding
    lateinit var db: AppDatabase
    lateinit var repos: RecyclerView
    lateinit var adapter: FavAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        repos = binding.repos
        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        repos.setHasFixedSize(true)
        repos.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        CoroutineScope(Dispatchers.IO).launch {
            db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "Favorite")
                .build()
            viewModel.getFaves(db.favDao())
        }

        setUpObservers()

        return binding.root
    }

    fun favToItem(fav: Favorite): ItemsModel {
        return ItemsModel(
            fav.repo_id, fav.repo_name, OwnerModel(fav.owner_name, fav.image),
            fav.description, fav.star.toInt(), fav.language, fav.forks.toInt(), fav.date, fav.link
        )
    }

    private fun setUpObservers(){
        viewModel.observeFaves().observe(viewLifecycleOwner) { favs ->
            val adapter = FavAdapter(requireContext(), favs)
            repos.adapter = adapter
            adapter.setOnItemClickListener(object : FavAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    findNavController().navigate(
                        FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(
                            favToItem(favs[position])
                        )
                    )
                }
            })
        }
    }
}