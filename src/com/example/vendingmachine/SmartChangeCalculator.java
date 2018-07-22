package com.example.vendingmachine;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 少し賢いお釣り計算ロジックです。
 * 支払われた貨幣から優先的にお釣りとして扱うので、自販機を両替機代わりに使われたくない場合に便利です。
 */
public class SmartChangeCalculator implements ChangeCalculator {

	/** 自動販売機 */
	private final VendingMachine vendingMachine;

	/**
	 * 自動販売機を指定してインスタンスを生成します
	 * 
	 * @param vendingMachine 自動販売機です
	 */
	public SmartChangeCalculator(VendingMachine vendingMachine) {
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
		
		// まずは支払われた貨幣からお釣りを計算
		Map<Money, Integer> paidMoneyAmmount = getVendingMachine().getPaidMoneyAmmount();
		
		// 高額貨幣順に並び替え
		List<Money> moneyList = paidMoneyAmmount.keySet().stream().collect(Collectors.toList());
		Collections.sort(moneyList, (m1, m2) -> -1 * Integer.valueOf(m1.getValue()).compareTo(m2.getValue()));

		int totalPaid = getVendingMachine().getTotalPaidAmmount();
		int changeRemain = totalPaid - price;
		
		if (changeRemain < 0) {
			throw new IllegalArgumentException("paid money not enough: paid=" + totalPaid + ", need=" + price);
		}

		Map<Money, Integer> change = new LinkedHashMap<Money, Integer>();
		
		for (Money money: moneyList) {
			int ammount = Math.min(paidMoneyAmmount.get(money), changeRemain / money.getValue());
			changeRemain -= ammount * money.getValue();
			
			change.put(money, ammount);
		}
		
		if (changeRemain == 0) {
			return change;
		}

		// 次に足りない分をもともと有った貨幣からお釣りを計算
		Map<Money, Integer> stockAmmount = getVendingMachine().getMoneyStock();
		
		// 高額貨幣順に並び替え
		moneyList = stockAmmount.keySet().stream().collect(Collectors.toList());
		Collections.sort(moneyList, (m1, m2) -> -1 * Integer.valueOf(m1.getValue()).compareTo(m2.getValue()));
		
		for (Money money: moneyList) {
			int ammount = Math.min(stockAmmount.get(money), changeRemain / money.getValue());
			changeRemain -= ammount * money.getValue();
			
			change.put(money, (change.containsKey(money) ? change.get(money) : 0) + ammount);
		}
		
		if (changeRemain > 0) {
			// 釣り銭不足
			throw new IllegalStateException("no enough money for change.");
		}
		
		return change;
	}

}
