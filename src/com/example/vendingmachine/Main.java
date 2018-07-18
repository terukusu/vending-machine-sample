package com.example.vendingmachine;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws Exception {
		int[] coinValues = { 500, 100, 50, 10 };
		int[] coinAmmounts = new int[4];

		int totalInputMoney = 0;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// 各貨幣の枚数を入力
		for (int i = 0; i < coinValues.length; i++) {
			// 対象の貨幣
			int currentValue = coinValues[i];

			System.out.print(currentValue + "?: ");
			int ammount = Integer.parseInt(br.readLine());

			// 枚数チェック
			if (ammount < 0) {
				System.out.println("minus");
				return;
			}

			// 枚数を記録
			coinAmmounts[i] = ammount;

			// トータル入力金額に加算
			totalInputMoney += currentValue * ammount;
		}

		System.out.println("total input money: " + totalInputMoney);

		// 入力ストリーム開放
		br.close();

		// お釣り計算
		int changeTotal = totalInputMoney - 120;
		int changeRemain = changeTotal;

		System.out.println("change total: " + changeTotal);

		int[] changeAmmounts = new int[4];

		// 各貨幣の枚数を入力
		for (int i = 0; i < coinValues.length; i++) {
			// 対象の貨幣
			int currentValue = coinValues[i];

			// 貨幣の枚数計算
			int ammount = changeRemain / currentValue;
			changeRemain -= ammount * currentValue;

			// 枚数を記録
			changeAmmounts[i] = ammount;

			System.out.println("change " + currentValue + ": " + ammount);
		}

		System.out.println("change remain: " + changeRemain);
	}
}