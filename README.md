# RS-shootingGame:airplane:

------

![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fmsmn1729%2FRS-shootingGame&count_bg=%2306A1F1&title_bg=%23555555&icon=iconify.svg&icon_color=%23FFFFFF&title=hits&edge_flat=false)
![imge](https://img.shields.io/badge/ProjectType-TeamProject-green)
![imge](https://img.shields.io/badge/Language-Java-yellow)
![imge](https://img.shields.io/badge/Tools-AndroidStudio-red)

## Refactoring🛠
- [x] 랭킹 등록 서버 복구
- [x] 불필요한 주석제거
- [x] 코드 정리

- ### GameState
  - [x] 충돌처리 메소드 추출

## 프로그램 소개:memo:
- 2020.07.20 Developed.

- 안드로이드 기반의 실시간 랭킹시스템 슈팅 게임입니다. (Android-based real-time ranking system shooting game.)

- 고전풍의 UI와 8비트 배경음악을 삽입하여 레트로 감성을 구현했습니다.

- 랜덤 아이템 박스(버프 or 디버프)컨텐츠를 통해 플레이어의 재미를 높히고자 했습니다. :smile:

------

## 개발 환경:computer:
![개발 환경](https://user-images.githubusercontent.com/59201008/95119599-28b71a80-0787-11eb-8f94-214e1cea63ec.png)

------

## 시스템 구성도:mailbox:
![시스템 구성도](https://user-images.githubusercontent.com/59201008/95119680-50a67e00-0787-11eb-8528-5d6b2d348550.png)

------

## 클래스 구조도 UML 다이어그램:notebook_with_decorative_cover:
- 기본틀, 스테이트 구조도
![기본틀, 스테이트 구조도](https://user-images.githubusercontent.com/59201008/95118834-e80ad180-0785-11eb-88f3-53690fbcb5fb.png)

- 그래픽 관련 구조도
![그래픽 관련 구조도](https://user-images.githubusercontent.com/59201008/95118891-053fa000-0786-11eb-9be8-de6e022d2d7a.png)

- 랜덤박스 구조도
![랜덤박스 구조도](https://user-images.githubusercontent.com/59201008/95118931-17214300-0786-11eb-9f40-bef32ca0cba2.png)

------

## 디자인 패턴
- State Pattern
![스테이트 패턴](https://user-images.githubusercontent.com/59201008/95119231-99116c00-0786-11eb-8386-ceb2baed78b5.png)

------

## Random item box
![랜덤박스 아이템](https://user-images.githubusercontent.com/59201008/95119814-8ba8b180-0787-11eb-8de8-a5ec3ad1d662.png)

------

## Player type
![플레이어 3종](https://user-images.githubusercontent.com/59201008/95119858-9bc09100-0787-11eb-8dbd-10a97cc062cc.png)

------

## Skill 설명
### Skill 1 : super missile

![필살기1](https://user-images.githubusercontent.com/59201008/95120024-dcb8a580-0787-11eb-8383-3c512088cda7.png)

### Skill 2 : explosion

![필살기2](https://user-images.githubusercontent.com/59201008/95120040-e4784a00-0787-11eb-8cfd-336e6844aec3.png)

### Skill 3 : 5 Seconds invincible

![필살기3](https://user-images.githubusercontent.com/59201008/95120056-e9d59480-0787-11eb-8c7d-08594de798c9.png)

------

## UI 정보
![게임화면 정보 출력](https://user-images.githubusercontent.com/59201008/95120110-007beb80-0788-11eb-97a4-b1aba80daa73.png)

![내가 추가 구현한 것](https://user-images.githubusercontent.com/59201008/95119253-a4649780-0786-11eb-8ac4-69c94b48854c.png)

------

## 시연 동영상
### Skill 1 : super missile:rocket:
- 배속 : 1.7
- 동영상 길이 : 30초
- 게임오버 후 유저가 닉네임을 입력하면 스코어보드에 랭킹등록이 되는 것을 확인할 수 있습니다. 

![필살기1(30초)](https://user-images.githubusercontent.com/59201008/95127713-6621a500-0793-11eb-9377-3bc6e6a1f906.gif)


### Skill 2 : explosion:fire:
- 배속 : 1.0
- 동영상 길이 : 7초

![필살기2(7초)](https://user-images.githubusercontent.com/59201008/95126959-37ef9580-0792-11eb-966c-17044383f52e.gif)

### Skill 3 : 5 Seconds invincible:snowflake:
- 배속 : 1.0
- 동영상 길이 : 12초
- 에뮬레이터의 사양이 낮아서 Skill 3만 조금 버벅임이 있으나, 실제 안드로이드 디바이스로는 매끄럽게 구동됨을 확인했습니다.

![필살기3(12초)](https://user-images.githubusercontent.com/59201008/95128182-024bac00-0794-11eb-9d1d-62c4f6346cdd.gif)

