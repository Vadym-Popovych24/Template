package com.android.template.ui.compose.viewmodel

import com.android.template.manager.interfaces.NewsManager
import com.android.template.testutils.ViewModelTest
import com.android.template.testutils.createFlowOfProgress
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ComposeViewModelFlowTest : ViewModelTest() {

    private val newsManager: NewsManager = mockk()
    private val viewModel = ComposeViewModel(newsManager)

    @Test
    fun `numbersFlow should emit values from newsManager`() = runTest {

        every { newsManager.getProgress() } returns flowOf(1, 2, 3)

        val result = viewModel.numbersFlow().toList()
        val expectedNumbers = createFlowOfProgress(3).toList()
        assertEquals(expectedNumbers, result)
    }
}