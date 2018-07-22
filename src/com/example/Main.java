package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.util.Log;
import com.example.vendingmachine.ChangeCalculator;
import com.example.vendingmachine.DefaultChangeCalculator;
import com.example.vendingmachine.Money;
import com.example.vendingmachine.Product;
import com.example.vendingmachine.SmartChangeCalculator;
import com.example.vendingmachine.VendingMachine;

public class Main {

	/**
	 * プログラムのエントリポイントです。
	 * 
	 * @param args コマンドライン引数
	 * @throws Exception 何らかの問題が起きた時
	 */
	public static void main(String[] args) throws Exception {
//		// お釣りの数最小版
//		VendingMachine vm = createStupidVendingMachine();

		// 小銭両替防止版
		VendingMachine vm = createSmartVendingMachine();
		
		boolean isContinue = true;
		do {
			isContinue = useVendingMachine(vm);
		} while(isContinue);
	}
	
	/**
	 * 自販機を利用します。
	 * お金を入れて商品を選んで、買う。
	 * 
	 * @param vm 利用する自販機です
	 * @return ユーザーがもう一度購入したいときは true
	 * @throws Exception
	 */
	public static boolean useVendingMachine(VendingMachine vm) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		println("商品リスト: " + vm.getProductList().values().stream()
				.map(x -> x.getId() + ":" + x.getName() + "(" + x.getPrice() + "円)").collect(Collectors.joining(", ")));

		// お金を入れる
	
		println("お金を投入してください");
		for (Money money : Money.values()) {
			String form = "玉";
			if (money.getValue() > 500) {
				form = "札";
			}

			int ammount = 0;
			while (true) {
				println(money.getValue()+"円"+form+"の投入数は？:...");
				String ammountStr = br.readLine();

				try {
					ammount = Integer.parseInt(ammountStr);
				} catch (NumberFormatException e) {
					println("不正な値が入力されました: " + ammountStr);
					continue;
				}
				
				if (ammount < 0) {
					println("不正な値が入力されました: " + ammount);
					continue;
				}
				
				break;
			}

			vm.payMoney(money, ammount);
			
			println("現在の合計投入金額: " + vm.getTotalPaidAmmount() + "円");

		}

		Log.debug("自販機の状態: " + vm.dumpStatus());

		// 商品を選ぶ
		
		println("\n購入可能商品はこちらです。");
		println(vm.getBuyableProductList().stream()
				.map(x -> x.getId() + ":" + x.getName() + "(" + x.getPrice() + "円)").collect(Collectors.joining(", ")));

		int productId = -1;
		while (true) {
			println("購入する商品の番号を入力してください:...");
			String productIdStr = br.readLine();

			try {
				productId = Integer.parseInt(productIdStr);
			} catch (NumberFormatException e) {
				// 数値としておかしい
				println("不正な値が入力されました: " + productIdStr);
				continue;
			}

			if (!vm.isProductBuyable(productId)) {
				// 買えないやつを選んだ
				println("不正な値が入力されました: " + productId);
				continue;
			}
			
			break;
		}
		
		// 買う
		Map<Money, Integer> change = vm.buyProduct(productId);

		println("お釣り: " + change);
		
		Log.debug("自販機の状態: " + vm.dumpStatus());

		println("終了しますか？(y/n):...");
		String answerStr = br.readLine();
		
		if(answerStr.toUpperCase().equals("Y")) {
			return false;
		}
		
		return true;
	}

	/**
	 * コンソールにメッセージを表示します。終端に自動改行は入りません。
	 * 
	 * @param str 文字列
	 */
	public static void print(String str) {
		System.out.print(str);
	}

	/**
	 * コンソールにメッセージを表示します。終端に自動改行が入ります。
	 * 
	 * @param str 文字列
	 */
	public static void println(String str) {
		System.out.println(str);
	}

	/**
	 * アホな自販機を作成します。
	 * お釣りは貨幣枚数が最小になるように出てきます。(ある意味かしこい)
	 * お金と商品は適当に補充します。
	 * 
	 * @return 自販機です
	 */
	private static VendingMachine createStupidVendingMachine() {
		VendingMachine vm = new VendingMachine();
		ChangeCalculator cc = new DefaultChangeCalculator(vm);
		vm.setChangeCalculator(cc);

		setupMoneyAndProductForVendingMachine(vm);

		return vm;
	}

	/**
	 * 賢い自販機を作成します。
	 * 大量の少額貨幣の両替に使われてしまわないように、投入された貨幣を優先してお釣りに使う自販機です。
	 * お金と商品は適当に補充します。
	 * 
	 * @return 自販機です
	 */
	private static VendingMachine createSmartVendingMachine() {
		VendingMachine vm = new VendingMachine();
		ChangeCalculator cc = new SmartChangeCalculator(vm);
		vm.setChangeCalculator(cc);

		setupMoneyAndProductForVendingMachine(vm);
		
		return vm;
	}
	
	/**
	 * 自販機にお金と商品を補充して使える状態に準備します。
	 * 
	 * @param vm 自販機です。
	 */
	private static void setupMoneyAndProductForVendingMachine(VendingMachine vm) {
		// お金補充。とりあえず100枚ずつくらい
		for(Money money: Money.values()) {
			vm.addMoney(money, 100);
		}

		// 商品補充。とりあえず100個ずつくらい
		vm.addProduct(new Product(1, "商品A", 120), 100);
		vm.addProduct(new Product(2, "商品B", 1890), 100);
		vm.addProduct(new Product(3, "商品C", 780), 100);		
		vm.addProduct(new Product(4, "商品D", 38590), 100);		
	}
}