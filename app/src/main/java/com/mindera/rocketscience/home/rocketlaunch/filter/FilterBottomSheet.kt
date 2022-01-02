package com.mindera.rocketscience.home.rocketlaunch.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mindera.rocketscience.common.presentation.MessageDisplayer
import com.mindera.rocketscience.common.presentation.viewmodel.ShowErrorCommand
import com.mindera.rocketscience.databinding.DialogFilterBinding
import com.mindera.rocketscience.home.rocketlaunch.filter.viewmodel.*
import com.mindera.rocketscience.rocketlaunch.domain.model.Filter
import com.mindera.rocketscience.rocketlaunch.domain.model.SortOrder
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable

@AndroidEntryPoint
class FilterBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "FilterBottomSheet"
        fun newInstance() = FilterBottomSheet()
    }

    private lateinit var commandsDisposable: Disposable

    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        commandsDisposable.dispose()
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUp()
        observeState()
        observeCommands()

        viewModel.onEvent(GetFiltersEvent())
    }

    private fun setUp() {
        binding.run {
            dialogYearPicker.run {
                btnNext.setOnClickListener {
                    viewModel.onEvent(NextYearEvent())
                }
                btnPrevious.setOnClickListener {
                    viewModel.onEvent(PreviousYearEvent())
                }
            }

            dialogMissionStatus.run {
                fun handleMissionStatusChange(status: Filter.MissionStatus, forRadioButton: RadioButton) {
                    forRadioButton.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) viewModel.onEvent(ChangeMissionStatusEvent(status))
                    }
                }

                handleMissionStatusChange(Filter.MissionStatus.ANY, forRadioButton = radioAny)
                handleMissionStatusChange(Filter.MissionStatus.SUCCESS, forRadioButton = radioSuccess)
                handleMissionStatusChange(Filter.MissionStatus.FAILURE, forRadioButton = radioFailure)
            }

            dialogSortOrder.run {
                fun handleSortOrderChange(sortOrder: SortOrder, forRadioButton: RadioButton) {
                    forRadioButton.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) viewModel.onEvent(ChangeSortOrderEvent(sortOrder))
                    }
                }

                handleSortOrderChange(SortOrder.DESCENDING, forRadioButton = radioDescending)
                handleSortOrderChange(SortOrder.ASCENDING, forRadioButton = radioAscending)
            }

            btnApply.setOnClickListener {
                viewModel.onEvent(ApplyFiltersEvent())
            }
        }
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            handleFilter(state.filter)
            handleIsApplyEnabled(state.isApplyEnabled)
            handleSavingStatus(state.savingStatus)
        }
    }

    private fun handleIsApplyEnabled(isApplyEnabled: Boolean) {
        binding.btnApply.isEnabled = isApplyEnabled
    }

    private fun handleFilter(filter: Filter) {
        handleYear(filter.rocketLaunchYear)
        handleMissionStatus(filter.missionStatus)
        handleSortOrder(filter.sortOrder)
    }

    private fun handleSavingStatus(savingStatus: FilterViewModel.State.SavingStatus) {
        if (savingStatus == FilterViewModel.State.SavingStatus.SAVED) {
            dismiss()
        }
    }

    private fun handleYear(year: Int) {
        binding.dialogYearPicker.txtYear.run {
            if (text != year.toString()) {
                text = year.toString()
            }
        }
    }

    private fun handleMissionStatus(missionStatus: Filter.MissionStatus) {
        binding.dialogMissionStatus.run {
            when(missionStatus) {
                Filter.MissionStatus.ANY -> handleRadioButtonCheck(radioAny)
                Filter.MissionStatus.SUCCESS -> handleRadioButtonCheck(radioSuccess)
                Filter.MissionStatus.FAILURE -> handleRadioButtonCheck(radioFailure)
            }
        }
    }

    private fun handleSortOrder(sortOrder: SortOrder) {
        binding.dialogSortOrder.run {
            when(sortOrder) {
                SortOrder.DESCENDING -> handleRadioButtonCheck(radioDescending)
                SortOrder.ASCENDING -> handleRadioButtonCheck(radioAscending)
            }
        }
    }

    private fun handleRadioButtonCheck(radioButton: RadioButton) {
        if (!radioButton.isChecked) {
            radioButton.isChecked = true
        }
    }

    private fun observeCommands() {
        commandsDisposable = viewModel.command.subscribe { command ->
            when(command) {
                /*
                 * matheus:
                 * FIXME: not ideal displaying the error message like this, in this case.
                 *  Display the error in a TextView, inside the BottomSheet itself.
                 */
                is ShowErrorCommand -> MessageDisplayer.show(command.error, forView = binding.root)
            }
        }
    }
}