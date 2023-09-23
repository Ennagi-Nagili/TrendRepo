package com.annaginagili.trendrepo

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import com.annaginagili.FavoriteFragmentDirections
import com.annaginagili.trendrepo.adapter.FavAdapter
import com.annaginagili.trendrepo.database.AppDatabase
import com.annaginagili.trendrepo.databinding.FragmentDetailsBinding
import com.annaginagili.trendrepo.model.Favorite
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding
    val args by navArgs<DetailsFragmentArgs>()
    lateinit var profile: ImageView
    lateinit var repoName: TextView
    lateinit var ownerName: TextView
    lateinit var star: TextView
    lateinit var language: TextView
    lateinit var forks: TextView
    lateinit var date: TextView
    lateinit var link: TextView
    lateinit var description: TextView
    lateinit var fav: ImageView
    lateinit var db: AppDatabase
    //private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentDetailsBinding.inflate(inflater)
        profile = binding.profile
        repoName = binding.repoName
        ownerName = binding.ownerName
        star = binding.stars
        language = binding.language
        forks = binding.forks
        date = binding.date
        link = binding.link
        description = binding.description
        fav = binding.fav
        //viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]

        CoroutineScope(Dispatchers.IO).launch {
            db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "Favorite")
                .build()
            var favorite:Favorite? = null

            for (i in db.favDao().getAll()) {
                Log.e("hello", i.repo_id.toString())
                if (i.repo_id == args.details.id) {
                    favorite = i
                    fav.setBackgroundResource(R.drawable.baseline_favorite_24)
                }
            }

            fav.setOnClickListener {
                if (favorite == null) {
                    favorite = Favorite(0, args.details.id, args.details.owner.avatar_url?:"",
                        args.details.name, args.details.owner.login,
                        args.details.stargazers_count.toString(), args.details.description?:"",
                        args.details.language?:"", args.details.forks.toString(),
                        args.details.created_at?:"", args.details.html_url?:"")
                    CoroutineScope(Dispatchers.Default).launch {
                        db.favDao().addFav(favorite!!)
                    }
                    fav.setBackgroundResource(R.drawable.baseline_favorite_24)
                } else {
                    CoroutineScope(Dispatchers.Default).launch {
                        db.favDao().delete(favorite!!)
                    }
                    fav.setBackgroundResource(R.drawable.fav_empty)

                }
            }
        }

        Glide.with(requireActivity()).load(args.details.owner.avatar_url).into(profile)
        repoName.text = args.details.name
        ownerName.text = args.details.owner.login
        star.text = args.details.stargazers_count.toString()
        val lang = (args.details.language)?:"none"
        val langSpannable = SpannableString("Language: $lang")
        val bold = StyleSpan(Typeface.BOLD)
        langSpannable.setSpan(bold, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        language.text = langSpannable
        val forkSpannable = SpannableString("Number of forks: " + args.details.forks.toString())
        forkSpannable.setSpan(bold, 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        forks.text = forkSpannable
        val dateSpannable = SpannableString("Creation date: " + args.details.created_at)
        dateSpannable.setSpan(bold, 0, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        date.text = dateSpannable
        val linkSpannable = SpannableString("Github page link: " + args.details.html_url)
        linkSpannable.setSpan(bold, 0, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                Log.e("hello", "click")
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(args.details.html_url))
                requireActivity().startActivity(browserIntent)
            }
        }
        linkSpannable.setSpan(clickSpan, 17, linkSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        link.movementMethod = LinkMovementMethod.getInstance()
        val blueSpan = ForegroundColorSpan(Color.BLUE)
        linkSpannable.setSpan(blueSpan, 17, linkSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        link.text = linkSpannable
        description.text = args.details.description

        return binding.root
    }
}