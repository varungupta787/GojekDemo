package com.gojek.demo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gojek.demo.di.AppModule
import com.gojek.demo.domain.usercase.RepoDataUsecase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ViewModelFactory(@AppModule.MainDispatcher
                       var mainDispatcher: CoroutineDispatcher =
                           Dispatchers.Main, var repoUseCase: RepoDataUsecase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RepoViewModel(mainDispatcher, repoUseCase) as T
    }
}