package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.domain.entity.community.Story
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StoryRepository : JpaRepository<Story, Long>{

    /**
     * 같은 findById 동작을 원하지만 EntityGraph 를 다르게 적용 하고 싶다면 어떻게?
     * QueryDSL 도입의 이유가 될듯. -> CQRS
     * */
    @EntityGraph(attributePaths = ["user","image","perfume", "storyLikes", "storyLikes.user"])
    override fun findById(@Param("id") id:Long): Optional<Story>

    //accessible story: 내 스토리 + (남이쓴 스토리 & 나만보기아님)
    @EntityGraph(attributePaths = ["user","image","perfume"])
    @Query("select s from Story s " +
                   "where (s.user.id = :userId or (s.user.id <> :userId and s.isOnlyMe = false)) "+ // 남이 쓴 스토리
                        "and s.perfume.id = :perfumeId " +
                        "and s.id < :cursor ")
    fun findAccessibleStoriesWithUserByPerfumeId(@Param("perfumeId") perfumeId: Long, @Param("userId") userId: Long, @Param("cursor") cursor: Long, pageable: Pageable): List<Story>

    @EntityGraph(attributePaths = ["user","image","perfume"])
    @Query("select s from Story s " +
                   "where s.isOnlyMe = false "+ // user 정보가 없으므로 나만보기 false 인 것만 가져온다.
                   "and s.perfume.id = :perfumeId " +
                   "and s.id < :cursor ")
    fun findPublicStoriesByPerfumeId(@Param("perfumeId") perfumeId: Long, @Param("cursor") cursor: Long, pageable: Pageable): List<Story>

    @EntityGraph(attributePaths = ["user","image","perfume"])
    @Query("select s from Story s " +
                   "where s.isOnlyMe = false")
    fun findPublicStories(pageable: Pageable): List<Story>


    @EntityGraph(attributePaths = ["user","image","perfume"])
    @Query("select s from Story s " +
                   "where (s.user.id = :userId or (s.user.id <> :userId and s.isOnlyMe = false)) "+
                   "and s.id in :storyIds")
    fun findAccessibleStoriesWithUser(@Param("storyIds") storyIds: List<Long>, @Param("userId") userId: Long): List<Story>

    @EntityGraph(attributePaths = ["user","image","perfume"])
    @Query("select s from Story s " +
                   "where s.isOnlyMe = false "+
                   "and s.id in :storyIds")
    fun findPublicStories(@Param("storyIds") storyIds: List<Long>): List<Story>

    @EntityGraph(attributePaths = ["user","image","perfume"])
    @Query("select s from Story s " +
                   "where (s.user.id = :userId or (s.user.id <> :userId and s.isOnlyMe = false)) "+
                   "and s.id = :storyId")
    fun findAccessibleStoryWithUser(@Param("storyId") storyId: Long, @Param("userId") userId: Long): Story?

    @EntityGraph(attributePaths = ["user","image","perfume"])
    @Query("select s from Story s " +
                   "where s.isOnlyMe = false "+
                   "and s.id = :storyId")
    fun findPublicStory(@Param("storyId") storyId: Long): Story?



    /**
     * ======Deprecates========
     */

    @EntityGraph(attributePaths = ["user","image","perfume"])
    fun findTop10ByOrderByLikeCountDesc(): List<Story>

    @EntityGraph(attributePaths = ["user","image","perfume"])
    fun findTop10ByPerfumeIdOrderByLikeCountDesc(perfumeId: Long?): List<Story>

    @EntityGraph(attributePaths = ["user","image","perfume"])
    @Query("select s from Story s " +
                   "where s.perfume.id = :perfumeId and s.id < :cursor " +
                   "order by s.id desc")
    fun findStoryByPerfumeId(@Param("perfumeId") perfumeId: Long, @Param("cursor") cursor: Long, pageable: Pageable) : List<Story>


    @EntityGraph(attributePaths = ["user","image","perfume"])
    fun findTop10ByPerfumeIdOrderByIdDesc(perfumeId: Long) : List<Story>


    @EntityGraph(attributePaths = ["user","image","perfume"])
    @Query("select s " +
                   "from Story s " +
                   "where s.id in :ids")
    fun findByIds(@Param("ids") storyIds: List<Long>): List<Story>

}