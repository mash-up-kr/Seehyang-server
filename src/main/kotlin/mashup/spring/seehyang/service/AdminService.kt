package mashup.spring.seehyang.service

import mashup.spring.seehyang.domain.StoryDomain
import mashup.spring.seehyang.domain.UserDomain
import mashup.spring.seehyang.domain.entity.community.Story
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class AdminService(
    val userDomain: UserDomain,
    val storyDomain: StoryDomain
) {

    @Transactional(readOnly = true)
    fun getAdminStoryDetail(storyId: Long): Story {

        val story = storyDomain.adminGetStoryById(storyId)

        return story
    }
}