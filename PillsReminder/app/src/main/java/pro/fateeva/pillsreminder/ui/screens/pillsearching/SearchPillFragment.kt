package pro.fateeva.pillsreminder.ui.screens.pillsearching

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import pro.fateeva.pillsreminder.databinding.FragmentSearchPillBinding
import pro.fateeva.pillsreminder.ui.BaseFragment

private const val DEFAULT_DEBOUNCE = 500L
private const val DEFAULT_STATEFLOW_VALUE = "-1"

class SearchPillFragment :
    BaseFragment<FragmentSearchPillBinding>(FragmentSearchPillBinding::inflate) {

    private val queryFlow = MutableStateFlow(DEFAULT_STATEFLOW_VALUE)
    private val textListener = TextTypeListener()
    
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textListener.onTypeTextListener(binding.searchEditText, queryFlow)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                queryFlow
                    .filter { it != DEFAULT_STATEFLOW_VALUE }
                    .debounce(DEFAULT_DEBOUNCE)
                    .collect {
                        // запрос в сеть через вьюМодель
                    }
            }
        }
    }
}