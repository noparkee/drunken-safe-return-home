### MainActivity.java   
- 테스트 위한 메인 화면   
- 시작하면 startService() 함수 실행 -> MyService.class 실행   

1) exit -> ExitRoom.class 실행
2) dbbtn -> PermissionCheckActivity.class 실행 
3) makeroom -> MakeRoom.class 실행
4) enterroom -> EnterRoom.class 실행
5) test -> AddnewUserDB.class 실행
6) send -> sendSms 함수 실행


### MyService.java
- 포그라운드 알림 설정
- 알림 채널 생성
- 서비스 종료 시 foreground 종료하도록.. > 여기는 수정 필요

### ExitRoom.java
- 방에서 나갈 때 DB 변경
- 방에서 나갈 때 유저 정보에서 방 정보를 삭제
- 방 정보에서 유저 정보도 삭제
- num이 0이 되면 방 삭제

### PermissionCheckActivity.java
- 퍼미션 체크

### MakeRoom.java
- 방을 만들어서 방을 처음 만든 사람은 방에 들어갈 때 DB 관리

### EnterRoom.java
- 방에 초대된 후 수락해서 방에 들어갈 때

### AddnewUserDB.java
- 새로 가입했을 때 유저 정보 추가

### ReadRoomid.java
- 유저가 현재 들어가 있는 방 목록을 DB에서 읽기
- 새로 추가되거나 변경이 있는 방의 id를 ReadRoomDatabase.java로 넘김

### ReadRoomDatabase.java
- ReadRoomid.java에서 전달 받은 방 id와 관련한 정보를 DB에서 읽음
- 약속 장소와 약속 날짜를 읽음

### ReadUserDatabase.java
- 유저가 개인정보를 변경했을 때의 DB 변경

### StateChange.java
- headsup 알림 설정
- 집에 도착여부
- 같은 방에 있는 사람 중 누군가 집에 도착하지 못했다면 알림으로 알려줘야함.

### SetAlarm.java / Alarm.java / AlarmReceiver.java
- 누군가 도착하지 않았다고 알림 띄우기
- 여기는 삭제될 확률 높음

### AutoRun.java
- 재부팅시 자동으로 재시작 하도록
