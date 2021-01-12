package com.issen.workerfinder.ui.userProfile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.models.UserDataWithComments
import com.issen.workerfinder.databinding.FragmentUserProfileBinding
import kotlinx.android.synthetic.main.dialog_user_comments.view.*

class UserProfileFragment : Fragment(), UserProfileListener {

    private val userProfileFragmentArgs: UserProfileFragmentArgs by navArgs()
    lateinit var userDataFull: UserDataFull

    private val userProfileViewModel: UserProfileViewModel by viewModels {
        UserProfileViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).taskRepository,
            (requireActivity().application as WorkerFinderApplication).commentRepository,
            (requireActivity().application as WorkerFinderApplication).userRepository,
            (requireActivity().application as WorkerFinderApplication).contactRepository,
            (requireActivity().application as WorkerFinderApplication).dashboardNotificationRepository,
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

        userProfileViewModel.isUserInContactList.observe(viewLifecycleOwner, Observer {
            binding.isUserContact = it
            binding.executePendingBindings()
        })

        userProfileViewModel.isAccountPublic.observe(viewLifecycleOwner, Observer {
            if (userDataFull.userData.userId == currentLoggedInUserFull!!.userData.userId) {
                if (it) {
                    binding.userProfilePublic.visibility = View.GONE
                    binding.userProfilePrivate.visibility = View.VISIBLE
                } else {
                    binding.userProfilePublic.visibility = View.VISIBLE
                    binding.userProfilePrivate.visibility = View.GONE
                }
            }
        })

        binding.executePendingBindings()
        return view
    }

    override fun onCallClicked(userFull: UserDataFull) {
        Toast.makeText(requireContext(), "call clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onChatClicked(userFull: UserDataFull) {

        val actionChat = UserProfileFragmentDirections.actionNavUserProfileToNavConversation(null, userFull.userData)
        findNavController().navigate(actionChat)
    }

    override fun onEmailClicked(userFull: UserDataFull) {
        Toast.makeText(requireContext(), "email clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onSmsClicked(userFull: UserDataFull) {
        Toast.makeText(requireContext(), "sms clicked", Toast.LENGTH_SHORT).show()
    }

    //todo add restrictions if profile is public
    override fun onEditProfileClicked(userFull: UserDataFull) {
        val actionEdit = UserProfileFragmentDirections.actionNavUserProfileToNavUserProfileEdit(userFull)
        findNavController().navigate(actionEdit)
    }

    override fun onDeleteAccountClicked(userFull: UserDataFull) {
        (this.activity as MainActivity).askUserForPassword()
    }

    override fun onPublicClicked(userFull: UserDataFull) {
        showPublicConfirmationDialog(true)
    }

    override fun onPrivateClicked(userFull: UserDataFull) {
        if (checkValidation()) {
            showPublicConfirmationDialog(false)
        } else {
            Toast.makeText(requireContext(), "Uzupełnij wymagane dane aby móc uczynić profil publicznym!", Toast.LENGTH_SHORT).show()
        }
    }


    //todo create solid validation
    private fun checkValidation() = when {
        userDataFull.userData.userName == "" -> false
        userDataFull.userData.email == "" && userDataFull.userData.phone == "" -> false
        else -> true
    }

    override fun onContactInvite(userFull: UserDataFull) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.apply {
            setMessage(
                "Wysłać zaproszenie do użytkownika ${userFull.userData.userName}? Jeśli zaakceptuje Twoje zaproszenie, będzie możliwe" +
                        " udostępnianie oraz wykonywanie zadań pomiędzy Wami, czy jesteś pewien?"
            )
            setPositiveButton("Akceptuj") { dialogInterface, i ->
                userProfileViewModel.addContact(userDataFull)
                Toast.makeText(requireContext(), "Wysłano zaproszenie do użytkownika ${userFull.userData.userName}", Toast.LENGTH_SHORT)
                    .show()
            }
            setNeutralButton("Anuluj") { dialogInterface, i -> }
            create()
            show()
        }
    }

    override fun onContactDelete(userFull: UserDataFull) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.apply {
            setMessage(
                "Usunięcie kontaktu spowoduje brak możliwości wysyłania oraz otrzymywania zadań pomiędzy Tobą a ${
                    userFull
                        .userData.userName
                }, jednakże wciąż będziecie mieli możliwość dokończenia aktywnych zadań. Czy jesteś pewien?"
            )
            setPositiveButton("Akceptuj") { dialogInterface, i ->
                userProfileViewModel.removeContact(userDataFull)
                Toast.makeText(requireContext(), "Zerwałeś kontakt z użytkownikiem ${userFull.userData.userName}", Toast.LENGTH_SHORT)
                    .show()
            }
            setNeutralButton("Anuluj") { dialogInterface, i -> }
            create()
            show()
        }
    }

    override fun onOfferWork(userFull: UserDataFull) {
        val actionTask = UserProfileFragmentDirections.actionNavUserProfileToNavNewTask(userFull)
        findNavController().navigate(actionTask)
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

    private fun showPublicConfirmationDialog(goingToPublic: Boolean) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle("Potwierdź decyzję")
            if (goingToPublic) {
                setMessage("Twój profil stanie się widoczny dla innych użytkowników, czy jesteś pewien?")
                setPositiveButton("Akceptuj") { dialogInterface, i ->
                    userProfileViewModel.setAccountPublic(userDataFull.userData.userId, goingToPublic)
                    userDataFull.userData.isAccountPublic = goingToPublic
                    Toast.makeText(requireContext(), "Twój profil jest teraz publiczny", Toast.LENGTH_SHORT).show()
                }
            } else {
                setMessage(
                    "Twój profil przestanie być widoczny dla innych użytkowników, oraz nie będzie już dostępny w wynikach " +
                            "wyszukiwania, czy jesteś pewien?"
                )
                setPositiveButton("Akceptuj") { dialogInterface, i ->
                    userProfileViewModel.setAccountPublic(userDataFull.userData.userId, goingToPublic)
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
    fun onPublicClicked(userFull: UserDataFull)
    fun onPrivateClicked(userFull: UserDataFull)
    fun onContactInvite(userFull: UserDataFull)
    fun onContactDelete(userFull: UserDataFull)
    fun onOfferWork(userFull: UserDataFull)
    fun onProfilePhotoClicked(userFull: UserDataFull)
    fun onShowUserCommentsClicked(userFull: UserDataFull)
    fun onShowWorkerCommentsClicked(userFull: UserDataFull)
}