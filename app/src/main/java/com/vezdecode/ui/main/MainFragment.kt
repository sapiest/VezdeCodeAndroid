package com.vezdecode.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.vezdecode.R
import com.vezdecode.data.remote.model.IncidentModel
import com.vezdecode.data.remote.repository.IncidentsRepository
import com.vezdecode.databinding.MainFragmentBinding
import com.vezdecode.ui.incident_details_fragment.IncidentDetailsFragment
import com.vezdecode.ui.main.adapter.IncidentsAdapter
import com.vezdecode.viewmodels.IncidentsViewModel
import com.vezdecode.viewmodels.factory.ViewModelFactory

class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IncidentsViewModel by activityViewModels {
        ViewModelFactory(
            IncidentsRepository()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.updateIncidents()
    }

    private fun bindViews() {
        initToolbar()
        setObservers()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.rvIncidents.apply {

            // Set Layout manager
            this.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            // Set RecyclerViewAdapter
            this.adapter = IncidentsAdapter(R.layout.incident_item_list, viewModel::onClick)
        }
    }

    private fun initToolbar() {
        binding.toolbar.title = getString(R.string.incidents_title)
    }

    private fun setObservers() {
        viewModel.incidents.observe(viewLifecycleOwner, Observer { viewState ->
            if (!viewState.isLoading()) {
                viewState.data?.let {
                    (binding.rvIncidents.adapter as ListAdapter<IncidentModel, *>).submitList(it)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}