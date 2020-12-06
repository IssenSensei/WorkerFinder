package com.issen.workerfinder.ui.userProfile

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.issen.workerfinder.MainActivity
import com.issen.workerfinder.R
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInUser
import com.issen.workerfinder.database.models.UserModel
import com.issen.workerfinder.databinding.FragmentUserProfileBinding

class UserProfileFragment : Fragment(), UserProfileListener {

    private val userProfileViewModel: UserProfileViewModel by viewModels {
        UserProfileViewModelFactory(
            this.requireActivity().application,
            currentLoggedInUser!!.firebaseKey
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentUserProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        val view = binding.root

        Glide.with(requireContext()).load(currentLoggedInUser!!.photo).placeholder(R.drawable.meme).into(binding.userProfilePhoto)
        binding.user = currentLoggedInUser
        binding.clickListener = this

        userProfileViewModel.activeTasks.observe(viewLifecycleOwner, Observer {
            binding.userProfileActiveTasks.text = it.toString()
        })

        userProfileViewModel.completedTasks.observe(viewLifecycleOwner, Observer {
            binding.userProfileCompletedTasks.text = it.toString()
        })

        userProfileViewModel.abandonedTasks.observe(viewLifecycleOwner, Observer {
            binding.userProfileAbandonedTasks.text = it.toString()
        })

        binding.executePendingBindings()


        return view
    }

    override fun onCallClicked(user: UserModel) {
        Toast.makeText(requireContext(), "call clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onChatClicked(user: UserModel) {
        Toast.makeText(requireContext(), "chat clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onEmailClicked(user: UserModel) {
        Toast.makeText(requireContext(), "email clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onSmsClicked(user: UserModel) {
        Toast.makeText(requireContext(), "sms clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onEditProfileClicked(user: UserModel) {
        findNavController().navigate(R.id.action_nav_user_profile_to_nav_user_profile_edit)
    }

    override fun onDeleteAccountClicked(user: UserModel) {
        (this.activity as MainActivity).askUserForPassword()
    }

    override fun onPublicManageClicked(user: UserModel) {
        if(user.isAccountPublic){
            //todo check if there are any active tasks in dialog and confirm decision
            userProfileViewModel.setAccountPublic(user.firebaseKey, false)
            user.isAccountPublic = false
        } else {
            //todo validate data
            userProfileViewModel.setAccountPublic(user.firebaseKey, true)
            user.isAccountPublic = true
            Toast.makeText(requireContext(), "public clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onContactManageClicked(user: UserModel) {
        Toast.makeText(requireContext(), "addContact clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onProfilePhotoClicked(user: UserModel) {
        Toast.makeText(requireContext(), "photo clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onShowUserCommentsClicked(user: UserModel) {
        Toast.makeText(requireContext(), "userComments clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onShowWorkerCommentsClicked(user: UserModel) {
        Toast.makeText(requireContext(), "workerComments clicked", Toast.LENGTH_SHORT).show()
    }
}

interface UserProfileListener{
    fun onCallClicked(user: UserModel)
    fun onChatClicked(user: UserModel)
    fun onEmailClicked(user: UserModel)
    fun onSmsClicked(user: UserModel)
    fun onEditProfileClicked(user: UserModel)
    fun onDeleteAccountClicked(user: UserModel)
    fun onPublicManageClicked(user: UserModel)
    fun onContactManageClicked(user: UserModel)
    fun onProfilePhotoClicked(user: UserModel)
    fun onShowUserCommentsClicked(user: UserModel)
    fun onShowWorkerCommentsClicked(user: UserModel)
}