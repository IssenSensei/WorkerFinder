package com.issen.workerfinder.ui.newWorker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.R

class NewWorkerFragment : Fragment() {

    companion object {
        fun newInstance() = NewWorkerFragment()
    }

    private lateinit var viewModel: NewWorkerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_worker_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewWorkerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}