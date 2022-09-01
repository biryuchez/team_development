package pro.fateeva.pillsreminder.ui.screens.pillsearching

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import pro.fateeva.pillsreminder.databinding.FragmentSearchPillBinding
import pro.fateeva.pillsreminder.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.ui.screens.BaseFragment

private const val DEFAULT_DEBOUNCE = 500L
private const val DEFAULT_STATEFLOW_VALUE = "-1"

class SearchPillFragment :
    BaseFragment<FragmentSearchPillBinding>(FragmentSearchPillBinding::inflate) {

    private val pillSearchingViewModel by viewModels<SearchPillViewModel>()
    private val queryFlow = MutableStateFlow(DEFAULT_STATEFLOW_VALUE)
    private val textListener = TextTypeListener()
    private val searchPillAdapter = SearchPillAdapter(object : SearchItemClickListener {
        override fun onSearchItemClick(drugDomain: DrugDomain) {
            onItemClick(drugDomain)
        }
    })

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pillSearchingRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchPillAdapter
        }

        pillSearchingViewModel.getData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        textListener.onTypeTextListener(binding.searchEditText, queryFlow)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                queryFlow
                    .filter { it != DEFAULT_STATEFLOW_VALUE }
                    .debounce(DEFAULT_DEBOUNCE)
                    .collect {
                        pillSearchingViewModel.searchPills(it)
                    }
            }
        }
    }

    private fun onItemClick(drugDomain: DrugDomain) {
        navigator.navigateToEventFrequencyScreen(drugDomain.drugName)
    }

    private fun renderData(state: SearchPillState) {
        when (state) {
            SearchPillState.Loading -> {
                binding.pillsSearchingProgressBar.isVisible = true
            }
            is SearchPillState.Success -> {
                searchPillAdapter.setData(state.dataList)
                binding.pillsSearchingProgressBar.isVisible = false
            }
            is SearchPillState.Error -> {
                Snackbar.make(binding.root, state.error, Snackbar.LENGTH_SHORT).show()
                binding.pillsSearchingProgressBar.isVisible = false
            }
        }
    }
}