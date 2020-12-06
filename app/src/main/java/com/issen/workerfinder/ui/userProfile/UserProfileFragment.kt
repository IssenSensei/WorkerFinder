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
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInFullUser
import com.issen.workerfinder.database.models.FullUserData
import com.issen.workerfinder.databinding.FragmentUserProfileBinding

class UserProfileFragment : Fragment(), UserProfileListener {

    private val userProfileViewModel: UserProfileViewModel by viewModels {
        UserProfileViewModelFactory(
            this.requireActivity().application,
            currentLoggedInFullUser!!.userData.firebaseKey
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentUserProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        val view = binding.root

        Glide.with(requireContext()).load(currentLoggedInFullUser!!.userData.photo).placeholder(R.drawable.meme).into(binding.userProfilePhoto)
        binding.user = currentLoggedInFullUser
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

    override fun onCallClicked(fullUser: FullUserData) {
        Toast.makeText(requireContext(), "call clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onChatClicked(fullUser: FullUserData) {
        Toast.makeText(requireContext(), "chat clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onEmailClicked(fullUser: FullUserData) {
        Toast.makeText(requireContext(), "email clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onSmsClicked(fullUser: FullUserData) {
        Toast.makeText(requireContext(), "sms clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onEditProfileClicked(fullUser: FullUserData) {
        findNavController().navigate(R.id.action_nav_user_profile_to_nav_user_profile_edit)
    }

    override fun onDeleteAccountClicked(fullUser: FullUserData) {
        (this.activity as MainActivity).askUserForPassword()
    }

    override fun onPublicManageClicked(fullUser: FullUserData) {
        if(fullUser.userData.isAccountPublic){
            //todo check if there are any active tasks in dialog and confirm decision
            userProfileViewModel.setAccountPublic(fullUser.userData.firebaseKey, false)
            fullUser.userData.isAccountPublic = false
        } else {
            //todo validate data
            userProfileViewModel.setAccountPublic(fullUser.userData.firebaseKey, true)
            fullUser.userData.isAccountPublic = true
            Toast.makeText(requireContext(), "public clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onContactManageClicked(fullUser: FullUserData) {
        Toast.makeText(requireContext(), "addContact clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onProfilePhotoClicked(fullUser: FullUserData) {
        Toast.makeText(requireContext(), "photo clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onShowUserCommentsClicked(fullUser: FullUserData) {
        Toast.makeText(requireContext(), "userComments clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onShowWorkerCommentsClicked(fullUser: FullUserData) {
        Toast.makeText(requireContext(), "workerComments clicked", Toast.LENGTH_SHORT).show()
    }
}

interface UserProfileListener{
    fun onCallClicked(fullUser: FullUserData)
    fun onChatClicked(fullUser: FullUserData)
    fun onEmailClicked(fullUser: FullUserData)
    fun onSmsClicked(fullUser: FullUserData)
    fun onEditProfileClicked(fullUser: FullUserData)
    fun onDeleteAccountClicked(fullUser: FullUserData)
    fun onPublicManageClicked(fullUser: FullUserData)
    fun onContactManageClicked(fullUser: FullUserData)
    fun onProfilePhotoClicked(fullUser: FullUserData)
    fun onShowUserCommentsClicked(fullUser: FullUserData)
    fun onShowWorkerCommentsClicked(fullUser: FullUserData)
}