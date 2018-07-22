package com.example.vendingmachine;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VendingMachine {

	/** この自販機の貨幣在庫 */
	private final HashMap<Money, Integer> moneyStock = new HashMap<Money, Integer>();

	/** この自販機の商品リスト */
	private final LinkedHashMap<Integer, Product> productList = new LinkedHashMap<Integer, Product>();

	/** この自販機の商品の在庫 */
	private final HashMap<Integer, Integer> productRemain = new HashMap<Integer, Integer>();

	/** 支払われたお金 */
	private final HashMap<Money, Integer> paidMoneyAmmount = new HashMap<Money, Integer>();

	/** お釣りの計算ロジック */
	private ChangeCalculator changeCalculator;

	/**
	 * コンストラクタ
	 */
	public VendingMachine() {
		// 自販機の中にあるお金を初期化
		for (Money m : Money.values()) {
			getMoneyStock().put(m, 0);
		}		
		
		this.changeCalculator = new DefaultChangeCalculator(this);
	}

	/**
	 * お釣りの計算ロジックを取得します。
	 * 
	 * @return お釣りの計算ロジックです
	 */
	public ChangeCalculator getChangeCalculator() {
		return changeCalculator;
	}

	/**
	 * お釣りの計算ロジックを設定します。
	 * 
	 * @param changeCalculator
	 *            お釣りの計算ロジックです
	 */
	public void setChangeCalculator(ChangeCalculator changeCalculator) {
		this.changeCalculator = changeCalculator;
	}

	/**
	 * @return the moneyStock
	 */
	public HashMap<Money, Integer> getMoneyStock() {
		return moneyStock;
	}

	/**
	 * @return the productList
	 */
	public LinkedHashMap<Integer, Product> getProductList() {
		return productList;
	}

	/**
	 * @return the productRemain
	 */
	public HashMap<Integer, Integer> getProductRemain() {
		return productRemain;
	}

	/**
	 * @return the paidMoneyAmmount
	 */
	public HashMap<Money, Integer> getPaidMoneyAmmount() {
		return paidMoneyAmmount;
	}
	
	/**
	 * お金を補充します
	 * 
	 * @param type
	 *            お金の種類です
	 * @param ammount
	 *            量です
	 */
	public void addMoney(Money type, int ammount) {
		int currentAmmount = getMoneyStock().get(type);
		int newAmmount = Math.min(Integer.MAX_VALUE, currentAmmount + ammount);

		getMoneyStock().put(type, newAmmount);
	}

	/**
	 * お金の在庫量を取得します。
	 * 
	 * @param type
	 *            お金の種類です
	 * @return 指定されたお金の在庫量です
	 */
	public int getAmmountOfMoney(Money type) {
		return getMoneyStock().get(type);
	}

	/**
	 * 自販機に商品を追加します。
	 * 
	 * @param product
	 *            追加する商品です
	 * @param ammount
	 *            追加する量です
	 */
	public void addProduct(Product product, int ammount) {

		getProductList().put(product.getId(), product);

		int currentAmmount = getProductRemain().containsKey(product.getId()) ? getProductRemain().get(product.getId())
				: 0;
		int newAmmount = Math.min(Integer.MAX_VALUE, currentAmmount + ammount);

		getProductRemain().put(product.getId(), newAmmount);
	}

	/**
	 * 指定された商品の在庫量を取得します。
	 * 
	 * @param product
	 *            商品です
	 * @return 商品の在庫個数です
	 */
	public int getAmmountOfProduct(Product product) {
		return getProductRemain().get(product.getId());
	}

	/**
	 * 指定された商品IDの商品の在庫量を取得します。
	 * 
	 * @param id
	 *            商品IDです
	 * @return 商品の在庫個数です
	 */
	public int getAmmountOfProduct(int id) {
		return getProductRemain().get(id);
	}

	/**
	 * 貨幣を支払います。
	 * 
	 * @param money
	 *            投入する貨幣の種類です
	 * @param ammount
	 *            投入する貨幣の個数です
	 */
	public void payMoney(Money money, int ammount) {
		
		if (ammount < 0) {
			throw new IllegalArgumentException("ammount should be positive: ammount="+ammount);
		}
		
		Integer currentAmmount = getPaidMoneyAmmount().get(money);
		int newAmmount = ammount;

		if (currentAmmount != null) {
			newAmmount += currentAmmount.intValue();
		}

		getPaidMoneyAmmount().put(money, newAmmount);
	}

	/**
	 * 購入可能な商品のリストを取得します。 購入可能とは現在の投入金額で購入可能で、かつ、在庫が有るのことを言います。
	 */
	public List<Product> getBuyableProductList() {
		int currentPaid = getTotalPaidAmmount();
		List<Product> result = getProductList().values().stream()
				.filter(x -> (x.getPrice() <= currentPaid && getProductRemain().get(x.getId()) > 0))
				.collect(Collectors.toList());

		return result;
	}

	/**
	 * 投入済みの金額を取得します。 商品の購入により消費された分やお釣りとして払い出された分は含まれません。
	 * 
	 * @return 投入済みの金額です
	 */
	public int getTotalPaidAmmount() {
		int totalAmmount = getPaidMoneyAmmount().entrySet().stream().mapToInt(x -> x.getKey().getValue() * x.getValue())
				.sum();

		return totalAmmount;
	}

	/**
	 * 商品を購入します。
	 * 
	 * @param id 商品IDです
	 * @return お釣りです
	 */
	public Map<Money, Integer> buyProduct(int id) {
		if (!getProductList().containsKey(id)) {
			throw new IllegalArgumentException("invalid product id: " + id);
		}

		Product product = getProductList().get(id);

		if (product.getPrice() > getTotalPaidAmmount()) {
			throw new IllegalArgumentException("paid money not enough. product id: " + id);
		}

		if (getProductRemain().get(id) <= 0) {
			throw new IllegalArgumentException("product is sold out. product id: " + id);
		}

		// お釣りを計算して釣り銭が足りているか確認
		Map<Money, Integer> change = getChangeCalculator().calculateChange(product.getPrice());

		// 在庫を減らす
		getProductRemain().put(id, getProductRemain().get(id) - 1);

		// お釣りに使われなかった支払金を貨幣ストックに移動
		Map<Money, Integer> paidAndStock = getTotalPaidAndStockMoneyAmmount();
		getMoneyStock().putAll(
				paidAndStock.entrySet().stream().collect(Collectors.toMap(
						x -> x.getKey(),
						x -> x.getValue() - (change.containsKey(x.getKey()) ? change.get(x.getKey()) : 0))));

		// 支払金をリセット
		getPaidMoneyAmmount().clear();
		
		return change;
	}

	/**
	 * 指定された商品IDの商品が購入可能かを取得します。
	 * 現在の投入金額で購入可能、かつ、在庫が有るかを調べます。
	 * 
	 * @param productId 商品IDです。
	 * @return 指定された商品IDの商品が購入可能な場合に true
	 */
	public boolean isProductBuyable(int productId) {
		return getBuyableProductList().stream().filter(x -> x.getId() == productId).findFirst().isPresent();
	}

	/**
	 * 支払い貨幣量ともとから自販機内にあった貨幣量の合計量を取得します。
	 * 
	 * @return 支払い貨幣量ともとから自販機内にあった貨幣量の合計量です
	 */
	public Map<Money, Integer> getTotalPaidAndStockMoneyAmmount() {		
		Map<Money, Integer> paidAmmount = getPaidMoneyAmmount();
		Map<Money, Integer> stockAmmount = getMoneyStock();
		Map<Money, Integer> total = stockAmmount.entrySet().stream()
				.collect(Collectors.toMap(
						x -> x.getKey(),
						x -> x.getValue() + (paidAmmount.containsKey(x.getKey()) ? paidAmmount.get(x.getKey()) : 0)));
		
		return total;
	}

	/**
	 * 購入をキャンセルしします。
	 * 支払った貨幣はお釣りとして返却します。
	 * 
	 * @return 支払った貨幣です
	 */
	public Map<Money, Integer> cancel() {
		Map<Money, Integer> change = new HashMap<Money, Integer>();
		change.putAll(getPaidMoneyAmmount());
		
		// 支払い金額リセット
		getPaidMoneyAmmount().clear();
		
		return change;
	}
	
	/**
	 * 【デバッグ用】自販機の主要なステータスをダンプします。
	 * 
	 * @return 自販機の主要なステータスです
	 */
	public String dumpStatus() {
		StringBuilder sb = new StringBuilder();

		sb.append("money stock(");
		sb.append(Stream.of(Money.values())
				.map(x -> x.name() + "=" + getMoneyStock().get(x))
				.collect(Collectors.joining(",")));
		sb.append(")");
		sb.append(", product stock(");
		sb.append(getProductList().values().stream()
				.map(x -> x.getName() + "=" + this.getAmmountOfProduct(x))
				.collect(Collectors.joining(",")));
		sb.append(")");
		sb.append(", paid money(");
		sb.append(getPaidMoneyAmmount().entrySet().stream()
				.map(x -> x.getKey().name() + "=" + x.getValue())
				.collect(Collectors.joining(",")));
		sb.append(")");

		return sb.toString();
	}
}
