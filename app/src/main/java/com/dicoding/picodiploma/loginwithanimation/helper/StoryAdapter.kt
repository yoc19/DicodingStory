package com.dicoding.picodiploma.loginwithanimation.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.local.entity.StoryEntity
import com.dicoding.picodiploma.loginwithanimation.databinding.CardStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StoryAdapter :
    PagingDataAdapter<StoryEntity, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    inner class MyViewHolder(private val binding: CardStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryEntity) {
            with(binding) {
                tvItemName.text = story.name
                Glide.with(itemView.context).load(story.photoUrl).apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    ).into(ivItemPhoto)
                val dateString = story.createdAt
                val formattedDate = parseDateString(dateString)
                tvCreatedAt.text = formattedDate

                itemView.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivProfile, "profile"),
                            Pair(tvItemName, "name"),
                            Pair(ivItemPhoto, "photo"),
                            Pair(tvCreatedAt,"time")
                        )
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.STORY_ID, story.id)
                    itemView.context.startActivity(intent,optionsCompat.toBundle())

                }
            }

        }

        private fun parseDateString(dateString: String): String? {
            val inputFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()
            )
            val outputFormat = SimpleDateFormat("dd-MM-yyyy | HH:mm", Locale.getDefault())
            val date: Date? = inputFormat.parse(dateString)
            return date?.let { outputFormat.format(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: CardStoryBinding =
            CardStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryEntity> =
            object : DiffUtil.ItemCallback<StoryEntity>() {
                override fun areItemsTheSame(
                    oldItem: StoryEntity, newItem: StoryEntity
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: StoryEntity, newItem: StoryEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}