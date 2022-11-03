package com.pdf.music.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pdf.music.databinding.ItemBookmarkBinding
import com.pdf.music.db.BookmarkEntity

class BookmarkAdapter(
    val click: (BookmarkEntity) -> Unit
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    var entities = emptyList<BookmarkEntity>()

    fun setItems(list: List<BookmarkEntity>) {
        entities = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(
            ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(entities[position])
    }

    override fun getItemCount(): Int {
        return entities.size
    }

    inner class BookmarkViewHolder(
        val binding: ItemBookmarkBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookmarkEntity: BookmarkEntity) {
            binding.root.setOnClickListener {
                click.invoke(bookmarkEntity)
            }

            binding.page.text = bookmarkEntity.page.toString()
            binding.bookmark.text = bookmarkEntity.name
        }

    }

}