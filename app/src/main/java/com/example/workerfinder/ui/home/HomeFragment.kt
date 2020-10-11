package com.example.workerfinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.workerfinder.R
import com.example.workerfinder.database.TaskModel
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(), TaskListListener {

    private lateinit var homeViewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val adapter = HomeRecyclerViewAdapter(this)

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })
        root.task_recycler_list.adapter = adapter
        return root
    }

    override fun onTaskComplete(task: TaskModel) {
        Toast.makeText(context, task.completed, Toast.LENGTH_SHORT).show()
    }


}