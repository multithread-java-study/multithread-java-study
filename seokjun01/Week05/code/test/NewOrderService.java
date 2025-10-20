package thread.executor.test;

import static util.MyLogger.*;
import static util.ThreadUtils.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NewOrderService {

	private final ExecutorService es = Executors.newFixedThreadPool(3);

	public void order(String orderNo) {
		InventoryWork inventoryWork = new InventoryWork(orderNo);
		ShippingWork shippingWork = new ShippingWork(orderNo);
		AccountingWork accountingWork = new AccountingWork(orderNo);

		try {
			Future<Boolean> inventoryFuture = es.submit(inventoryWork);
			Future<Boolean> shippingFuture = es.submit(shippingWork);
			Future<Boolean> accountingFuture = es.submit(accountingWork);

			Boolean inventoryResult = inventoryFuture.get();
			Boolean shippingResult = shippingFuture.get();
			Boolean accountingResult = accountingFuture.get();

			if (inventoryResult && shippingResult && accountingResult) {
				log("모든 주문 처리가 성공적으로 완료되었습니다.");
			} else {
				log("일부 작업이 실패했습니다.");
			}
		} catch (Exception e) {
			log("작업 중 예외 발생: " + e.getMessage());
		} finally {
			es.shutdown(); // close() 대신 shutdown()
		}
	}

	// ✅ 내부 클래스 3개는 NewOrderService 클래스의 안쪽에 위치해야 함

	static class InventoryWork implements Callable<Boolean> {
		private final String orderNo;

		public InventoryWork(String orderNo) {
			this.orderNo = orderNo;
		}

		@Override
		public Boolean call() {
			log("재고 업데이트: " + orderNo);
			sleep(1000);
			return true;
		}
	}

	static class AccountingWork implements Callable<Boolean> {
		private final String orderNo;

		public AccountingWork(String orderNo) {
			this.orderNo = orderNo;
		}

		@Override
		public Boolean call() {
			log("회계 시스템 업데이트: " + orderNo);
			sleep(1000);
			return true;
		}
	}

	static class ShippingWork implements Callable<Boolean> {
		private final String orderNo;

		public ShippingWork(String orderNo) {
			this.orderNo = orderNo;
		}

		@Override
		public Boolean call() {
			log("배송 시스템 알림: " + orderNo);
			sleep(1000);
			return true;
		}
	}
}
