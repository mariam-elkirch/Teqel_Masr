package com.example.teqelmasr.model

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teqelmasr.displaySellerProducts.viewModel.MyProductsViewModel
import com.example.teqelmasr.displaySellerProducts.viewModel.MyProductsViewModelFactory
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [30])
@RunWith(AndroidJUnit4::class)
class ViewModelTest : TestCase() {
    private lateinit var repository: Repository
    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var viewModel: MyProductsViewModel
    private  lateinit var factory : MyProductsViewModelFactory
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun obtainRepoInstance() {
        fakeDataSource = FakeDataSource()
        repository = Repository.getInstance(fakeDataSource, context)
    }

    @Test
    fun getMyProducts(){
        val viewModel = MyProductsViewModel(repository)
        viewModel.myProducts?.observeForever(){
            assertEquals(0,it?.size)
        }
    }
}