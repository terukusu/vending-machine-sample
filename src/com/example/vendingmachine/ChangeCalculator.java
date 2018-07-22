package com.example.vendingmachine;

import java.util.Map;

/**
 * お釣りを
 */
public interface ChangeCalculator {

	/**
	 * お釣りを計算します。
	 * 
	 * @param price 商品の価格です
	 * @return お釣りです。貨幣の種類とその量(個数)のマップです
	 */
	public Map<Money, Integer> calculateChange(int price);
}
