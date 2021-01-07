package com.issen.workerfinder.ui.conversationList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.ConversationsFull
import kotlinx.android.synthetic.main.fragment_conversation_list.view.*

class ConversationListFragment : Fragment(), ConversationListListener {

    private val conversationListViewModel: ConversationListViewModel by viewModels {
        ConversationListViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).conversationRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_conversation_list, container, false)

        val adapter = ConversationListRecyclerViewAdapter(this)
        conversationListViewModel.conversationList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        root.conversation_recycler_list.adapter = adapter

        root.conversation_fab.setOnClickListener {
            val actionPickContact = ConversationListFragmentDirections.actionNavConversationListToNavContactList(true)
            findNavController().navigate(actionPickContact)
        }
        return root
    }

    override fun onConversationClicked(conversationsFull: ConversationsFull) {
        val actionMessages = ConversationListFragmentDirections.actionNavConversationListToNavConversation(conversationsFull)
        findNavController().navigate(actionMessages)
    }

}