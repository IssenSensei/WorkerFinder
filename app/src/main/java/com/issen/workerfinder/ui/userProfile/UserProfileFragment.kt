package com.issen.workerfinder.ui.userProfile

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.issen.workerfinder.MainActivity
import com.issen.workerfinder.R
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.models.UserDataWithComments
import com.issen.workerfinder.databinding.FragmentUserProfileBinding
import kotlinx.android.synthetic.main.dialog_user_comments.view.*

class UserProfileFragment : Fragment(), UserProfileListener {

    private val userProfileFragmentArgs: UserProfileFragmentArgs by navArgs()
    lateinit var userDataFull: UserDataFull

    private val userProfileViewModel: UserProfileViewModel by viewModels {
        UserProfileViewModelFactory(
            this.requireActivity().application,
            userDataFull
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentUserProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        val view = binding.root
        userDataFull = userProfileFragmentArgs.userDataFull
        Glide.with(requireContext()).load(userDataFull.userData.photo).placeholder(R.drawable.meme).into(
            binding.userProfilePhoto
        )
        binding.user = userDataFull
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

        userProfileViewModel.ratingAsUser.observe(viewLifecycleOwner, Observer {
            binding.userProfileRatingUser.text = it?.toString() ?: 0.toString()
            binding.userProfileRatingUserBar.rating = it ?: 0f
        })

        userProfileViewModel.ratingAsWorker.observe(viewLifecycleOwner, Observer {
            binding.userProfileRatingWorker.text = it?.toString() ?: 0.toString()
            binding.userProfileRatingWorkerBar.rating = it ?: 0f
        })

        if (userDataFull.userData.firebaseKey == currentLoggedInUserFull!!.userData.firebaseKey) {
            binding.userProfileContactSms.visibility = View.GONE
            binding.userProfileContactEmail.visibility = View.GONE
            binding.userProfileContactCall.visibility = View.GONE
            binding.userProfileContactChat.visibility = View.GONE
            binding.userProfileContactManage.visibility = View.GONE
        } else {
            binding.userProfileDelete.visibility = View.GONE
            binding.userProfilePublicManage.visibility = View.GONE
            binding.userProfileEdit.visibility = View.GONE
        }

        binding.executePendingBindings()
        return view
    }

    override fun onCallClicked(userFull: UserDataFull) {
        Toast.makeText(requireContext(), "call clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onChatClicked(userFull: UserDataFull) {
        Toast.makeText(requireContext(), "chat clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onEmailClicked(userFull: UserDataFull) {
        Toast.makeText(requireContext(), "email clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onSmsClicked(userFull: UserDataFull) {
        Toast.makeText(requireContext(), "sms clicked", Toast.LENGTH_SHORT).show()
    }

    //todo add restrictions if profile is public
    override fun onEditProfileClicked(userFull: UserDataFull) {
        findNavController().navigate(R.id.action_nav_user_profile_to_nav_user_profile_edit)
    }

    override fun onDeleteAccountClicked(userFull: UserDataFull) {
        (this.activity as MainActivity).askUserForPassword()
    }

    override fun onPublicManageClicked(userFull: UserDataFull) {
        if (userFull.userData.isAccountPublic) {
            showConfirmationDialog(false)
        } else {
            if (checkValidation()) {
                showConfirmationDialog(true)
            } else {
                Toast.makeText(requireContext(), "Uzupełnij wymagane dane aby móc uczynić profil publicznym!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //todo create solid validation
    private fun checkValidation() = when {
        userDataFull.userData.userName == "" -> false
        userDataFull.userData.email == "" && userDataFull.userData.phone == "" -> false
        else -> true
    }

    override fun onContactManageClicked(userFull: UserDataFull) {
        Toast.makeText(requireContext(), "addContact clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onProfilePhotoClicked(userFull: UserDataFull) {
        Toast.makeText(requireContext(), "photo clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onShowUserCommentsClicked(userFull: UserDataFull) {
        showRatingDialog(userProfileViewModel.commentUser.value)
    }

    override fun onShowWorkerCommentsClicked(userFull: UserDataFull) {
        showRatingDialog(userProfileViewModel.commentWorker.value)
    }

    private fun showRatingDialog(commentList: List<UserDataWithComments>?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_user_comments, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val adapter = CommentRecyclerViewAdapter()
        dialogView.dialog_user_comments_recycler.adapter = adapter
        adapter.submitList(commentList)

        builder.apply {
            setView(dialogView)
            setTitle("Komentarze")
            setNeutralButton("Zamknij") { dialogInterface, i -> }
            create()
            show()
        }
    }

    private fun showConfirmationDialog(goingToPublic: Boolean) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle("Potwierdź decyzję")
            if (goingToPublic) {
                setMessage("Twój profil stanie się widoczny dla innych użytkowników, czy jesteś pewien?")
                setPositiveButton("Akceptuj") { dialogInterface, i ->
                    userProfileViewModel.setAccountPublic(userDataFull.userData.firebaseKey, goingToPublic)
                    userDataFull.userData.isAccountPublic = goingToPublic
                    Toast.makeText(requireContext(), "Twój profil jest teraz publiczny", Toast.LENGTH_SHORT).show()
                }
            } else {
                setMessage(
                    "Twój profil przestanie być widoczny dla innych użytkowników, oraz nie będzie już dostępny w wynikach " +
                            "wyszukiwania, czy jesteś pewien?"
                )
                setPositiveButton("Akceptuj") { dialogInterface, i ->
                    userProfileViewModel.setAccountPublic(userDataFull.userData.firebaseKey, goingToPublic)
                    userDataFull.userData.isAccountPublic = goingToPublic
                    Toast.makeText(requireContext(), "Twój profil jest teraz prywatny", Toast.LENGTH_SHORT).show()
                }
            }
            setNeutralButton("Anuluj") { dialogInterface, i -> }
            create()
            show()
        }
    }
}

interface UserProfileListener {
    fun onCallClicked(userFull: UserDataFull)
    fun onChatClicked(userFull: UserDataFull)
    fun onEmailClicked(userFull: UserDataFull)
    fun onSmsClicked(userFull: UserDataFull)
    fun onEditProfileClicked(userFull: UserDataFull)
    fun onDeleteAccountClicked(userFull: UserDataFull)
    fun onPublicManageClicked(userFull: UserDataFull)
    fun onContactManageClicked(userFull: UserDataFull)
    fun onProfilePhotoClicked(userFull: UserDataFull)
    fun onShowUserCommentsClicked(userFull: UserDataFull)
    fun onShowWorkerCommentsClicked(userFull: UserDataFull)
}