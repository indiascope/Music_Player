package com.encora.musicplayer.ui.main.viewholder

import android.widget.ImageView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.encora.musicplayer.R
import com.encora.musicplayer.data.local.entity.SongEntity
import com.encora.musicplayer.databinding.ItemSongBinding

/**
 * [RecyclerView.ViewHolder] implementation to inflate View for RecyclerView.
 * See [SongListAdapter]]
 */
class SongViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(song: SongEntity, onItemClicked: (SongEntity, ImageView) -> Unit) {
        binding.songTitle.text = song.title
        binding.songCategoryName.text = HtmlCompat.fromHtml("Category: ${song.categoryName}",HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.songPrice.text = "Price : ${song.price}"
        binding.imageView.load(song.imageUrl) {
            placeholder(R.drawable.ic_photo)
            error(R.drawable.ic_broken_image)
        }

        binding.root.setOnClickListener {
            onItemClicked(song, binding.imageView)
        }
    }
}
