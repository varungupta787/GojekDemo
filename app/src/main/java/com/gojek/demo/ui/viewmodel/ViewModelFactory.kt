package com.gojek.demo.ui.viewmodel

/*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gojek.demo.di.AppModule
import com.gojek.demo.domain.usercase.RepoDataUsecase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    @AppModule.IoDispatcher var mainDispatcher: CoroutineDispatcher,
    var repoUseCase: RepoDataUsecase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepoViewModel::class.java)) {
            return RepoViewModel(mainDispatcher, repoUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
*/
