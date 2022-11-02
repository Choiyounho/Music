package com.pdf.music

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pdf.music.databinding.ItemFolderBinding
import com.pdf.music.db.FolderEntity

class FolderAdapter(
    private val click: (String) -> Unit
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    var entities = emptyList<FolderEntity>()

    fun setItems(list: List<FolderEntity>) {
        entities = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        return FolderViewHolder(
            ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(entities[position])
    }

    override fun getItemCount(): Int {
        return entities.size
    }

    inner class FolderViewHolder(
        private val binding: ItemFolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(folderEntity: FolderEntity) {
            binding.root.setOnClickListener {
                click.invoke(folderEntity.name)
            }

            binding.folderName.text = folderEntity.name
        }

    }
}