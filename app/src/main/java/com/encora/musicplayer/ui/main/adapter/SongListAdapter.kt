package com.encora.musicplayer.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.encora.musicplayer.data.local.entity.SongEntity
import com.encora.musicplayer.databinding.ItemSongBinding
import com.encora.musicplayer.ui.main.viewholder.SongViewHolder

/**
 * Adapter class [RecyclerView.Adapter] for [RecyclerView] which binds [SongEntity] along with [SongViewHolder]
 * @param onItemClicked which will receive callback when item is clicked.
 */
class SongListAdapter(
    private val onItemClicked: (SongEntity, ImageView) -> Unit
) : ListAdapter<SongEntity, SongViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SongViewHolder(
        ItemSongBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClicked)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SongEntity>() {
            override fun areItemsTheSame(oldItem: SongEntity, newItem: SongEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SongEntity, newItem: SongEntity): Boolean =
                oldItem == newItem
        }
    }
}
