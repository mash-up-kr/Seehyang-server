package mashup.spring.seehyang.controller.api.dto

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

//TODO: 왜 value 어노테이션이 안먹지?
//TODO: 사이드 플젝땐 yaml 파일 분리하고 리소스로더로 yaml 불러와서 @Value 최상위필드로 꽂아서 됐는데
//TODO: 클래스 로더 문제인가?
fun addBucketUrl(url:String):String = "https://elasticbeanstalk-ap-northeast-2-306614265263.s3.ap-northeast-2.amazonaws.com/"+url

