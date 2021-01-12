package com.issen.workerfinder.ui.taskDetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.databinding.FragmentTaskDetailBinding
import com.issen.workerfinder.enums.CompletionTypes

class TaskDetailFragment : Fragment(), TaskDetailListener {

    private val taskDetailViewModel: TaskDetailViewModel by viewModels {
        TaskDetailViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).taskRepository,
            (requireActivity().application as WorkerFinderApplication).dashboardNotificationRepository
        )
    }
    private lateinit var taskFull: TaskModelFull

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        val safeArgs: TaskDetailFragmentArgs by navArgs()
        taskFull = safeArgs.fullTask
        binding.fullTask = taskFull
        binding.clickListener = this
        binding.executePendingBindings()

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.fragment_task_detail, menu)
        menu.findItem(R.id.action_abandon).isVisible = isPossibleToAbandon()
        menu.findItem(R.id.action_edit).isVisible = taskFull.task.workerFirebaseKey != ""
    }

    private fun isPossibleToAbandon(): Boolean {
        return taskFull.task.completed != CompletionTypes.COMPLETED.name
                && taskFull.task.completed != CompletionTypes.ABANDONED.name
                && taskFull.task.completed != CompletionTypes.BOARD.name
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                editTask()
                return true
            }
            R.id.action_abandon -> {
                abandonTask()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun abandonTask() {
        if (taskFull.task.userFirebaseKey == currentLoggedInUserFull!!.userData.userId && taskFull.task.workerFirebaseKey == currentLoggedInUserFull!!.userData.userId) {
            taskDetailViewModel.abandonCreatedTask(taskFull)
        } else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.apply {
                setTitle("Potwierdź decyzję")
                if (taskFull.task.userFirebaseKey == currentLoggedInUserFull!!.userData.userId && taskFull.task.workerFirebaseKey != currentLoggedInUserFull!!.userData.userId) {
                    setMessage("Czy na pewno chcesz anulować wystawione przez Ciebie zadanie?")
                    setPositiveButton("Zatwierdź") { dialogInterface, i ->
                        taskDetailViewModel.abandonCommissionedTask(taskFull)
                        Toast.makeText(requireContext(), "Zadanie zostało wycofane", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    setMessage("Czy na pewno chcesz porzucić wykonywanie tego zadania?")
                    setPositiveButton("Akceptuj") { dialogInterface, i ->
                        taskDetailViewModel.abandonAcceptedTask(taskFull)
                        Toast.makeText(requireContext(), "Zadanie zostało porzucone", Toast.LENGTH_SHORT).show()
                    }
                }
                setNeutralButton("Anuluj") { dialogInterface, i -> }
                create()
                show()
            }
        }
    }

    private fun editTask() {
        findNavController().navigate(R.id.action_nav_task_detail_to_task_edit_fragment)
    }

    override fun onSelectFromApplications() {
        val actionPicker = TaskDetailFragmentDirections.actionNavTaskDetailToNavBoardTaskWorkerPicker(taskFull.task.taskId, taskFull)
        findNavController().navigate(actionPicker)
    }

}

interface TaskDetailListener {
    fun onSelectFromApplications()
}