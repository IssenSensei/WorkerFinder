package com.issen.workerfinder.ui.userProfile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.UserData
import com.issen.workerfinder.databinding.FragmentUserProfileEditBinding
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

        val binding = FragmentUserProfileEditBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.user = userProfileEditFragmentArgs.userDataFull
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
                currentLoggedInUserFull!!.userData.photo,
                user_profile_edit_email.text.toString(),
                user_profile_edit_phone.text.toString(),
                user_profile_edit_description.text.toString(),
                user_profile_edit_localization.text.toString(),
                currentLoggedInUserFull!!.userData.isAccountPublic,
                currentLoggedInUserFull!!.userData.isOpenForWork
            )
        )
        findNavController().popBackStack(R.id.nav_user_profile, false)
    }


}