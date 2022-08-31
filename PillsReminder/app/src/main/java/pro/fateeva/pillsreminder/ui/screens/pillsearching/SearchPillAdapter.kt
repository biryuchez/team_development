package pro.fateeva.pillsreminder.ui.screens.pillsearching

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pro.fateeva.pillsreminder.databinding.ItemSearchPillBinding
import pro.fateeva.pillsreminder.domain.entity.DrugDomain

class SearchPillAdapter : RecyclerView.Adapter<SearchPillAdapter.SearchPillViewHolder>() {

    private var dataList = listOf<DrugDomain>()

    fun setData(dataList: List<DrugDomain>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPillViewHolder {
        return SearchPillViewHolder((ItemSearchPillBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
            .root)
    }

    override fun onBindViewHolder(holder: SearchPillViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size

    class SearchPillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(drugDomain: DrugDomain) {
            ItemSearchPillBinding.bind(itemView).apply {
                searchPillItemPillNameTextView.text = drugDomain.drugName
            }
        }
    }
}