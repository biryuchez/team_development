package pro.fateeva.pillsreminder.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.google.android.material.card.MaterialCardView
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.databinding.FragmentHistoryCalendarBinding
import pro.fateeva.pillsreminder.databinding.HistoryCalendarItemBinding

class HistoryCalendarFragment :
    BaseFragment<FragmentHistoryCalendarBinding>(FragmentHistoryCalendarBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rowsCount = 5
        val columnsCount = 3

        repeat(rowsCount * columnsCount) {
            val newViewBinding =
                HistoryCalendarItemBinding.inflate(LayoutInflater.from(requireContext()))
            val newView = newViewBinding.root
            newView.id = it + 1

            newViewBinding.historyCalendarItemCard.setOnClickListener {
                binding.calendarContainer.children.iterator().forEachRemaining { parentView ->
                    parentView.findViewById<MaterialCardView>(R.id.history_calendar_item_card)
                        .setCardBackgroundColor(requireContext().getColor(R.color.app_primary))
                }
                newViewBinding.historyCalendarItemCard.setCardBackgroundColor(requireContext().getColor(
                    R.color.teal_200))
            }

            val params = ConstraintLayout.LayoutParams(0, 0)
            params.dimensionRatio = "W,1:1"
            newView.layoutParams = params

            if (it == 0) {
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                params.endToStart = newView.id + 1
                params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            } else {
                when (it % rowsCount) {
                    0 -> {
                        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        params.endToStart = newView.id + 1
                        params.topToBottom = newView.id - rowsCount
                    }

                    in (1..rowsCount - 2) -> {
                        params.startToEnd = newView.id - 1
                        params.endToStart = newView.id + 1
                        params.topToTop = newView.id - 1
                    }

                    (rowsCount - 1) -> {
                        params.startToEnd = newView.id - 1
                        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                        params.topToTop = newView.id - 1
                    }
                }
            }

            binding.calendarContainer.addView(newView)
        }
    }
}