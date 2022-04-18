package com.gojek.demo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gojek.demo.data.NetworkUtils
import com.gojek.demo.data.model.NetworkResponseWrapper
import com.gojek.demo.data.model.Owner
import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.data.remote.ApiService
import com.gojek.demo.data.repositories.RepositoryRepoDataImpl
import com.gojek.demo.domain.models.ResponseResource
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.*
import org.mockito.ArgumentMatchers.*
import retrofit2.Response

class RepositoryRepoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @MockK
    lateinit var apiService: ApiService
    private val testDispatcher = TestCoroutineDispatcher()

    lateinit var repositoryRepoDataTest: RepositoryRepoDataImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repositoryRepoDataTest = RepositoryRepoDataImpl(apiService, testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getRepoDataNetworkSuccessTest() = runBlockingTest {

        val owner = Owner("testLogin", "www.test.com")
        val repoItem1 = RepoItem(1, any(), any(), any(), any(), any(), any(), owner)
        val repoItem2 = RepoItem(2, anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), any())
        val repoList = listOf(repoItem1, repoItem2)

        val mockCallback = mockk<suspend () -> Response<List<RepoItem>>>()
        val slotCallback = slot<suspend () -> Response<List<RepoItem>>>()
        val mockNetworkResponseWrapper =
            NetworkResponseWrapper.NetworkSuccess<List<RepoItem>>(anyList())

        coEvery { apiService.getRepoList() } returns Response.success(200, repoList)

        val response = repositoryRepoDataTest.getRepoData()

        Assert.assertEquals(ResponseResource.StatusType.SUCCESS, response.status)
        Assert.assertEquals(2, response.data?.size)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun getRepoDataNetworkErrorTest() = runBlockingTest {
        val body = ResponseBody.create(MediaType.parse(anyString()), anyString())
        coEvery { apiService.getRepoList() } returns Response.error(5678600, body)

        val response = repositoryRepoDataTest.getRepoData()

        Assert.assertEquals(5678600, response.code)
        Assert.assertEquals(NetworkUtils.default_err_message, response.message)
        Assert.assertEquals(ResponseResource.StatusType.ERROR, response.status)
    }

    @After
    fun reset() {

    }

}