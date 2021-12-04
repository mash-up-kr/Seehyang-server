package mashup.spring.seehyang.domain

import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.community.StoryTag
import mashup.spring.seehyang.domain.entity.community.Tag
import mashup.spring.seehyang.domain.enums.Domain
import mashup.spring.seehyang.exception.InternalServerException
import mashup.spring.seehyang.repository.community.TagRepository

@Domain
class TagDomain(
    private val tagRepository: TagRepository
) {

    /**
     * =========== Responsibilities =============
     */

    fun addTagsToStory(story: Story, tags: List<String>) {

        tags.filter{isExistedTag(it)}
            .forEach{story.addStoryTag(StoryTag(story = story, tag = tagRepository.findByContents(it)?:throw InternalServerException(SeehyangStatus.INVALID_TAG_ENTITY)))}


        tags.filter{!isExistedTag(it)}
            .map{
                tagRepository.save(Tag(contents = it))
            }.forEach {
                story.addStoryTag(StoryTag(story = story, tag = it))
            }

    }

    private fun isExistedTag(contents: String) = tagRepository.existsByContents(contents)

}