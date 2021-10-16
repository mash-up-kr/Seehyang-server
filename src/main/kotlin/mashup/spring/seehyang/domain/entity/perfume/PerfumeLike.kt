package mashup.spring.seehyang.domain.entity.perfume

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import mashup.spring.seehyang.domain.entity.user.User
import javax.persistence.*

@Entity
class PerfumeLike(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /**
     *  ========== Many to One ==========
     *  User, Post
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    val perfume: Perfume

) : BaseTimeEntity()