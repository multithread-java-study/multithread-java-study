## 섹션 4. 스레드 제어와 생명 주기1

### 스레드 생명주기

1.  New -> Runnable -> Terminated
2. Runnable <-> Blocked, Waiting, Timed Waiting

---

### Runnable 인터페이스의 run() 구현 시, 예외 던질 수 없는 이유

1.  자바에서, 부모 메서드가 체크 예외를 던지지 않으면, 자식도 던질 수 없음.
	Runnable 인터페이스의 run()은 체크 예외를 던지지 않음.

---

### join

1.  **join()**: 호출 스레드는 대상 스레드가 완료될때까지 무한 대기
2. **join(ms)**: 호출 스레드는 특정 시간만 대기