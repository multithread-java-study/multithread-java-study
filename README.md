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
│     ├─ cs-questions // 공통 작업 파일들
│     │     ├─  Week01.md // 1주차
│     │     ├─  Week02.md // 2주차
│     │     ├─  Week03.md // 3주차
│     │     ├─  Week04.md // 4주차
│     │     └─  Week05.md // 5주차  
│     │
│     │
│     ├─ gimin0226/ // 본인의 핸들명(Github ID)
│     │     ├─  Week01/
│     │     │    ├─ section01.md // 섹션 정리(파일명: section숫자)
│     │     │    ├─ section02.md
│     │     │    ├─ section03.md
│     │     │    ├─ presentation.md # 발표 자료( 영문 파일명 talk.md로 통일)
│     │     │    └─ code/ 
│     │     │        ├─ problem01.java # 과제 코드(파일명: problem숫자)
│     │     │        ├─ problem02.java 
│     │     │        └─ problem03.java 
│     │     │
│     │     ├─  Week02/
│     │     │    ├─ section04.md 
│     │     │    ├─ section05.md
│     │     │    ├─ section06.md
│     │     │    ├─ 발표자료.md 
│     │     │    └─ code/
│     │     │        ├─ problem01.java 
│     │     │        ├─ problem02.java 
│     │     │        └─ problem03.java
│     │     │
│     │     │
│     │     └─ ... 이하 동일
│     │   
│     │   
│     ├─ seokjun/                 // 다른 구성원도 동일 구조
│
│
```

## 브랜치 전략 가이드

### 원칙
- 참가자는 해당 주차 산출물(문서+코드)을 **개인 주차 브랜치**에서 작업한다.  
  예: `gimin0226-week-01`
- 모든 PR의 대상(base)은 **develop** 이다. `main`에는 직접 올리지 않는다.
- 주차 마감 시 스터디장(김기민)이 `develop`의 누적 변경을 `main`에 한 번에 반영한다.
  - main: 마감본 확정본, 주차가 끝나야만 갱신됨
  - develop: 이번 주 모두가 함께 쌓아가는 통합본, 개인 PR들이 계속 머지되는 곳
---

### 브랜치 네이밍 규칙
- `<github핸들명>-week-<NN>`
- 예: `gimin0226-week-01`, `seokjun01-week-03`

---

### 참가자 작업 흐름 (로컬 → PR)

### 1. 기준 브랜치 최신화
```bash
git switch develop
git pull origin develop --ff-only
```

- 원격 저장소(origin)의 `develop` 브랜치를 가져와 fast-forward가 가능할 때만 반영한다.
- fast-forward merge: 로컬 브랜치에 새 커밋이 없고, 원격이 더 앞서 있을 때 단순히 **포인터만 앞으로 이동**하는 병합 방식
- 즉, 내 로컬 브랜치가 원격보다 뒤처졌을 때만, 안전하게 최신화하는 것

### 2. 주차 브랜치 생성
```bash
git switch -c gimin0226-week-01
```

### 3. 산출물 작성 및 커밋 후 원격 푸시
```bash
git add .
git commit -m "docs: Week01 section01 수정"
git push -u origin gimin-week-01
```
- 커밋 메시지 prefix 가이드
  - `docs`: → 개인 문서/섹션
  - `code`: → 과제 코드
  - `cs-questions`: → 공통 문제
- 커밋 메시지 예시
  - `docs: Week01 section01 수정`
  - `code: Week01 problem01 추가`
  - `cs-questions: Week01.md 수정`

### 4. Github에서 PR 생성
- base: `develop`
- compare: `gimin-week-01`
- PR 제목 예시
  - `[김기민] Week01/section01.md 제출`
  - `[김기민] Week02/code/problem01.java 제출`
  - `[김기민] cs-questions/Week01.md 수정`

### 5. 머지 후 로컬 정리
- 스터디장이 PR을 승인하고 Merge하면, Github가 자동으로 해당 커밋을 `develop`에 합친다.
```bash
git switch develop
git pull origin develop --ff-only
git branch -d gimin-week-01
```
- PR이 병합됐으니, 로컬도 상태를 최신화하고, 필요 없어진 작업 브랜치 정리
- branch 삭제 작업은 모든 파일을 develop 브랜치에 올린 후 수행할 것
 
---
## 스터디장 할 것 (다른 스터디원들은 x)

### PR 리뷰·머지 기준
- base가 `develop`인지 확인
- 제목,본문 컨벤션 준수 여부 확인

### 주차 마감 직전 점검
- `develop`에 해당 주차 PR들이 모두 머지됐는지 확인
- 필요시 누락자 멘션

### 주차 마감 반영( develop -> main )
- Github에서 Create pull request 선택
- base: `main`, compare: `develop`
- PR 제목: `1주차 마감 PR`
- 본문: 주요 변경 요약 + 참여자 목록 정리

