package com.gojek.demo.data.local.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.gojek.demo.data.local.dao.RepoItemDao
import com.gojek.demo.data.local.entity.RepoItemEntity
import com.gojek.demo.data.model.Owner
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@SmallTest
class RepositoryDatabaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: RepositoryDatabase

    lateinit var repoItemDao: RepoItemDao

    @Before
    fun setUp() {
        hiltRule.inject()
        repoItemDao = database.repoItemDao()
    }

    @After
    fun reset() {
        database.close()
    }

    @Test
    fun getAllRepositoryItemTest() {
        val owner1 = Owner("testLogin1", "www.test1.com")
        val owner2 = Owner("testLogin2", "www.test2.com")
        val entity1 = RepoItemEntity(1, 11, "", 111, "", "", "", owner1)
        val entity2 = RepoItemEntity(2, 22, "", 222, "", "", "", owner2)
        val repoEntityList = listOf(entity1, entity2)

        repoItemDao.insertAllRepositoryItems(repoEntityList)

        val dbResult = repoItemDao.getAllRepositoryItem()

        assertTrue(dbResult.contains(entity1))
        assertEquals(dbResult.size, 2)
        assertEquals(dbResult.first().id, 1)
    }

    @Test
    fun insertAllRepositoryItemsTest() {
        val owner1 = Owner("testLogin1", "www.test1.com")
        val owner2 = Owner("testLogin2", "www.test2.com")
        val entity1 = RepoItemEntity(1, 11, "", 111, "", "", "", owner1)
        val entity2 = RepoItemEntity(2, 22, "", 222, "", "", "", owner2)
        val repoEntityList = listOf(entity1, entity2)

        repoItemDao.insertAllRepositoryItems(repoEntityList)
        val dbResult = repoItemDao.getAllRepositoryItem()
        assertTrue(dbResult.contains(entity2))
        assertEquals(dbResult.size, 2)
        assertEquals(dbResult.first().id, 1)
    }

    @Test
    fun deleteAllRepositoryItemsTest() {
        repoItemDao.deleteAllRepositoryItems()
        val dbResult = repoItemDao.getAllRepositoryItem()
        assertEquals(dbResult.size, 0)

    }
}