package com.issen.workerfinder.ui.taskEdit

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.issen.workerfinder.R

class TaskEditFragment : Fragment() {

    private val taskEditViewModel: TaskEditViewModel by viewModels {
        TaskEditViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_edit, container, false)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                updateTask()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateTask() {
        TODO("Not yet implemented")
    }

}