package com.issen.workerfinder.ui.userProfile

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.UserData
import com.issen.workerfinder.databinding.FragmentUserProfileEditBindingImpl
import kotlinx.android.synthetic.main.fragment_user_profile_edit.*

class UserProfileEditFragment : Fragment() {

    private val userProfileEditFragmentArgs: UserProfileEditFragmentArgs by navArgs()
    private val userProfileViewModel: UserProfileViewModel by viewModels {
        UserProfileViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).taskRepository,
            (requireActivity().application as WorkerFinderApplication).commentRepository,
            (requireActivity().application as WorkerFinderApplication).userRepository,
            (requireActivity().application as WorkerFinderApplication).contactRepository,
            (requireActivity().application as WorkerFinderApplication).dashboardNotificationRepository,
            userProfileEditFragmentArgs.userDataFull
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

        val binding: FragmentUserProfileEditBindingImpl =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile_edit, container, false)
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
                currentLoggedInUserFull!!.userData.userId,
                user_profile_edit_username.text.toString(),
                "",
                "",
                user_profile_edit_phone.text.toString(),
                "aaaaa",
                false
            )
        )
        findNavController().popBackStack(R.id.nav_user_profile, false)
    }


}