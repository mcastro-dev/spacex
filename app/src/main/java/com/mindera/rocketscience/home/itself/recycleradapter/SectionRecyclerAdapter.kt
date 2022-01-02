package com.mindera.rocketscience.home.itself.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mindera.rocketscience.databinding.ItemSectionBinding
import com.mindera.rocketscience.home.itself.model.UISection

class SectionRecyclerAdapter
    : ListAdapter<UISection, SectionRecyclerAdapter.SectionHeaderViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionHeaderViewHolder {
        return SectionHeaderViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SectionHeaderViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UISection>() {
            override fun areItemsTheSame(oldItem: UISection, newItem: UISection): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: UISection, newItem: UISection): Boolean {
                return oldItem == newItem
            }
        }
    }

    class SectionHeaderViewHolder(
        private val binding: ItemSectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: UISection) {
            binding.run {
                txtTitle.text = item.title
                progressHorizontal.visibility = if (item.isSectionLoading) View.VISIBLE else View.GONE

                txtEmpty.visibility = if (item.isEmpty && item.messageWhenEmpty != null && !item.isSectionLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                item.messageWhenEmpty?.let { message ->
                    txtEmpty.text = message
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup) : SectionHeaderViewHolder {
                val binding = ItemSectionBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return SectionHeaderViewHolder(binding)
            }
        }
    }
}