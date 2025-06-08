package com.example.exerciseactivity.ui.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.exerciseactivity.data.GetParkDataUseCase
import com.example.exerciseactivity.data.response.ImportDate
import com.example.exerciseactivity.data.response.Park
import com.example.exerciseactivity.data.response.ParkResult
import com.example.exerciseactivity.util.observeOnce
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomepageViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomepageViewModel
    private lateinit var getParkDataUseCase: GetParkDataUseCase

    private val testDispatcher = StandardTestDispatcher()

    private val fakePark = Park(
        id = 1,
        importdate = ImportDate(
            date = "2022-05-09 14:47:38.971190",
            timezoneType = 3,
            timezone = "Asia/Taipei"
        ),
        no = "1",
        category = "戶外區",
        name = "臺灣動物區",
        picUrl = "http://www.zoo.gov.tw/iTAP/05_Exhibit/01_FormosanAnimal.jpg",
        info = "臺灣位於北半球，北迴歸線橫越南部，造成亞熱帶溫和多雨的氣候。又因高山急流、起伏多樣的地形，因而在這蕞爾小島上，形成了多樣性的生態系，孕育了多種不同生態習性的動、植物，豐富的生物物種共存共榮於此，也使臺灣博得美麗之島「福爾摩沙」的美名。臺灣動物區以臺灣原生動物與棲息環境為展示重點，佈置模擬動物原生棲地之生態環境，讓動物表現如野外般自然的生活習性，引導民眾更正確地認識本土野生動物，為園區環境教育的重要據點。藉由提供動物寬廣且具隱蔽的生態環境，讓動物避開人為過度的干擾，並展現如野外般自然的生活習性和行為。展示區以多種臺灣的保育類野生動物貫連成保育廊道，包括臺灣黑熊、穿山甲、歐亞水獺、白鼻心、石虎、山羌等。唯有認識、瞭解本土野生動物，才能愛護、保育牠們，並進而珍惜我們共同生存的這塊土地！",
        memo = "",
        geo = "MULTIPOINT ((121.5805931 24.9985962))",
        url = "https://youtu.be/QIUbzZ-jX_Y"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getParkDataUseCase = mockk()
        viewModel = HomepageViewModel(getParkDataUseCase, SavedStateHandle())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should emit loading and full park data`() = runTest {
        coEvery { getParkDataUseCase.invoke() } returns flow {
            emit(ParkResult(null, null, null, null, listOf(fakePark)))
        }
        viewModel.init()
        advanceUntilIdle()
        viewModel.displayPark.observeOnce {
            assertEquals(listOf(fakePark), it)
        }
        viewModel.showLoading.observeOnce {
            assertEquals(false, it)
        }
    }

    @Test
    fun `initTransport should update displayTransport`() {
        viewModel.initTransport()

        viewModel.displayTransport.observeOnce {
            assertEquals(
                "https://www.zoo.gov.taipei/News_Content.aspx?n=9F0E68F3EC5B5751&sms=F3B2EF982C0582B3&s=01BD7568D718DAED",
                it
            )
        }
    }

    @Test
    fun `initMap should update displayMap`() {
        viewModel.initMap()

        viewModel.displayMap.observeOnce {
            assertEquals(
                "https://www.zoo.gov.taipei/News_Content.aspx?n=E8BB5E02604E3BC7&sms=F3B2EF982C0582B3&s=BC8193C264494D92",
                it
            )
        }
    }

    @Test
    fun `onDownloadClick should post download URL`() {
        viewModel.onDownloadClick()

        viewModel.displayDownload.observeOnce {
            assertEquals(
                "https://www-ws.gov.taipei/001/Upload/432/relpic/21511/8040477/88855b76-085e-4d95-8b9e-7f7132d20ac9.jpg",
                it
            )
        }
    }

    @Test
    fun `onParkClick should post selected park`() {
        viewModel.onParkClick(fakePark)

        viewModel.navigationToParkDetail.observeOnce {
            assertEquals(fakePark, it)
        }
    }
}
