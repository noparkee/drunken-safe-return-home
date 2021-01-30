# 2021.01.10

### 1. Android Studio 설치
### 2. Git에서 코드 다운받기
### 3. 진행상태 정리
>java
> > Database:  
*사용자에게 입력받은 id, nickname, address, phone을 DB에 기록함.(Database(id, nickname), Database(address, phone)    
> > GlobalApplication:  
*초기화 부분. Kakao Sdk 초기화.   
> > HomeActivity: 
*로그인 후 표시되는 홈 화면.   
*DB에서 nickname, 프로필 이미지 읽어와서 표시.    
> > InfoActivity: 
*address와 contect를 DB에 기록(writeDatabase(address, contact))  
*HomeActivity로 이동.  
> > MainActivity:   
*Kakao 로그인 API. 
*id, nickname, profileImage를 DB에 기록(writeDatabase(id, nickname, profileImage))  
*로그인 성공 후 InfoActivity로 이동.  
> > SplashActivity:   
*앱 시작했을 때의 첫 화면.  
> > StoreActivity:   
*Firebase에 형식에 맞춰서 기록.   
*writeDatabase(id, nickname, profileImage), writeDatabase(Address, contact) 구현됨  


>res/layout
> > activity_home.xml:  
*HomeActivity.java  
> > activity_info.xml:  
*InfoActivity.java  
> > activity_login.xml:   
*아직 코드 작성 X  
> > activity_main.xml:  
*MainActivity.java  

# 2021.01.17
### 1. 이름 안 헷갈리게 정리하기  
### 2. Kotlin 코드 이해할정도로는 문법 익히기  
*https://developer.android.com/courses/pathways/kotlin-for-java?hl=ko
*https://thdev.tech/kotlin/2016/08/02/Basic-Kotlin-01/
### 3. 메시지 API, 링크 API
*https://developers.kakao.com/docs/latest/ko/message/android
### 4. HomeActivity 이후 어떻게 연결할 것인지 생각하기(유빈언니 UI 물어보기)

# 2021.01.23
