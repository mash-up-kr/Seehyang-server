package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StoryRepository : JpaRepository<Story, Long>{

    @Query("select s " +
           "from Story s " +
               "join s.user u " +
               "join u.profileImage pi " +
               "join s.perfume p " +
               "join s.image i " +
           "where p.id = :perfumeId " +
           "order by s.likeCount DESC")
    fun findByPerfumeIdOrderByLike(@Param("perfumeId") perfumeId: Long, pageable: Pageable) : Page<Story>

    @Query("select s " +
                   "from Story s " +
                   "join s.user u " +
                   "join u.profileImage pi " +
                   "join s.perfume p " +
                   "join s.image i " +
                   "where p.id = :perfumeId " +
                   "order by s.createdAt DESC")
    fun findByPerfumeIdOrderByDate(@Param("perfumeId") perfumeId: Long, pageable: Pageable) : Page<Story>

    @Query("select s " +
                   "from Story s " +
                   "join s.user u " +
                   "join u.profileImage pi " +
                   "join s.perfume p " +
                   "join s.image i " +
                   "where p.id = :perfumeId " +
                   "order by s.commentCount DESC")
    fun findByPerfumeIdOrderByComment(@Param("perfumeId") perfumeId: Long, pageable: Pageable) : Page<Story>

    fun findTop10ByPerfumeIdOrderByLikeCountDesc(perfumeId: Long?): List<Story>

    // 임시 메소드
    fun findTop10By() : List<Story>

}