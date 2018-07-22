package com.example.vendingmachine;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * デフォルトのお釣り計算ロジックです。
 * もともと合った貨幣も今回支払われた貨幣も総合してお釣りの貨幣数が最小になるようにお釣りを計算します。
 */
public class DefaultChangeCalculator implements ChangeCalculator {

	/** 自動販売機 */
	private final VendingMachine vendingMachine;

	/**
	 * 自動販売機を指定してインスタンスを生成します
	 * 
	 * @param vendingMachine 自動販売機です
	 */
	public DefaultChangeCalculator(VendingMachine vendingMachine) {
		this.vendingMachine = vendingMachine;
	}
	
	/**
	 * @return the vendingMachine
	 */
	public VendingMachine getVendingMachine() {
		return vendingMachine;
	}

	@Override
	public Map<Money, Integer> calculateChange(int price) {
		
		Map<Money, Integer> totalStock = getVendingMachine().getTotalPaidAndStockMoneyAmmount();
		
		// 高額貨幣順に並び替え
		List<Money> moneyList = totalStock.keySet().stream().collect(Collectors.toList());
		Collections.sort(moneyList, (m1, m2) -> -1 * Integer.valueOf(m1.getValue()).compareTo(m2.getValue()));

		int totalPaid = getVendingMachine().getTotalPaidAmmount();
		int changeRemain = totalPaid - price;
		
		if (changeRemain < 0) {
			throw new IllegalArgumentException("paid money not enough: paid=" + totalPaid + ", need=" + price);
		}

		Map<Money, Integer> change = new LinkedHashMap<Money, Integer>();
		
		for (Money money: moneyList) {
			int ammount = Math.min(totalStock.get(money), changeRemain / money.getValue());
			changeRemain -= ammount * money.getValue();
			
			change.put(money, ammount);
		}
		
		if (changeRemain > 0) {
			// 釣り銭不足
			throw new IllegalStateException("no enough money for change.");
		}
		
		return change;
	}

}
