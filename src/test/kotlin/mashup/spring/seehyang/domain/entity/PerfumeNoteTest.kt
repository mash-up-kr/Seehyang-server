package mashup.spring.seehyang.domain.entity.perfume

import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.NoteRepository
import mashup.spring.seehyang.repository.perfume.PerfumeNoteRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager

@DataJpaTest
class PerfumeNoteTest @Autowired constructor(
    val perfumeRepository: PerfumeRepository,
    val noteRepository: NoteRepository,
    val brandRepository: BrandRepository,
    val perfumeNoteRepository: PerfumeNoteRepository,
    val entityManager: EntityManager
) {
    private val testBrand = Brand(
        name ="Chanel",
        koreanName = "샤넬"
    )

    private val testNote = Note(
        name = "steel",
        koreanName = "강철"
    )

    private val testPerfume = Perfume(
        name = "theBestPerfume",
        koreanName = "최고의향수",
        type = PerfumeType.EAU_DE,
        gender = Gender.BOTH,
        thumbnailUrl = "best/best.jpg",
        brand = testBrand
    )

    @BeforeEach
    fun before() {
        brandRepository.save(testBrand)
        perfumeRepository.save(testPerfume)
        noteRepository.save(testNote)

        val perfumeNote = PerfumeNote(
            perfume = testPerfume,
            note = testNote,
            type = NoteType.TOP
        )

        perfumeNoteRepository.save(perfumeNote)
        entityManager.clear()
    }

    @Test
    fun get_note_from_perfume() {
        val findPerfume = perfumeRepository.findAll()[0]

        assertEquals(1, findPerfume.viewNotes().size)
        assertEquals("steel", findPerfume.viewNotes()[0].note.name)
    }
}