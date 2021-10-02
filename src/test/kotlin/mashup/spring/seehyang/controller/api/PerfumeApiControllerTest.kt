package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.domain.entity.perfume.*
import mashup.spring.seehyang.service.PerfumeService
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@ExtendWith(MockitoExtension::class)
@SpringBootTest
class PerfumeApiControllerTest @Autowired constructor(
    val perfumeApiController: PerfumeApiController,
) {

    @MockBean
    private lateinit var perfumeService: PerfumeService

    @Test
    fun getPerfume() {
        val testPerfume = testPerfume()
        given(perfumeService.get(anyLong())).willReturn(testPerfume)

        val actual: SeehyangResponse<PerfumeDto> = perfumeApiController.getPerfumeDetail(0L)

        assertEquals(actual.data!!.id, testPerfume.id)
        assertEquals(actual.data!!.brandId, testPerfume.brand.id)
        assertEquals(actual.data!!.notes.base.size, 1)
        assertEquals(actual.data!!.notes.middle.size, 1)
        assertEquals(actual.data!!.notes.top.size, 0)
        assertEquals(actual.data!!.accords.size, 1)
    }

    fun testPerfume() :Perfume {
        val brand = Brand(1L, "Chanel", "샤넬")
        val baseNote = Note(1L, "base", "베이스", NoteType.BASE)
        val middleNote = Note(2L, "top", "탑", NoteType.MIDDLE)
        val accord = Accord(1L, "white", "화이트")
        val perfume = Perfume(1L, "perfume", "향수", type = PerfumeType.EAU_DE, Gender.BOTH, "", brand)

        perfume.accords.add(PerfumeAccord(1L, perfume, accord))
        perfume.notes.add(PerfumeNote(1L, perfume, baseNote))
        perfume.notes.add(PerfumeNote(2L, perfume, middleNote))

        return perfume
    }
}