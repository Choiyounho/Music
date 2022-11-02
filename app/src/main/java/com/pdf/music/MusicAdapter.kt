package com.pdf.music

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pdf.music.Converter.stringToBitmap
import com.pdf.music.databinding.ItemMusicBinding
import com.pdf.music.db.MusicEntity

class MusicAdapter(
    val click: (MusicEntity) -> Unit
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    var entities = emptyList<MusicEntity>()

    fun setItems(list: List<MusicEntity>) {
        entities = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(
            ItemMusicBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bind(entities[position])
    }

    override fun getItemCount(): Int {
        return entities.size
    }

    inner class MusicViewHolder(
        private val binding: ItemMusicBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(musicEntity: MusicEntity) {
            try {
                binding.musicImage.setImageBitmap(stringToBitmap(musicEntity.thumbnail))
            } catch (e: Exception) {

            }

            binding.musicName.text = musicEntity.title

            binding.root.setOnClickListener {
                click.invoke(musicEntity)
            }
        }

    }

}