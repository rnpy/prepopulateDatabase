package nz.co.trademe.prepopulatedatabase.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.View

class SingleLayoutViewHolder<in T>(view: View, private val update: View.(T) -> Unit) : RecyclerView.ViewHolder(view) {
    fun bind(item: T) = itemView.update(item)
}