package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.*
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.perfume.Brand
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext
@DataJpaTest
class StoryRepositoryTest @Autowired constructor(
    val userRepository: UserRepository,
    val imageRepository: ImageRepository,
    val brandRepository: BrandRepository,
    val storyRepository: StoryRepository,
    val perfumeRepository: PerfumeRepository,

) {


    @BeforeEach
    fun setUP(){

        val userList : MutableList<User> = mutableListOf()
        val storyList: MutableList<Story> = mutableListOf()
        /**
         *  1. 3명의 유저가 있다.
         *  2. 각 유저가 스토리를 올린다.
         *  3. 각 스토리는 좋아요 수, 댓글 수, 생성 일시가 다르다.
         */
        // 세명의 유저 저장
        for(i in 1..3){
            val user = userRepository.save(createTestUser(nickname = "test${i}"))
            userList.add(user)
        }
        // 각 유저가 쓴 세 스토리 저장
        for(i in 1..3){
            val image = imageRepository.save(createTestImage("image${i}"))
            val brand = brandRepository.save(createTestBrand(name = "brand${i}", koreanName = "브랜드${i}"))
            val perfume = perfumeRepository.save(createTestPerfume(brand = brand, name = "perfume${i}", koreanName = "퍼퓸${i}"))
            val story = storyRepository.save(createTestStory(userList[i], image, perfume))
            storyList.add(story)
        }
        //스토리마다 댓글
        val story1 = storyList[0]
        story1.likeCount = 1;
        story1.likes.add()



    }

    @Test
    fun storyQueryOrderByLikeTest(){
        val story1 = storyRepository.findByPerfumeIdOrderByLike(perfumeId = perfume.id!!, PageRequest.of(0,5))

    }


}