package mashup.spring.seehyang.domain

import mashup.spring.seehyang.repository.community.TagRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class TagDomainTest @Autowired constructor(
    val tagRepository: TagRepository
) {


    /**
     * ======= addTagsToStory Test ========
     */

    @Test
    fun addTagsToStoryTest(){

    }

}