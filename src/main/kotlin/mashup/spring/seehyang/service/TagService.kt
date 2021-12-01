package mashup.spring.seehyang.service

import mashup.spring.seehyang.domain.TagDomain
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class TagService(
    val tagDomain: TagDomain
) {

    // TODO 검색란에 태그로 검색하기 기능 추가
}