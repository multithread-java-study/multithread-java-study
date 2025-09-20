# 섹션 3 발표 자료 정리

## 1. Thread.sleep() 예외 처리 이유

- 메소드 내부에 `throws InterruptedException`이 붙어 있음
- `sleep()` 호출 시 반드시 예외를 처리하거나 던져야 함
    - 체크 예외: InterruptedException, SQL, IO 등
- 컴파일러가 처리하지 않으면 컴파일 에러 발생
- 따라서 `try-catch` 또는 `throws`로 처리 필요

---

## 2. catch에서 `e.printStackTrace()`와 `throw new RuntimeException(e)` 차이

1. **`e.printStackTrace()`**
    - 콘솔에 에러 내용만 출력
    - 예외는 잡혔기 때문에 프로그램은 계속 실행
    - 의미: "알려주긴 했으니, 프로그램은 그냥 진행할게"

2. **`throw new RuntimeException(e)`**
    - 체크 예외(InterruptedException)를 언체크 예외(RuntimeException)로 감싸서 다시 던짐
    - 현재 스레드는 즉시 종료
    - 의미: "이 예외는 무시할 수 없는 심각한 상황"

---

## 3. 자바 메모리 구조

- JVM이 OS로부터 메모리를 할당받아 **Stack, Heap, Static 영역**으로 나눠서 사용
- 메모리 구조 이해를 위해 JVM부터 정리

### JVM이란?
- Java Virtual Machine
- 자바 바이트코드를 해석하고 실행
- OS에 상관없이 설치만 하면 실행 가능

### JVM 실행 순서
1. `.java` 파일 → 컴파일러 통해 `.class` 파일로 변환
2. `.class` 파일을 ClassLoader가 JVM 런타임 영역으로 로딩
3. ClassLoader가 메모리에 올리고 실행 시작

- 이렇게 실행된 JVM 런타임 메모리가 바로 **Stack, Heap, Static 영역**

---

## 4. 변수 종류

| 종류 | 설명 | 저장 위치 |
|------|------|-----------|
| 클래스 변수 (static) | static 붙고 어디서든 사용 가능 | Static 영역 |
| 인스턴스 변수 | 클래스 내, static 없음, 객체별 존재 | Heap |
| 지역 변수 | 메서드 내부, 초기값 지정 필요 | Stack |

---

## 5. 메모리 영역별 설명

### Static(Method, Class) 영역
- JVM 실행 시 클래스 로딩과 동시에 생성
- 클래스 정보, static 변수, 생성자, 메소드 정보 등 저장
- 프로그램 종료 시 메모리 해제
- 어디서든 접근 가능

### Heap 영역
- 객체와 인스턴스 변수, 배열 저장
- 모든 스레드 공유 가능
- GC 대상 영역
- 런타임에 크기 결정되는 동적 메모리 할당
- 상수 풀: 동일한 문자열은 한 번만 생성, 여러 변수에서 참조

### Stack 영역
- 메서드 호출 시 생성된 지역 변수, 매개변수, 임시 데이터, Heap 참조값 저장
- 스레드마다 독립적

---

