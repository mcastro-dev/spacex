package com.mindera.rocketscience.home.itself

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.mindera.rocketscience.R
import com.mindera.rocketscience.common.presentation.MessageDisplayer
import com.mindera.rocketscience.common.presentation.recyclerview.EndlessScrollListener
import com.mindera.rocketscience.common.presentation.viewmodel.ShowErrorCommand
import com.mindera.rocketscience.databinding.FragmentHomeBinding
import com.mindera.rocketscience.home.rocketlaunch.filter.FilterBottomSheet
import com.mindera.rocketscience.home.company.model.UICompany
import com.mindera.rocketscience.home.rocketlaunch.itself.model.UIRocketLaunch
import com.mindera.rocketscience.home.itself.model.UISection
import com.mindera.rocketscience.home.company.recycleradapter.CompanyRecyclerAdapter
import com.mindera.rocketscience.home.itself.model.UISectionFactory
import com.mindera.rocketscience.home.itself.recycleradapter.SectionRecyclerAdapter
import com.mindera.rocketscience.home.itself.viewmodel.GetInitialDataEvent
import com.mindera.rocketscience.home.itself.viewmodel.GetNextRocketLaunchesEvent
import com.mindera.rocketscience.home.itself.viewmodel.HomeViewModel
import com.mindera.rocketscience.home.itself.viewmodel.RefreshEvent
import com.mindera.rocketscience.home.rocketlaunch.itself.RocketLaunchBottomSheet
import com.mindera.rocketscience.home.rocketlaunch.itself.recycleradapter.RocketLaunchRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var commandsDisposable: Disposable

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sectionCompanyAdapter = SectionRecyclerAdapter()
    private val sectionRocketLaunchesAdapter = SectionRecyclerAdapter()
    private val companyAdapter = CompanyRecyclerAdapter()
    private val launchesAdapter = RocketLaunchRecyclerAdapter {
        showRocketLaunchItemDialog(it)
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUp()
        observeState()
        observeCommands()

        viewModel.onEvent(GetInitialDataEvent())
    }

    override fun onDestroyView() {
        commandsDisposable.dispose()
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_filter -> {
                showFilterDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUp() {
        setUpRecyclerView()

        updateSection(
            UISectionFactory.create(UISection.Type.COMPANY, resources).copy(isSectionLoading = true),
            forAdapter = sectionCompanyAdapter
        )
        updateSection(
            UISectionFactory.create(UISection.Type.ROCKET_LAUNCHES, resources).copy(isSectionLoading = true),
            forAdapter = sectionRocketLaunchesAdapter
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.onEvent(RefreshEvent())
        }
    }

    private fun setUpRecyclerView() {
        binding.rcyContent.apply {
            adapter = ConcatAdapter(
                sectionCompanyAdapter,
                companyAdapter,
                sectionRocketLaunchesAdapter,
                launchesAdapter
            )
            val linearLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = linearLayoutManager

            addOnScrollListener(EndlessScrollListener(
                layoutManager = linearLayoutManager,
                pageSize = viewModel.pageSize,
                onLoadMore = { viewModel.onEvent(GetNextRocketLaunchesEvent()) },
                isLoading = { viewModel.isLoadingMoreItems },
                hasMoreItems = { !viewModel.reachedLastPage }
            ))
        }
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            handleCompanyInfo(state.company)
            handleLoadingCompanyInfo(state.isLoadingCompanyInfo)
            handleRocketLaunches(state.rocketLaunches)
            handleRocketLaunchesSection(
                isEmpty = state.rocketLaunches.isEmpty(),
                isLoading = state.isLoadingRocketLaunches
            )
            handleRefreshing(state.isRefreshing)
            handleStaleData(state.hasStaleData)
        }
    }

    private fun observeCommands() {
        commandsDisposable = viewModel.command.subscribe { command ->
            when(command) {
                is ShowErrorCommand -> showError(command.error)
            }
        }
    }

    private fun handleRocketLaunchesSection(isEmpty: Boolean, isLoading: Boolean) {
        val section = UISectionFactory.create(UISection.Type.ROCKET_LAUNCHES, resources)
            .copy(isEmpty = isEmpty, isSectionLoading = isLoading)
        updateSection(
            section,
            forAdapter = sectionRocketLaunchesAdapter
        )
    }

    private fun handleLoadingCompanyInfo(loadingCompanyInfo: Boolean) {
        updateSection(
            UISectionFactory.create(UISection.Type.COMPANY, resources).copy(isSectionLoading = loadingCompanyInfo),
            forAdapter = sectionCompanyAdapter
        )
    }

    private fun handleRefreshing(isRefreshing: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = isRefreshing
    }

    private fun handleCompanyInfo(company: UICompany?) {
        if (company == null) {
            companyAdapter.submitList(emptyList())
            return
        }
        companyAdapter.submitList(listOf(company))
    }

    private fun handleRocketLaunches(rocketLaunches: List<UIRocketLaunch>) {
        launchesAdapter.submitList(rocketLaunches)
    }

    private fun handleStaleData(hasStaleData: Boolean) {
        binding.bannerStaleData.visibility = if (hasStaleData) View.VISIBLE else View.GONE
    }

    private fun updateSection(value: UISection, forAdapter: SectionRecyclerAdapter) {
        forAdapter.submitList(listOf(value))
    }

    private fun showError(error: Throwable) {
        MessageDisplayer.show(error, forView = binding.root)
    }

    private fun showRocketLaunchItemDialog(item: UIRocketLaunch) {
        RocketLaunchBottomSheet
            .newInstance(
                item.missionName,
                item.articleLink,
                item.wikipediaLink,
                item.videoLink
            )
            .show(parentFragmentManager, RocketLaunchBottomSheet.TAG)
    }

    private fun showFilterDialog() {
        FilterBottomSheet
            .newInstance()
            .show(parentFragmentManager, FilterBottomSheet.TAG)
    }
}