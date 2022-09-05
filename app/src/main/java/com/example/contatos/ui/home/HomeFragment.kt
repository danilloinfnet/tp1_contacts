package com.example.contatos.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contatos.R
import com.example.contatos.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        }
        recyclerView = view.findViewById(R.id.contactsRecycleView)
        configureRecyclerView()
        subscribe()

        binding.fabCreateContact.setOnClickListener {
            findNavController().navigate(R.id.nav_manage_contact)
        }
    }

    private fun configureRecyclerView() {
        val contactsAdapter = ContactsAdapter(context)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = contactsAdapter
    }

    private fun subscribe() {
        homeViewModel.contactsList.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                val adapter = recyclerView.adapter
                if (adapter is ContactsAdapter) {
                    adapter.changeData(list)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}