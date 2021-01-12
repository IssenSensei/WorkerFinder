package com.issen.workerfinder.ui.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.issen.workerfinder.MainActivity
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.UserData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_conversation.view.*

class ConversationFragment : Fragment() {

    private lateinit var conversationViewModel: ConversationViewModel
    private val conversationFragmentArgs: ConversationFragmentArgs by navArgs()
    private var conversationId: Int = 0
    private lateinit var secondUser: UserData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_conversation, container, false)

        val adapter = ConversationRecyclerViewAdapter()
        if(conversationFragmentArgs.conversationFull != null){
            conversationId = conversationFragmentArgs.conversationFull!!.conversation.conversationId
            secondUser = if(conversationFragmentArgs.conversationFull!!.firstUser.userId != currentLoggedInUserFull!!.userData.userId){
                conversationFragmentArgs.conversationFull!!.firstUser
            } else {
                conversationFragmentArgs.conversationFull!!.secondUser
            }
        } else{
            secondUser = conversationFragmentArgs.userData!!
        }

        val conversationViewModelFactory = ConversationViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).messageRepository,
            (requireActivity().application as WorkerFinderApplication).conversationRepository,
            conversationId,
            secondUser
        )

        (requireActivity() as MainActivity).toolbar.title = secondUser.userName
        conversationViewModel = ViewModelProvider(this, conversationViewModelFactory).get(ConversationViewModel::class.java)

        conversationViewModel.messageList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            if(adapter.itemCount > 0)
                root.conversation_message_recycler_list.smoothScrollToPosition(adapter.itemCount - 1)
        })
        root.conversation_message_recycler_list.adapter = adapter
        if(adapter.itemCount > 0)
            root.conversation_message_recycler_list.smoothScrollToPosition(adapter.itemCount - 1)

        root.conversation_message_send.setOnClickListener {
            conversationViewModel.sendMessage(root.conversation_message_new.text.toString())
            root.conversation_message_new.text.clear()
        }
        return root
    }
}