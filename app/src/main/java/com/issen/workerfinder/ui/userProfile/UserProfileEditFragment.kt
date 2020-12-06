package com.issen.workerfinder.ui.userProfile

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.issen.workerfinder.R
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInFullUser
import com.issen.workerfinder.database.models.FullUserData
import com.issen.workerfinder.database.models.UserData
import com.issen.workerfinder.databinding.FragmentUserProfileEditBindingImpl
import kotlinx.android.synthetic.main.fragment_user_profile_edit.*

class UserProfileEditFragment : Fragment() {

    val userProfileViewModel: UserProfileViewModel by viewModels {
        UserProfileViewModelFactory(
            this.requireActivity().application,
            currentLoggedInFullUser!!.userData.firebaseKey
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentUserProfileEditBindingImpl = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile_edit, container, false)
        val view = binding.root

//        binding.userProfileName.text = if (currentLoggedInUser.userName != "") currentLoggedInUser.userName else "No data"
//        binding.userProfilePhone.text = if (currentLoggedInUser.phone != "") currentLoggedInUser.phone else "No data"
//        binding.userProfileEmail.text = if (currentLoggedInUser.email != "") currentLoggedInUser.email else "No data"
//        Glide.with(requireContext()).load(currentLoggedInUser.photo).placeholder(R.drawable.meme).into(binding.userProfilePhoto)
//
//        userProfileViewModel.activeTasks.observe(viewLifecycleOwner, Observer {
//            binding.userProfileActiveTasks.text = it.toString()
//        })
//
//        userProfileViewModel.completedTasks.observe(viewLifecycleOwner, Observer {
//            binding.userProfileCompletedTasks.text = it.toString()
//        })
//
//        userProfileViewModel.abandonedTasks.observe(viewLifecycleOwner, Observer {
//            binding.userProfileAbandonedTasks.text = it.toString()
//        })
//
//        binding.userProfileChat.setOnClickListener {
//            Toast.makeText(requireContext(), "chat clicked", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.userProfileCall.setOnClickListener {
//            Toast.makeText(requireContext(), "call clicked", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.userProfilePhoto.setOnClickListener {
//            Toast.makeText(requireContext(), "photo clicked", Toast.LENGTH_SHORT).show()
//        }

        binding.executePendingBindings()


        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                updateUserInfo()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateUserInfo() {
        userProfileViewModel.updateUser(
            UserData(
                currentLoggedInFullUser!!.userData.userId,
                user_profile_edit_username.text.toString(),
                "",
                "",
                user_profile_edit_phone.text.toString(),
                currentLoggedInFullUser!!.userData.firebaseKey,
                false
            )
        )
        findNavController().popBackStack(R.id.nav_user_profile, false)
    }


}