package mashup.spring.seehyang

import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.perfume.Brand
import mashup.spring.seehyang.domain.entity.perfume.Gender
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.perfume.PerfumeType
import mashup.spring.seehyang.domain.entity.user.User

fun createTestUser(gender: Gender = Gender.BOTH,
                   age:Short = 99,
                   nickname: String = "test",
                   profile:Image = createTestImage())
= User(gender = gender,
       age = age,
       nickname = nickname,
       email = "${nickname}@test.com",
       profileImage = profile)



fun createTestBrand(name:String = "Chanel", koreanName: String = "샤넬")
= Brand(name = name,
        koreanName = koreanName)


fun createTestPerfume(brand: Brand,
                      name: String = "theBestPerfume",
                      koreanName: String ="최고의향수",
                      type:PerfumeType = PerfumeType.EAU_DE,
                      gender: Gender = Gender.BOTH,
                      thumbnailUrl: String = "best/best.jpg",)
= Perfume(name = name,
          koreanName = koreanName,
          type = type,
          gender = gender,
          thumbnailUrl = thumbnailUrl,
          brand = brand)


fun createTestImage(url: String = "image")
= Image(url = url)


fun createTestStory(user: User = createTestUser(),
                    image: Image = createTestImage(),
                    perfume: Perfume = createTestPerfume(createTestBrand()))
= Story(user = user,
        image = image,
        perfume = perfume)
