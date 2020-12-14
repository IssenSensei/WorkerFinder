package com.issen.workerfinder.ui.taskDetail

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.issen.workerfinder.R
import com.issen.workerfinder.databinding.FragmentTaskDetailBinding

class TaskDetailFragment : Fragment() {

    private lateinit var tastDetailViewModel: TaskDetailViewModel

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
        val task = safeArgs.taskFull
        binding.fullTask = task

        if(task.photos.isNotEmpty()){
            binding.taskListPhotos.visibility = View.VISIBLE
            task.photos.forEach {

                val imageView = ImageView(requireContext())
                imageView.layoutParams = ViewGroup.LayoutParams(300, 300)

                Glide.with(requireContext()).load(it.taskPhoto.toUri()).into(imageView)
                binding.taskListPhotos.addView(imageView)
            }
        }

        if(task.repeatDays.isNotEmpty()){
            task.repeatDays.forEach {
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun editTask() {
        findNavController().navigate(R.id.action_nav_task_detail_to_task_edit_fragment)
    }

}