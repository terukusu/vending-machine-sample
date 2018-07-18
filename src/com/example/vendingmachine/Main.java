package com.example.vendingmachine;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws Exception {
		int[] coinValues = {500, 100, 50, 10};
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

        int change500 = 0;
        int change100 = 0;
        int change50 = 0;
        int change10 = 0;

        int changeTotal = totalInputMoney - 120;
        System.out.println("change total: " + changeTotal);

        int changeRemain = changeTotal;

        // 500円の枚数
        change500 = changeRemain / 500;
        changeRemain -= change500 * 500;

        // 100円の枚数
        change100 = changeRemain / 100;
        changeRemain -= change100 * 100;

        // 50円の枚数
        change50 = changeRemain / 50;
        changeRemain -= change50 * 50;

        // 10円の枚数
        change10 = changeRemain / 10;
        changeRemain -= change10 * 10;

        // お釣り表示
        System.out.println("change 500: " + change500);
        System.out.println("change 100: " + change100);
        System.out.println("change 50: " + change50);
        System.out.println("change 10: " + change10);

        System.out.println("change remain: " + changeRemain);
	}

}
