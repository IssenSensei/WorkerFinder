package com.issen.workerfinder.ui.userProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.issen.workerfinder.MainActivity
import com.issen.workerfinder.R
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInUser
import com.issen.workerfinder.database.UserModel
import com.issen.workerfinder.databinding.FragmentUserProfileBinding

class UserProfileFragment : Fragment() {

    val userProfileViewModel: UserProfileViewModel by viewModels { UserProfileViewModelFactory(this.requireActivity().application, currentLoggedInUser.uid) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentUserProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        val view = binding.root

        binding.userProfileName.text = currentLoggedInUser.displayName
        binding.userProfilePhone.text = currentLoggedInUser.phoneNumber
        binding.userProfileEmail.text = currentLoggedInUser.email
        Glide.with(requireContext()).load(currentLoggedInUser.photoUrl).into(binding.userProfilePhoto)

        userProfileViewModel.activeTasks.observe(viewLifecycleOwner, Observer {
            binding.userProfileActiveTasks.text = it.toString()
        })

        userProfileViewModel.completedTasks.observe(viewLifecycleOwner, Observer {
            binding.userProfileCompletedTasks.text = it.toString()
        })

        userProfileViewModel.abandonedTasks.observe(viewLifecycleOwner, Observer {
            binding.userProfileAbandonedTasks.text = it.toString()
        })

        binding.userProfileDeleteButton.setOnClickListener {
            (this.activity as MainActivity).deleteUser()
        }

        binding.userProfileChat.setOnClickListener {
            Toast.makeText(requireContext(), "chat clicked", Toast.LENGTH_SHORT).show()
        }

        binding.userProfileCall.setOnClickListener {
            Toast.makeText(requireContext(), "call clicked", Toast.LENGTH_SHORT).show()
        }

        binding.userProfilePhoto.setOnClickListener {
            Toast.makeText(requireContext(), "photo clicked", Toast.LENGTH_SHORT).show()
        }

        binding.executePendingBindings()


        return view
    }

}