package com.gojek.demo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gojek.demo.data.model.Owner
import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.domain.usercase.RepoDataUsecase
import com.gojek.demo.ui.viewmodel.BaseViewModel
import com.gojek.demo.ui.viewmodel.RepoViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*

@ExperimentalCoroutinesApi
class RepoViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var repoUsecase: RepoDataUsecase

    private val testDispatcher = TestCoroutineDispatcher()

    lateinit var repoViewModel: RepoViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repoViewModel = RepoViewModel(testDispatcher, repoUsecase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetRepoListDataForSuccess() = runBlockingTest {

        val owner = Owner("testLogin", "www.test.com")
        val repoItem1 = RepoItem(1, any(), any(), any(), any(), any(), any(), owner)

        val repoItem2 = RepoItem(2, anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), any())
        val success = slot<(List<RepoItem>) -> Unit>()
        val error = mockk<(t: String?) -> Unit>()

        val repoList = listOf(repoItem1, repoItem2)

        coEvery {
            repoUsecase.getRepoData(capture(success), any())
        } coAnswers  {
            assert(repoViewModel.observeViewStateLiveData().value == BaseViewModel.ViewStateType.LOADING)
            success.captured.invoke(repoList)
        }
        repoViewModel.getDataEvent().observeForever {}
        repoViewModel.observeViewStateLiveData().observeForever {}

        repoViewModel.getRepoListData()
        assert(repoViewModel.observeViewStateLiveData().value != null)
        assert(repoViewModel.observeViewStateLiveData().value == BaseViewModel.ViewStateType.SUCCESS)
        coVerify { repoUsecase.getRepoData(any(), any()) }

        success.captured.let { res ->
            assertNotNull(res)
        }

        assertEquals(2, repoViewModel.getDataEvent().value?.size ?: 0)
        assertEquals(1,repoViewModel.getDataEvent().value?.get(0)?.id ?: 0)
        assertEquals("testLogin",repoViewModel.getDataEvent().value?.get(0)?.owner?.login ?: "")
        assertEquals("www.test.com",repoViewModel.getDataEvent().value?.get(0)?.owner?.avatar_url ?: "")
        assertEquals(2,repoViewModel.getDataEvent().value?.get(1)?.id ?: 0)
        assertEquals(Owner::class.java,repoViewModel.getDataEvent().value?.get(0)?.owner?.javaClass)
        repoViewModel.getDataEvent().removeObserver{}
        repoViewModel.observeViewStateLiveData().removeObserver {}

    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetRepoListDataForFail() = runBlockingTest {

        val success = slot<(List<RepoItem>) -> Unit>()
        val error = slot<(t: String?) -> Unit>()

        coEvery {
            repoUsecase.getRepoData(any(), capture(error))
        } coAnswers  {
            assert(repoViewModel.observeViewStateLiveData().value == BaseViewModel.ViewStateType.LOADING)
            error.captured.invoke(anyString())
        }
        repoViewModel.observeViewStateLiveData().observeForever {  }
        repoViewModel.getDataEvent().observeForever {  }
        repoViewModel.getRepoListData()
        assertNotNull(repoViewModel.observeViewStateLiveData().value)
        assert(repoViewModel.observeViewStateLiveData().value == BaseViewModel.ViewStateType.ERROR)
        coVerify() { repoUsecase.getRepoData(any(), any()) }

        repoViewModel.observeViewStateLiveData().removeObserver {  }
        repoViewModel.getDataEvent().removeObserver {  }
    }

    @After
    fun resetAll() {

    }
}