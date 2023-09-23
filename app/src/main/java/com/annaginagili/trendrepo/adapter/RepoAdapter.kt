package com.annaginagili.trendrepo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.annaginagili.trendrepo.R
import com.annaginagili.trendrepo.databinding.RepoLayoutBinding
import com.annaginagili.trendrepo.model.ItemsModel
import com.bumptech.glide.Glide

class RepoAdapter(private val context: Context, private val itemList: List<ItemsModel>):
    RecyclerView.Adapter<RepoAdapter.ItemHolder>() {
    private lateinit var listener: OnItemClickListener

    class ItemHolder(private val binding: RepoLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun setData(context: Context, item: ItemsModel, listener: OnItemClickListener) {
            if (item.owner.avatar_url != null) {
                Glide.with(context).load(item.owner.avatar_url).placeholder(R.drawable.baseline_account).into(binding.profile)
            }
            binding.repoName.text = item.name
            binding.ownerName.text = item.owner.login
            binding.star.text = item.stargazers_count.toString()
            if (item.description != null) {
                if (item.description.length > 200) {
                    binding.description.text = item.description.subSequence(0, 200).toString() + "..."
                } else {
                    binding.description.text = item.description
                }
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