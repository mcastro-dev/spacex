package com.mindera.rocketscience.home.rocketlaunch.itself.recycleradapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mindera.rocketscience.R
import com.mindera.rocketscience.common.presentation.imageloading.ImageLoader
import com.mindera.rocketscience.databinding.ItemRocketLaunchBinding
import com.mindera.rocketscience.home.rocketlaunch.itself.model.UIRocketLaunch
import com.mindera.rocketscience.rocketlaunch.domain.model.RocketLaunch

typealias RocketLaunchItemClickCallback = (UIRocketLaunch) -> Unit

class RocketLaunchRecyclerAdapter(
    private val onRocketLaunchItemClickListener: RocketLaunchItemClickCallback? = null
) : ListAdapter<UIRocketLaunch, RocketLaunchRecyclerAdapter.RocketLaunchesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketLaunchesViewHolder {
        return RocketLaunchesViewHolder.create(parent, onRocketLaunchItemClickListener)
    }

    override fun onBindViewHolder(holder: RocketLaunchesViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UIRocketLaunch>() {
            override fun areItemsTheSame(oldItem: UIRocketLaunch, newItem: UIRocketLaunch): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UIRocketLaunch, newItem: UIRocketLaunch): Boolean {
                return oldItem == newItem
            }
        }
    }

    class RocketLaunchesViewHolder(
        private val binding: ItemRocketLaunchBinding,
        private val onClickListener: RocketLaunchItemClickCallback? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: UIRocketLaunch) {
            binding.run {
                val res = root.resources

                txtMissionName.text = item.missionName
                txtDateTime.text = item.launchDate
                txtRocket.text = res.getString(R.string.content_rocket, item.rocketName, item.rocketType)
                
                item.absoluteDaysDiffFromToday.toInt().let { daysDiff ->
                    txtDays.text = res.getQuantityString(R.plurals.plural_content_days, daysDiff, daysDiff)
                }

                labelDays.text = if (item.didAlreadyLaunch) {
                    res.getString(R.string.label_launch_days_since)
                } else {
                    res.getString(R.string.label_launch_days_from)
                }

                when(item.status) {
                    RocketLaunch.Status.SUCCESS -> R.drawable.ic_baseline_check_32
                    RocketLaunch.Status.FAILURE -> R.drawable.ic_baseline_close_32
                    else -> 0
                }.let { iconResId ->
                    imgSuccessfulLaunch.setImageResource(iconResId)
                }

                item.patchImageLink?.let { link ->
                    ImageLoader.load(link, imgMissionPatch)
                }

                root.setOnClickListener {
                    onClickListener?.invoke(item)
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup, onClickListener: RocketLaunchItemClickCallback? = null) : RocketLaunchesViewHolder {
                val binding = ItemRocketLaunchBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return RocketLaunchesViewHolder(binding, onClickListener)
            }
        }
    }
}