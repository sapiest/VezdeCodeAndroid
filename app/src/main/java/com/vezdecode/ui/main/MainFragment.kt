package com.vezdecode.ui.main

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.vezdecode.R
import com.vezdecode.RootActivity
import com.vezdecode.data.remote.model.DescriptionModel
import com.vezdecode.data.remote.model.IncidentModel
import com.vezdecode.data.remote.model.SystemModel
import com.vezdecode.data.remote.repository.IncidentsRepository
import com.vezdecode.databinding.MainFragmentBinding
import com.vezdecode.ui.main.adapter.IncidentsAdapter
import com.vezdecode.ui.main.adapter.SearchAdapter
import com.vezdecode.ui.main.bottomsheets.DateBottomSheetFragment
import com.vezdecode.ui.main.bottomsheets.SystemBottomSheetFragment
import com.vezdecode.utils.DateUtils
import com.vezdecode.utils.gone
import com.vezdecode.utils.visible
import com.vezdecode.viewmodels.IncidentsViewModel
import com.vezdecode.viewmodels.factory.ViewModelFactory


class MainFragment : Fragment(), SystemBottomSheetFragment.SystemBottomSheetListener,
    DateBottomSheetFragment.DateBottomSheetListener {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IncidentsViewModel by activityViewModels {
        ViewModelFactory(
            IncidentsRepository()
        )
    }

    private var allIncidents = listOf<IncidentModel>()

    private var selectedSystems = listOf<SystemModel>()
    private var selectedDates: Pair<Long, Long>? = null

    private fun filteringList(filterBySystem: Boolean, filterByDate: Boolean) {
        var list = allIncidents
        if (filterBySystem && selectedSystems.isNotEmpty()) {
            list =
                list.filter { item -> selectedSystems.find { it.systemName == item.extSysName && it.isActive } != null }
        }
        if (filterByDate) {
            list = list.filter { item ->
                DateUtils.stringDateToLong(item.isKnowErrorDate) >= selectedDates?.first!! && DateUtils.stringDateToLong(
                    item.targetFinish
                ) <= selectedDates?.second!!
            }
        }
        (binding.rvIncidents.adapter as ListAdapter<IncidentModel, *>).submitList(list)
        val newSearchlist = list.map {
            DescriptionModel.createFromIncident(
                it
            )
        }
        (binding.rvSearchResults.adapter as SearchAdapter).apply {
            submitList(ArrayList(newSearchlist))
            saveList(newSearchlist)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.updateIncidents()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setUpAllBottomSheets()
        bindViews()
    }

    private fun bindViews() {
        initToolbar()
        setObservers()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.rvIncidents.apply {
            // Set Layout manager
            this.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            // Set RecyclerViewAdapter
            this.adapter = IncidentsAdapter(R.layout.incident_item_list, viewModel::onClick)
        }

        binding.rvSearchResults.apply {
            this.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            this.adapter = SearchAdapter(R.layout.search_item_list) { item ->
                (binding.rvIncidents.adapter as? ListAdapter<IncidentModel, *>)?.currentList?.let { list ->
                    val elem = list.find { it.ticketId == item.id }
                    elem?.let {
                        viewModel.onClick(it)
                    }
                }
            }
            this.itemAnimator = null
        }

        binding.tvSystemCategory.root.setOnClickListener {
            val list = ArrayList(allIncidents.map { SystemModel.createFromIncident(it) })
            Log.e("allSystems", list.toString())
            Log.e("curSystems", selectedSystems.toString())
            selectedSystems.forEach { systemModel ->
                val elem = list.find { it.systemName == systemModel.systemName }
                elem?.let {
                    val index = list.indexOf(elem)
                    list[index] = elem.copy(isActive = systemModel.isActive)
                }
            }
            Log.e("newsList", list.toString())
            val modalbottomSheetFragment = SystemBottomSheetFragment.newInstance(list, this)
            modalbottomSheetFragment.show(
                activity?.supportFragmentManager!!,
                modalbottomSheetFragment.tag
            )
        }

        binding.tvDateCategory.root.setOnClickListener {
            val modalbottomSheetFragment = DateBottomSheetFragment.newInstance(
                DateUtils.stringDateFromLong(selectedDates?.first),
                DateUtils.stringDateFromLong(selectedDates?.second),
                this
            )
            modalbottomSheetFragment.show(
                activity?.supportFragmentManager!!,
                modalbottomSheetFragment.tag
            )
        }
    }

    private fun setOnTextViewSelected(view: TextView, isSelected: Boolean) {
        if (isSelected) {
            view.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_gray_button)
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            view.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_red_button)
            view.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.holo_red_dark
                )
            )
        }
    }

    private fun initToolbar() {
        (requireActivity() as RootActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.title = getString(R.string.incidents_title)
        setHasOptionsMenu(true)
    }

    private fun setObservers() {
        viewModel.incidents.observe(viewLifecycleOwner, Observer { viewState ->
            if (!viewState.isLoading()) {
                viewState.data?.let {
                    (binding.rvIncidents.adapter as ListAdapter<IncidentModel, *>).submitList(it)
                    val newSearchList = it.map { DescriptionModel.createFromIncident(it) }
                    (binding.rvSearchResults.adapter as SearchAdapter).apply {
                        submitList(ArrayList(newSearchList))
                        saveList(newSearchList)
                    }

                    allIncidents = it
                    selectedSystems = it.map { SystemModel.createFromIncident(it) }
                }
            }
        })
        viewModel.goToDetailIncidentScreen.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { incidentModel ->
                val bundle = bundleOf("incident" to incidentModel)

                Navigation.findNavController(requireView()).navigate(
                    R.id.action_mainFragment_to_incidentFragment, bundle
                )
            }
        })

        viewModel.selectedCategory.observe(viewLifecycleOwner, Observer {
            val systemCategorySelected = it[SYSTEM_CATEGORY_INDEX]
            val dateCategorySelected = it[DATE_CATEGORY_INDEX]
            setOnTextViewSelected(binding.tvDateCategory.root as TextView, dateCategorySelected)
            setOnTextViewSelected(binding.tvSystemCategory.root as TextView, systemCategorySelected)
            filteringList(systemCategorySelected, dateCategorySelected)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.rvSearchResults.visible()
                binding.rvIncidents.foreground =
                    ColorDrawable(ContextCompat.getColor(requireContext(), R.color.black90))
                (binding.rvSearchResults.adapter as? SearchAdapter)?.filter?.filter(newText)
                return false
            }
        })

        searchView.setOnCloseListener {
            binding.rvSearchResults.gone()
            binding.rvIncidents.foreground = null
            true
        }

        // Get the search close button image view
        // Get the search close button image view
        val closeButton = searchView.findViewById(R.id.search_close_btn) as ImageView

        // Set on click listener

        // Set on click listener
        closeButton.setOnClickListener {
            //Clear query
            searchView.setQuery("", false)
            //Collapse the action view
            searchView.onActionViewCollapsed()
            //Collapse the search widget
            item.collapseActionView()
        }

        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                //Nothing to do here

                return true // Return true to expand action view
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                binding.rvSearchResults.gone()
                binding.rvIncidents.foreground = null
                return true // Return true to collapse action view
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                // app icon in action bar clicked; go home
                binding.rvSearchResults.gone()
                binding.rvIncidents.foreground = null
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSystemApplyButton(selectedList: List<SystemModel>) {
        if (selectedList.isEmpty()) {
            onSystemDiscardButton()
            return
        }
        this.selectedSystems = selectedList
        val list =
            ArrayList(viewModel.selectedCategory.value).apply { this[SYSTEM_CATEGORY_INDEX] = true }
        viewModel.selectCategory(list)
    }

    override fun onDateApplyButton(startDate: String, endDate: String) {
        this.selectedDates =
            Pair(DateUtils.stringDateToLong(startDate), DateUtils.stringDateToLong(endDate))
        val list =
            ArrayList(viewModel.selectedCategory.value).apply { this[DATE_CATEGORY_INDEX] = true }
        viewModel.selectCategory(list)
    }

    override fun onSystemDiscardButton() {
        this.selectedSystems = allIncidents.map { SystemModel.createFromIncident(it) }
        val list = ArrayList(viewModel.selectedCategory.value).apply {
            this[SYSTEM_CATEGORY_INDEX] = false
        }
        viewModel.selectCategory(list)
    }

    override fun onDateDiscardButton() {
        this.selectedDates = null
        val list =
            ArrayList(viewModel.selectedCategory.value).apply { this[DATE_CATEGORY_INDEX] = false }
        viewModel.selectCategory(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val SYSTEM_CATEGORY_INDEX = 0
        const val DATE_CATEGORY_INDEX = 1
    }
}