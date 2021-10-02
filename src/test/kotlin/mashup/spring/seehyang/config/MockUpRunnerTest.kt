package mashup.spring.seehyang.config

import mashup.spring.seehyang.repository.user.UserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MockUpRunnerTest @Autowired constructor(
    val userRepository: UserRepository
) {

    @Test
    fun run() {
        val adam = userRepository.findById(1)
        val eve = userRepository.findById(2)

        assertEquals("Adam", adam.get().nickname)
        assertEquals(1, adam.get().id)
        assertEquals("Eve", eve.get().nickname)
        assertEquals(2, eve.get().id)

    }
}