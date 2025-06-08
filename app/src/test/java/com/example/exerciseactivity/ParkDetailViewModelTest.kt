import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.exerciseactivity.data.GetParkDataInfoUseCase
import com.example.exerciseactivity.data.parameters.ParkDetailParameters
import com.example.exerciseactivity.data.response.ImportDate
import com.example.exerciseactivity.data.response.ImportDateInfo
import com.example.exerciseactivity.data.response.Park
import com.example.exerciseactivity.data.response.ParkInfo
import com.example.exerciseactivity.data.response.ParkResultInfo
import com.example.exerciseactivity.ui.parkdetail.ParkDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ParkDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getParkDataInfoUseCase: GetParkDataInfoUseCase

    @Mock
    private lateinit var displayParkObserver: Observer<Park?>

    private lateinit var viewModel: ParkDetailViewModel

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
        info = "臺灣位於北半球，北迴歸線橫越南部，造成亞熱帶溫和多雨的氣候。又因高山急流、起伏多樣的地形，因而在這蕞爾小島上，形成了多樣性的生態系，孕育了多種不同生態習性的動、植物，豐富的生物物種共存共榮於此，也使臺灣博得美麗之島「福爾摩沙」的美名。臺灣動物區以臺灣原生動物與棲息環境為展示重點，佈置模擬動物原生棲地之生態環境，讓動物表現如野外般自然的生活習性，引導民眾更正確地認識本土野生動物，為園區環境教育的重要據點。藉由提供動物寬廣且具隱蔽的生態環境，讓動物避開人為過度的干擾，並展現如野外般自然的生活習性和行為。展示區以多種臺灣的保育類野生動物貫連成保育廊道，包括臺灣黑熊、穿山甲、欧亚水獺、白鼻心、石虎、山羌等。唯有認識、瞭解本土野生動物，才能愛護、保育牠們，並進而珍惜我們共同生存的這塊土地！",
        memo = "",
        geo = "MULTIPOINT ((121.5805931 24.9985962))",
        url = "https://youtu.be/QIUbzZ-jX_Y"
    )

    val fakeCapybara = ParkInfo(
        id = 21,
        importDate = ImportDateInfo(
            date = "2022-05-09 14:47:38.971190",
            timezoneType = 3,
            timezone = "Asia/Taipei"
        ),
        nameCh = "水豚",
        summary = "",
        keywords = "",
        alsoknown = "",
        geo = "MULTIPOINT ((121.5827115 24.9967356))",
        location = "熱帶雨林區",
        poigroup = "",
        nameEn = "Capybara",
        nameLatin = "Hydrochoerus hydrochaeris",
        phylum = "脊索動物門",
        animalClass = "哺乳綱",
        order = "囓齒目",
        family = "豚鼠科",
        conservation = "暫無危機 LC",
        distribution = "南美洲的巴西及鄰近國家等區域。",
        habitat = "主要分布在南美洲的河流、水塘和草澤等水域。",
        feature = "1、 水豚是世界上最大的囓齒動物，體重甚至可以高達70公斤。\r\n 2、 前腳略短於後腿，腳掌呈部分蹼狀。",
        behavior = "1、 看起來笨重，其實牠們身手矯捷，在陸地上奔跑的速度很快，在水中敏捷的身手也十分出色。\r\n 2、 通常由一隻優勢的雄性領導10-20隻個體組成家族。",
        diet = "以水生植物、樹皮、青草及果實維生。",
        crisis = "雖然人們時常獵殺水豚取其肉以及毛皮，好在水豚繁殖很快，大多也都居住在受到保護的地區，目前屬於無危物種。",
        interpretation = "",
        themeName = "",
        themeUrl = "",
        adopt = "",
        code = "Capybara",
        pic01Alt = "水豚",
        pic01Url = "http://www.zoo.gov.tw/iTAP/03_Animals/RainForest/Capybara/Capybara_Pic01.jpg",
        pic02Alt = "水豚",
        pic02Url = "http://www.zoo.gov.tw/iTAP/03_Animals/RainForest/Capybara/Capybara_Pic02.jpg",
        pic03Alt = "水豚",
        pic03Url = "http://www.zoo.gov.tw/iTAP/03_Animals/RainForest/Capybara/Capybara_Pic03.jpg",
        pic04Alt = "水豚",
        pic04Url = "http://www.zoo.gov.tw/iTAP/03_Animals/RainForest/Capybara/Capybara_Pic04.jpg",
        pdf01Alt = "",
        pdf01Url = "",
        pdf02Alt = "",
        pdf02Url = "",
        voice01Alt = "",
        voice01Url = "",
        voice02Alt = "",
        voice02Url = "",
        voice03Alt = "",
        voice03Url = "",
        videoUrl = "https://youtu.be/pobwDQj3AQA",
        update = "########",
        cid = ""
    )


    private val fakeParameter = ParkDetailParameters(
        park = fakePark,
        offset = 0,
        limit = 20,
        showStart = 1,
        scope = "resourceAquire"
    )

    private val fakeParkInfoList = listOf(fakeCapybara)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ParkDetailViewModel(getParkDataInfoUseCase, fakeParameter.toSavedStateHandle())
        viewModel.displayPark.observeForever(displayParkObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.displayPark.removeObserver(displayParkObserver)
    }

    @Test
    fun `headerPark LiveData should emit fakePark when DataSourceFactory loads header`() = runTest {
        // Arrange: 模擬 UseCase 回傳假資料，包含 parks，觸發 header callback
        `when`(getParkDataInfoUseCase.invoke(any())).thenReturn(
            flowOf(
                ParkResultInfo(
                    limit = 20,
                    offset = 0,
                    count = 1,
                    sort = "",
                    parks = fakeParkInfoList
                )
            )
        )

        // Act: 重新設定 searchParameter 以觸發 getData -> DataSourceFactory -> header callback
        viewModel.javaClass.getDeclaredField("searchParameter").apply {
            isAccessible = true
            val liveData = get(viewModel) as MutableLiveData<ParkDetailParameters>
            liveData.postValue(
                ParkDetailParameters(
                    scope = "resourceAquire",
                    limit = 20,
                    offset = 0,
                    showStart = 1,
                    park = fakePark
                )
            )
        }

        // 執行 coroutine
        advanceUntilIdle()

        // Assert: 驗證 observer 收到的事件為 fakePark
        verify(displayParkObserver, atLeastOnce()).onChanged(fakePark)
    }

}

private fun ParkDetailParameters.toSavedStateHandle(): androidx.lifecycle.SavedStateHandle {
    val handle = androidx.lifecycle.SavedStateHandle()
    handle["Park"] = this.park
    return handle
}
