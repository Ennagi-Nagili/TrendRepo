package com.annaginagili.trendrepo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annaginagili.trendrepo.R
import com.annaginagili.trendrepo.databinding.RepoLayoutBinding
import com.annaginagili.trendrepo.model.Favorite
import com.annaginagili.trendrepo.model.ItemsModel
import com.bumptech.glide.Glide

class FavAdapter(private val context: Context, private val itemList: List<Favorite>):
    RecyclerView.Adapter<FavAdapter.ItemHolder>() {
    private lateinit var listener: OnItemClickListener

    class ItemHolder(private val binding: RepoLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun setData(context: Context, item: Favorite, listener: OnItemClickListener) {
            Glide.with(context).load(item.image).placeholder(R.drawable.baseline_account).into(binding.profile)
            binding.repoName.text = item.repo_name
            binding.ownerName.text = item.owner_name
            binding.star.text = item.star
            if (item.description.length > 200) {
                binding.description.text = item.description.subSequence(0, 200).toString() + "..."
            } else {
                binding.description.text = item.description
            }

            binding.root.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = RepoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(context, itemList[position], listener)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}