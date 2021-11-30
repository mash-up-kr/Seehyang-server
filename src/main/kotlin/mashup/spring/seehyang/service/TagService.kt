package mashup.spring.seehyang.service

import mashup.spring.seehyang.domain.TagDomain
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class TagService(
    val tagDomain: TagDomain
) {

    // TODO: NOT NEEDED SERVICE YET.
    // TODO: Tag Service would be used "search by tag" after
}