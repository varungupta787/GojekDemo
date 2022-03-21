package com.gojek.demo.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.domain.usercase.RepoDataUsecase
import com.gojek.demo.ui.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepoViewModel @Inject constructor(
    var mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    var repoUseCase: RepoDataUsecase
) :
    BaseViewModel() {
    fun getRepoListData(): SingleLiveEvent<List<RepoItem>> {
        val dataEvent = SingleLiveEvent<List<RepoItem>>()
        viewStateLiveData.setValue(ViewStateType.LOADING)
        viewModelScope.launch(mainDispatcher) {
            repoUseCase.getRepoData({
                viewStateLiveData.setValue(ViewStateType.SUCCESS)
                dataEvent.postValue(it)
            }, {
                viewStateLiveData.setValue(ViewStateType.ERROR)
            })
        }
        return dataEvent
    }
}