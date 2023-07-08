package com.gojek.demo.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.gojek.demo.R
import com.gojek.demo.ui.adapter.RepoListAdapter
import com.gojek.demo.ui.viewmodel.BaseViewModel
import com.gojek.demo.ui.viewmodel.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Objects
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RepositoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class RepositoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repository, container, false)
    }

  val mViewModel: RepoViewModel by activityViewModels()

    @Inject
    lateinit var mRepoAdapter: RepoListAdapter

    lateinit var mRepoRecyclerview: RecyclerView
    lateinit var mErrorContainer: RelativeLayout
    lateinit var mRetryButton: AppCompatButton


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setAdapter()
        setListeners()
        observeLoadingState()
        // get the api data
        fetchRepoListData()
        observeRepoListData()
    }

    //Initialize the views
    fun initViews(view: View) {
        mRepoRecyclerview = view.findViewById(R.id.repo_recyclerview)
        mErrorContainer = view.findViewById(R.id.error_container)
        mRetryButton = view.findViewById(R.id.retry_button)
        val progressBar = ProgressDialog(requireActivity())
        progressBar.setMessage("Please Wait....")
        this@RepositoryFragment.progressBar = progressBar
    }

    //set the list adapter
    fun setAdapter() {
        mRepoRecyclerview.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mRepoAdapter
        }
    }

    //obser the loading state during and ater api call to handle the views
    fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.observeViewStateLiveData().observe(viewLifecycleOwner, { it ->
                when (it) {
                    BaseViewModel.ViewStateType.SUCCESS -> {
                        onHideLoading(true)
                    }
                    BaseViewModel.ViewStateType.ERROR -> {
                        onHideLoading(false)
                    }
                    BaseViewModel.ViewStateType.LOADING -> {
                        onLoading()
                    }
                }
            })
        }
    }


    //show the loader
    fun onLoading() {
        showLoadingProgress()
        mRepoRecyclerview.visibility = GONE
        mErrorContainer.visibility = GONE
    }

    lateinit var progressBar:ProgressDialog
    //show the loader
    fun showLoadingProgress() {


    //    if(Objects.isNull(progressBar)) {

     //   }
        progressBar?.show()
    }

    //hide the loader
    fun hideLoadingProgress() {
        progressBar?.dismiss()

    }


    //hide the loader
    fun onHideLoading(isSucces: Boolean) {
        hideLoadingProgress()
        if (isSucces) {
            mErrorContainer.visibility = GONE
            mRepoRecyclerview.visibility = VISIBLE
        } else {
            mErrorContainer.visibility = VISIBLE
            mRepoRecyclerview.visibility = GONE
        }
    }

    //set listeners
    fun setListeners() {
        mRetryButton.setOnClickListener({
            onRetry()
        })
    }


    //on retry button click
    fun onRetry() {
        fetchRepoListData()
    }

    fun observeRepoListData() {
        mViewModel.getDataEvent().observe(viewLifecycleOwner, { it ->
            mRepoAdapter.setDataList(it)
        })
    }

    fun fetchRepoListData() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.getRepoListData()
        }
    }
}