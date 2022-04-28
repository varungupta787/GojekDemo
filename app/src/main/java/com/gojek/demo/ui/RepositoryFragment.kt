package com.gojek.demo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.gojek.demo.R
import com.gojek.demo.ui.adapter.RepoListAdapter
import com.gojek.demo.ui.viewmodel.BaseViewModel
import com.gojek.demo.ui.viewmodel.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

  /*  companion object {
        *//**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RepositoryFragment.
         *//*
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RepositoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/

  val mViewModel: RepoViewModel by activityViewModels()

    @Inject
    lateinit var mRepoAdapter: RepoListAdapter

    lateinit var mShimmerLayout: ShimmerFrameLayout
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
        mShimmerLayout = view.findViewById(R.id.shimmer_layout)
        mRepoRecyclerview = view.findViewById(R.id.repo_recyclerview)
        mErrorContainer = view.findViewById(R.id.error_container)
        mRetryButton = view.findViewById(R.id.retry_button)
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
                        hideLoading(true)
                    }
                    BaseViewModel.ViewStateType.ERROR -> {
                        hideLoading(false)
                    }
                    BaseViewModel.ViewStateType.LOADING -> {
                        showLoading()
                    }
                }
            })
        }
    }

    //stop the shimmer animation
    fun stopShimmerEffect() {
        mShimmerLayout.stopShimmerAnimation()
        mShimmerLayout.visibility = GONE
    }

    //show the shimmer animation
    fun showShimmerEffect() {
        mShimmerLayout.visibility = VISIBLE
        mShimmerLayout.startShimmerAnimation()
    }

    //show the loader
    fun showLoading() {
        showShimmerEffect()
        mRepoRecyclerview.visibility = GONE
        mErrorContainer.visibility = GONE
    }


    //hide the loader
    fun hideLoading(isSucces: Boolean) {
        stopShimmerEffect()
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