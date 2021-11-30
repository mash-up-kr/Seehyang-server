package mashup.spring.seehyang.domain.entity.perfume

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.util.isOnlyEngSpaceNumber
import mashup.spring.seehyang.util.isOnlyKoreanSpaceNumber
import javax.persistence.*

@Entity
class Perfume(
    id: Long? = null,

    name: String,

    koreanName: String,

    type: PerfumeType,

    gender: Gender,

    thumbnailUrl: String,

    brand: Brand,

) : BaseTimeEntity() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = id

    //TODO 프로퍼티로 쓰면 코드의 응집성 떨어지는 느낌과 Domain 규칙을 코드로 보여주지 못한다는 생각
    var name: String = name
        protected set

    var koreanName: String = koreanName
        protected set

    @Enumerated(EnumType.STRING)
    val type: PerfumeType = type

    @Enumerated(EnumType.STRING)
    val gender: Gender = gender

    // TODO : Url 로 납둬도 괜찮을까?
    var thumbnailUrl: String = thumbnailUrl
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    val brand: Brand = brand

    var likeCount: Int = 0
        protected set

    @OneToMany(mappedBy = "perfume")
    val accords: MutableList<PerfumeAccord> = mutableListOf()


    @OneToMany(mappedBy = "perfume")
    val notes: MutableList<PerfumeNote> = mutableListOf()


    @OneToMany(mappedBy = "perfume")
    val stories: MutableList<Story> = mutableListOf()


    @OneToMany(mappedBy = "perfume", cascade = [CascadeType.ALL], orphanRemoval = true)
    val perfumeLikes : MutableList<PerfumeLike> = mutableListOf()




    /**
     * ============= Public Methods =================
      */

    fun changePerfumeNames(name:String?, koreanName: String?){
        if(name != null){
            changeName(name)
        }
        if(koreanName != null){
            changeKoreanName(koreanName)
        }
    }


    fun changeThumbnailUrl(url: String){
        //TODO: Validation 필요?
        this.thumbnailUrl = url
    }

    // TODO: 나중에 CQRS 적용하면 좋을 듯
    fun likePerfume(user: User) :Boolean {
        val perfumeLike = perfumeLikes.find{it.user.id == user.id}

        var currentLike = false

        if(perfumeLike == null){
            perfumeLikes.add(PerfumeLike(user = user, perfume = this))
            currentLike = true
        }else{
            perfumeLikes.remove(perfumeLike)
        }

        likeCount = perfumeLikes.size

        return currentLike
    }

    /**
     * ============== Public Methods =============
     */

    private fun changeName(name: String){
        verifyEnglishPerfumeName(name)
        this.name = name
    }

    private fun changeKoreanName(koreanName: String){
        verifyKoreanPerfumeName(koreanName)
        this.koreanName = koreanName
    }

    private fun verifyEnglishPerfumeName(name: String) {
        isOnlyEngSpaceNumber(name)
    }

    private fun verifyKoreanPerfumeName(koreanName: String) {
        isOnlyKoreanSpaceNumber(koreanName)
    }


}