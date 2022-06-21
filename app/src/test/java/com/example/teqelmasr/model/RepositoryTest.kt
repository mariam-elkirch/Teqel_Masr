package com.example.teqelmasr.model

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk =[30])
@RunWith(AndroidJUnit4::class)
class RepositoryTest : TestCase(){

    private lateinit var repository: Repository
    private lateinit var fakeDataSource: FakeDataSource

    @Before
    fun obtainRepoInstance(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        fakeDataSource = FakeDataSource()
        repository = Repository.getInstance(fakeDataSource, context)
    }

    /*    @Before
    fun createRepository() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        remoteDataSource = FakeRepository()
        // Get a reference to the class under test
        repo = Repository.getInstance(remoteDataSource, context)
    }

    @Test
    fun getOrders() = runBlockingTest {
        // When tasks are requested from the tasks repository
        val tasks = repo.getOrders("")

        // Then tasks are loaded from the remote data source
        assertEquals(2,tasks.orders.size)
    }*/
}