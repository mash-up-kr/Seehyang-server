package mashup.spring.seehyang

import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.community.StoryTag
import mashup.spring.seehyang.domain.entity.community.Tag
import mashup.spring.seehyang.domain.entity.user.OAuthType
import mashup.spring.seehyang.domain.entity.perfume.Brand
import mashup.spring.seehyang.domain.entity.perfume.Gender
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.perfume.PerfumeType
import mashup.spring.seehyang.domain.entity.user.User

fun createTestUser(isSetDefaultInfo: Boolean) : User{
    val user = User(
        email = "test@test.com",
        oAuthType = OAuthType.GOOGLE
    )
    if(isSetDefaultInfo){

        user.changeGender(Gender.BOTH)
        user.changeAge(99)
        user.changeNickname("test")
    }
    return user
}

fun createTestStory(image: Image, perfume: Perfume, user: User): Story {

    val story = Story(isOnlyMe = false,
                      image = image,
                      perfume = perfume,
                      user= user)

    return story
}



fun createTestBrand() : Brand{
    return Brand(
        name ="Chanel",
        koreanName = "샤넬"
    )
}

fun createTestPerfume(brand: Brand) : Perfume{
    return Perfume(
        name = "theBestPerfume",
        koreanName = "최고의향수",
        type = PerfumeType.EAU_DE,
        gender = Gender.BOTH,
        thumbnailUrl = "best/best.jpg",
        brand = brand
    )
}

fun createTestImage(): Image{
    return Image(
        url = "image"
    )
}