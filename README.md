# block-file-extensions
파일 확장자 차단 프로젝트

### 개발환경

* IDE : sts 3.9.7
* OS : window 10
* SpringBoot 2.4.5
* Java 8
* maven


### 실행방법
```
1. IDE 이용시 :  해당 프로젝트를 import 후에 main 함수( BlockFileExtensionsApplication.java ) 실행
   ( 그 외 : 폴더 block-file-extensions에서 mvn clean 후 target 폴더의 block-file-extensions-1.0.jar 실행 )
2. http://localhost:8080/file/extensions/setting 에서 실행
```

### db관련 정보

* h2 in memory db 사용 ( 재접속시 db 유지 하기위해서 memory db 사용 안 할 시 application.yml hibernate.ddl-auto 주석 제거 및 url 주석 변경 ) 
![db접속](https://user-images.githubusercontent.com/81953480/123734322-157d4980-d8d8-11eb-98e0-a0634b1589c1.png)
![fixedTable](https://user-images.githubusercontent.com/81953480/123734330-17dfa380-d8d8-11eb-86b6-04a06f7375ee.png)
![customTable](https://user-images.githubusercontent.com/81953480/123734333-1910d080-d8d8-11eb-9fbc-7556700c0a3d.png)



