package com.example.teqelmasr.model

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [30])
@RunWith(AndroidJUnit4::class)
class RepositoryTest : TestCase() {

    private lateinit var repository: Repository
    private lateinit var fakeDataSource: FakeDataSource

    @Before
    fun obtainRepoInstance() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        fakeDataSource = FakeDataSource()
        repository = Repository.getInstance(fakeDataSource, context)
    }

    @Test
    fun getMyProducts() = runBlocking {
        val tasks = repository.getMyProducts()
        assertEquals(1, tasks.products?.size)
    }
}