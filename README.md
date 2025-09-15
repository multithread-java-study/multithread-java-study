# 자바 심화 스터디
---
> java 언어의 핵심 고급 주제 중 하나인 멀티스레딩과 동시성을 깊이 학습하고 실습하는 스터디입니다. '김영한의 실전 자바- 고급 1편, 멀티스레드와 동시성'이란 강의를 듣고 정리하며 더 나아가 심화 내용을 공부하여 발표하는 스터디입니다.
> 
## Members
---
|                           <a href="https://github.com/gimin0226"><img src="https://github.com/gimin0226.png" width=120/></a>                           |                          <a href="https://github.com/seokjun01"><img src="https://github.com/seokjun01.png" width=120/></a>                           |                       <a href="https://github.com/kwak513"><img src="https://github.com/kwak513.png" width=120 /></a>                        |                         <a href="https://github.com/goodjunseon"><img src="https://github.com/goodjunseon.png" width=120/></a>                          |                         <a href="https://github.com/hwangrock"><img src="https://github.com/hwangrock.png" width=120/></a>                          |                         <a href="https://github.com/zldzldzz"><img src="https://github.com/zldzldzz.png" width=120/></a>                          |
|:-----------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|
|                                     <a href="https://github.com/gimin0226">김기민</a>                                     |                                 <a href="https://github.com/seokjun01">문석준</a>                                  |                                  <a href="https://github.com/kwak513">곽채연</a>                                  |                                   <a href="https://github.com/goodjunseon">박준선</a>                                    |                                   <a href="https://github.com/hwangrock">황규민</a>                                    |                                   <a href="https://github.com/zldzldzz">이원진</a>                                    |

## 목표

1. 자바의 스레드 모델과 JVM이 스레드를 관리하는 방식을 이해한다.
2. 교착상태(Deadlock), 경쟁 조건(Race Condition) 등 전형적인 동시성 문제를 실습과 함께 분석한다.
3. 운영체제의 동시성 이론과 자바 코드 구현을 연결하여 CS 이론과 코드 간의 겝을 메운다.
4. 멀티스레드 환경에서의 성능 최적화와 안전성을 동시에 고려하는 사고 방식을 기른다.

## 스터디 커리큘럼

### 0주차
- 자기 소개 및 스터디 진행 방식 토의

### 1주차
- 섹션 2. 프로세스와 스레드 소개
- 섹션 3. 스레드 생성과 실행
- 섹션 4. 스레드 제어와 생명 주기 1

### 2주차
- 섹션 5. 스레드 제어와 생명 주기 2
- 섹션 6. 메모리 가시성
- 섹션 7. 동기화 – synchronized

### 3주차
- 섹션 8. 고급 동기화 – concurrent.Lock
- 섹션 9. 생산자 소비자 문제 1
- 섹션 10. 생산자 소비자 문제 2

### 4주차
- 섹션 11. CAS – 동기화와 원자적 연산
- 섹션 12. 동시성 컬렉션

### 5주차
- 섹션 13. 스레드 풀과 Executor 프레임워크 1
- 섹션 14. 스레드 풀과 Executor 프레임워크 2

## Directory Structure
---
```
│
├─ multithread-java-study
│     │
│     │
│     ├─ cs-questions // 브랜치 나눠서 PR 요청하기, 브랜치 명은 '이름-주차' ex) 'gimin-01'
│     │     ├─  Week01.md // 1주차
│     │     ├─  Week02.md // 2주차
│     │     ├─  Week03.md // 3주차
│     │     ├─  Week04.md // 4주차
│     │     └─  Week05.md // 5주차  
│     │
│     │
│     ├─ gimin (dir) // 본인의 핸들명
│     │     ├─  Week01 (dir) // code는 브랜치 나눠서 PR 진행, 나머지 파일은 main 브랜치에서 커밋
│     │     │    ├─ section1.md // 섹션을 듣고 정리한 내용. 확장자는 `.md`, 제목은 'section숫자'
│     │     │    ├─ section2.md
│     │     │    ├─ section3.md
│     │     │    ├─ 발표자료.md // 추가적으로 공부하여 발표할 내용. 확장자는 `.md`, 제목은 '발표자료'
│     │     │    └─ code (dir)  
│     │     │        ├─ problem1.java // 과제로 나온 문제 코드. 확장자는 `.java`, 제목은 'problem숫자'
│     │     │        ├─ problem2.java 
│     │     │        └─ problem3.java 
│     │     │
│     │     ├─  Week02 (dir) 
│     │     │    ├─ section4.md 
│     │     │    ├─ section5.md
│     │     │    ├─ section6.md
│     │     │    ├─ 발표자료.md 
│     │     │    └─ code (dir)
│     │     │        ├─ problem1.java 
│     │     │        ├─ problem2.java 
│     │     │        └─ problem3.java
│     │     │
│     │     │
│     │     └─ ... 이하 동일
│     │   
│     │   
│     ├─ seokjun (dir) // 본인의 핸들명, .. 이하 동일
│
│
```
