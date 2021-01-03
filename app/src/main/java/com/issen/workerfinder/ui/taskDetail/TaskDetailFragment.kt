package com.issen.workerfinder.ui.taskDetail

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.databinding.FragmentTaskDetailBinding

class TaskDetailFragment : Fragment() {

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

        if(taskFull.photos.isNotEmpty()){
            binding.taskListPhotos.visibility = View.VISIBLE
            taskFull.photos.forEach {

                val imageView = ImageView(requireContext())
                imageView.layoutParams = ViewGroup.LayoutParams(300, 300)

                Glide.with(requireContext()).load(it.taskPhoto.toUri()).into(imageView)
                binding.taskListPhotos.addView(imageView)
            }
        }

        if(taskFull.repeatDays.isNotEmpty()){
            taskFull.repeatDays.forEach {
                binding.taskDetailRepeatDays.append(it.repeatDay + "\n")
            }
        }

        binding.executePendingBindings()

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.fragment_task_detail, menu)
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
        taskDetailViewModel.abandonTask(taskFull)
        Toast.makeText(context, taskFull.task.completed, Toast.LENGTH_SHORT).show()
    }

    private fun editTask() {
        findNavController().navigate(R.id.action_nav_task_detail_to_task_edit_fragment)
    }

}