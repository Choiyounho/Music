package com.pdf.music

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pdf.music.databinding.ItemFolderBinding
import com.pdf.music.db.FolderEntity

class FolderAdapter(
    private val folders: List<FolderEntity>,
    private val click: (String) -> Unit
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        return FolderViewHolder(
            ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(folders[position])
    }

    override fun getItemCount(): Int {
        return folders.size
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