package com.annaginagili.trendrepo.ui.fragments.details

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
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import com.annaginagili.trendrepo.R
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
    private lateinit var viewModel: DetailsViewModel

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
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]

        viewModel.getFaves(requireContext(), args.details.id)
        setObserver()

        fav.setOnClickListener {
            viewModel.addFav(args.details, requireContext(), args.details.id)
        }

        Glide.with(requireActivity()).load(args.details.owner.avatar_url).into(profile)

        repoName.text = args.details.name
        ownerName.text = args.details.owner.login
        star.text = args.details.stargazers_count.toString()

        val lang = (args.details.language)?:"none"
        setSpan("Language: $lang", 0, 9, language, null)

        val fork = args.details.forks.toString()
        setSpan("Number of forks: $fork", 0, 16, forks, null)

        val dateTxt = "Creation date: " + args.details.created_at
        setSpan(dateTxt, 0, 14, date, null)

        val linkTxt = "Github page link: " + args.details.html_url
        val clickSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                Log.e("hello", "click")
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(args.details.html_url))
                requireActivity().startActivity(browserIntent)
            }
        }
        setSpan(linkTxt, 0, 17, link, clickSpan)
        link.movementMethod = LinkMovementMethod.getInstance()
        description.text = args.details.description

        return binding.root
    }

    private fun setSpan(text: String, start: Int, end: Int, view: TextView, click: ClickableSpan?) {
        val spannable = SpannableString(text)
        val bold = StyleSpan(Typeface.BOLD)
        spannable.setSpan(bold, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (click != null) {
            spannable.setSpan(click, end, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val blueSpan = ForegroundColorSpan(Color.BLUE)
            spannable.setSpan(blueSpan, end, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        view.text = spannable
    }

    fun setObserver() {
        viewModel.observeFav().observe(viewLifecycleOwner) {
            if (it) {
                fav.setBackgroundResource(R.drawable.baseline_favorite_24)
            }

            else {
                fav.setBackgroundResource(R.drawable.fav_empty)
            }
        }
    }
}