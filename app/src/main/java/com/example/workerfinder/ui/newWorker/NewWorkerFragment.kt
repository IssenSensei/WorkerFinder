package com.example.workerfinder.ui.newWorker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.workerfinder.R

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
        viewModel = ViewModelProviders.of(this).get(NewWorkerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}