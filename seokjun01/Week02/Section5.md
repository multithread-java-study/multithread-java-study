# 📚 2주차 - 섹션 5

## 인터럽트 (Interrupt)

스레드 실행을 **외부에서 중단하거나 깨우기 위한 신호**를 보낼 수 있다.  
특히, 대기 상태(`WAITING`, `TIMED_WAITING`)의 스레드를 **즉시 `Runnable` 상태로 바꿀 수 있음**.

### 기본 동작
- `main` 스레드에서 다른 스레드에 `interrupt()` 호출 → 해당 스레드가 **인터럽트 상태**로 변경됨.
- 실행 중에 `sleep()`, `join()`, `wait()` 같은 **대기 메서드**를 만나면 `InterruptedException` 예외가 발생 → `catch` 블록으로 제어가 넘어감.

---

## 상태 변화 흐름

1. 스레드 실행 중 `interrupt()` 호출
2. 스레드 내부에서 인터럽트 상태 플래그가 `true` 로 설정됨
3.
  - 대기 메서드(`sleep`, `join`, `wait`) 실행 중이었다면 → 즉시 `InterruptedException` 발생
  - 실행 중이었다면 → 별도로 `isInterrupted()` 또는 `Thread.interrupted()` 로 상태 확인 필요

👉 **인터럽트 자체는 강제 중단이 아니라 “중단 요청” 신호**일 뿐이며, 스레드가 이를 **직접 처리**해야 한다.

---

## 인터럽트 상태 확인 방법

### 1. `isInterrupted()`
- 현재 스레드의 인터럽트 상태를 확인 (`true/false` 반환).
- 상태를 **유지**한다 → 계속 `true`로 남음.

### 2. `Thread.interrupted()`
- 현재 스레드가 인터럽트 상태인지 확인.
- 만약 `true`라면, 상태를 **확인 후 즉시 `false`로 초기화**.
- 한 번 체크 후 정상 상태로 돌려놓고 싶을 때 사용.

📌 **정리**
- `isInterrupted()` → 상태 확인만 (상태 유지).
- `Thread.interrupted()` → 상태 확인 + 초기화.

---

## while문에서 인터럽트 체크

```java
while (!Thread.currentThread().isInterrupted()) {
    log("작업 중");
    try {
        Thread.sleep(3000); // 대기 중 인터럽트 발생 시 예외 발생
    } catch (InterruptedException e) {
        log("인터럽트 발생, 종료 처리");
        break;
    }
}
