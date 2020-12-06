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
import com.issen.workerfinder.databinding.FragmentUserProfileBinding

class UserProfileFragment : Fragment() {

    val userProfileViewModel: UserProfileViewModel by viewModels { UserProfileViewModelFactory(this.requireActivity().application,
        currentLoggedInUser!!.firebaseKey) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentUserProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        val view = binding.root

        binding.userProfileName.text = if(currentLoggedInUser!!.userName != "") currentLoggedInUser!!.userName else "Brak danych"
        binding.userProfilePhone.text = if(currentLoggedInUser!!.phone != "") currentLoggedInUser!!.phone else "Brak danych"
        binding.userProfileEmail.text = if(currentLoggedInUser!!.email != "") currentLoggedInUser!!.email else "Brak danych"
        Glide.with(requireContext()).load(currentLoggedInUser!!.photo).placeholder(R.drawable.meme).into(binding.userProfilePhoto)

        userProfileViewModel.activeTasks.observe(viewLifecycleOwner, Observer {
            binding.userProfileActiveTasks.text = it.toString()
        })

        userProfileViewModel.completedTasks.observe(viewLifecycleOwner, Observer {
            binding.userProfileCompletedTasks.text = it.toString()
        })

        userProfileViewModel.abandonedTasks.observe(viewLifecycleOwner, Observer {
            binding.userProfileAbandonedTasks.text = it.toString()
        })

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                editUserInfo()
                return true
            }
            R.id.action_delete -> {
                (this.activity as MainActivity).askUserForPassword()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun editUserInfo() {
        findNavController().navigate(R.id.action_nav_user_profile_to_nav_user_profile_edit)
    }


}