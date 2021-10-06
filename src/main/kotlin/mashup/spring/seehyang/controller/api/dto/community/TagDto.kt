package mashup.spring.seehyang.controller.api.dto.community

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.domain.entity.community.Tag

data class TagDto(
    @JsonIgnore
    val tag: Tag,

    val id: Long? = tag.id,
    val contents: String = tag.contents
)