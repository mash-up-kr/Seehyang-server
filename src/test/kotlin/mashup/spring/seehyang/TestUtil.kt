package mashup.spring.seehyang

import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.perfume.Brand
import mashup.spring.seehyang.domain.entity.perfume.Gender
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.user.User

fun createTestUser() : User{
    return User(
        gender = Gender.BOTH,
        age = 99,
        nickname = "test",
        email = "test@test.com"
    )
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