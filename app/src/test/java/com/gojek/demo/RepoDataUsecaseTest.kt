package com.gojek.demo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gojek.demo.data.NetworkConnectionUtil
import com.gojek.demo.data.local.database.RepositoryDatabase
import com.gojek.demo.data.local.entity.RepoItemEntity
import com.gojek.demo.data.model.Owner
import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.domain.RepositoryDataRepo
import com.gojek.demo.domain.models.ResponseResource
import com.gojek.demo.domain.usercase.RepoDataUsecase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*

@ExperimentalCoroutinesApi
class RepoDataUsecaseTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var repositoryDataRepo: RepositoryDataRepo

    @MockK
    lateinit var repositoryDatabase: RepositoryDatabase

    private val testDispatcher = TestCoroutineDispatcher()

    lateinit var repoDataUsecase: RepoDataUsecase

    @MockK
    lateinit var testNetworkConnectionUtil: NetworkConnectionUtil

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repoDataUsecase = RepoDataUsecase(testDispatcher, repositoryDataRepo, repositoryDatabase)
        repoDataUsecase.networkConnectionUtil = testNetworkConnectionUtil
    }

    @Test
    fun getRepoDataForNetworkApiSuccessTest() = runBlockingTest {

        val owner = Owner("testLogin", "www.test.com")
        val repoItem1 = RepoItem(1, any(), any(), any(), any(), any(), any(), owner)
        val repoItem2 = RepoItem(2, anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), any())
        val repoList = listOf(repoItem1, repoItem2)
        val repoEntityList = mutableListOf<RepoItemEntity>()
        every { testNetworkConnectionUtil.isNetworkAvailable() } returns true

        val fakeResponse = ResponseResource<List<RepoItem>>(ResponseResource.StatusType.SUCCESS, repoList, any(), any())

        coEvery { repositoryDataRepo.getRepoData() } returns fakeResponse

        every { repositoryDatabase.repoItemDao().deleteAllRepositoryItems() } returns Unit
        val repoEntiy =  repoDataUsecase.convertDataToRepoEntityList(repoList)
        every { repositoryDatabase.repoItemDao().insertAllRepositoryItems(repoEntiy) } returns Unit

        val success = mockk<(List<RepoItem>) -> Unit>()
        val error = mockk<(t: String?) -> Unit>()

        every { success.invoke(any()) } answers  { fakeResponse  }

        repoDataUsecase.getRepoData(onSuccess = success , onError = error)

        coVerifyAll{
            repoDataUsecase.saveToDatabase(repoList)
           repoDataUsecase.deleteAllPreviousData()
            repoDataUsecase.saveNewData(repoList)
        }
    }


    @Test
    fun getRepoDataForNetworkApiErrorTest() = runBlockingTest {

        every { testNetworkConnectionUtil.isNetworkAvailable() } returns true

        val fakeResponseError = ResponseResource<List<RepoItem>>(ResponseResource.StatusType.ERROR, anyList(), "ERROR_MSG", any())
        val success = mockk<(List<RepoItem>) -> Unit>()
        val error = mockk<(t: String?) -> Unit>()

        coEvery { repositoryDataRepo.getRepoData() } returns fakeResponseError

        every { error.invoke(any()) } answers { "ERROR_MSG"  }

        assertEquals(true, testNetworkConnectionUtil.isNetworkAvailable())
        verify{ testNetworkConnectionUtil.isNetworkAvailable()  }
        repoDataUsecase.getRepoData(onSuccess = success , onError = error)
        coVerify { error.invoke("ERROR_MSG") }
    }
}