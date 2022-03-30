package com.gojek.demo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.di.AppModule
import com.gojek.demo.domain.usercase.RepoDataUsecase
import com.gojek.demo.ui.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class RepoViewModel  @Inject constructor(
    @AppModule.MainDispatcher var mainDispatcher: CoroutineDispatcher,
    var repoUseCase: RepoDataUsecase)
 : BaseViewModel() {
    suspend fun getRepoListData(): SingleLiveEvent<List<RepoItem>> {
        val dataEvent = SingleLiveEvent<List<RepoItem>>()
        viewStateLiveData.setValue(ViewStateType.LOADING)
        viewModelScope.launch(mainDispatcher) {
            repoUseCase.getRepoData({
                viewStateLiveData.postValue(ViewStateType.SUCCESS)
                dataEvent.postValue(it)
            }, {
                viewStateLiveData.postValue(ViewStateType.ERROR)
            })
        }
        return dataEvent
    }
}