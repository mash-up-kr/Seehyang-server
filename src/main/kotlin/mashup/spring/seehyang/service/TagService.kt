package mashup.spring.seehyang.service

import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.community.StoryTag
import mashup.spring.seehyang.domain.entity.community.Tag
import mashup.spring.seehyang.repository.community.StoryTagRepository
import mashup.spring.seehyang.repository.community.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class TagService(
    val tagRepository: TagRepository,
    val storyTagRepository: StoryTagRepository
) {

    fun addTagsToStory(story: Story, tags: List<String>):Unit{

        tags.filter{isExistedTag(it)}
                    .forEach{storyTagRepository.save(StoryTag(story = story,
                                                              tag = tagRepository.findByContents(it)))}

        tags.filter{!isExistedTag(it)}
            .map{tagRepository.save(Tag(contents = it))}
            .forEach{storyTagRepository.save(StoryTag(story = story, tag = it))}

    }

    private fun isExistedTag(contents: String) = tagRepository.existsByContents(contents)
}