package nz.co.trademe.prepopulatedatabase.recyclerview

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class SingleLayoutAdapter<T>(private @LayoutRes val layoutRes: Int, private val items: List<T>, private val bind: View.(T) -> Unit)
    : RecyclerView.Adapter<SingleLayoutViewHolder<T>>() {
    override fun onBindViewHolder(holder: SingleLayoutViewHolder<T>, position: Int) = holder.bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SingleLayoutViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false), bind)

    override fun getItemCount() = items.size
}