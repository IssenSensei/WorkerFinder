package com.issen.workerfinder.ui.workerAdd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.R

class WorkerAddFragment : Fragment() {

    companion object {
        fun newInstance() = WorkerAddFragment()
    }

    private lateinit var addViewModel: WorkerAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_worker_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addViewModel = ViewModelProvider(this).get(WorkerAddViewModel::class.java)
        // TODO: Use the ViewModel
    }

}