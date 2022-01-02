package com.mindera.rocketscience.home.company.recycleradapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mindera.rocketscience.R
import com.mindera.rocketscience.home.company.model.UICompany
import com.mindera.rocketscience.databinding.ItemCompanyBinding

class CompanyRecyclerAdapter
    : ListAdapter<UICompany, CompanyRecyclerAdapter.CompanyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        return CompanyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UICompany>() {
            override fun areItemsTheSame(oldItem: UICompany, newItem: UICompany): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: UICompany, newItem: UICompany): Boolean {
                return oldItem == newItem
            }
        }
    }

    class CompanyViewHolder(
        private val binding: ItemCompanyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: UICompany) {
            binding.run {
                txtInfo.text = binding.root.resources.getString(
                    R.string.content_company_info,
                    item.name,
                    item.founder,
                    item.foundedYear,
                    item.employeesCount,
                    item.launchSitesCount,
                    item.valuation.value
                )
            }
        }

        companion object {
            fun create(parent: ViewGroup) : CompanyViewHolder {
                val binding = ItemCompanyBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return CompanyViewHolder(binding)
            }
        }
    }
}