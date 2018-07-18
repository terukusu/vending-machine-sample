package com.example.vendingmachine;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws Exception {
        int num500 = 0;
        int num100 = 0;
        int num50 = 0;
        int num10 = 0;
        int totalInputMoney = 0;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // 500円の枚数
        System.out.print("500?: ");
        num500 = Integer.parseInt(br.readLine());

        // 500円の枚数チェック
        if (num500 < 0) {
            System.out.println("minus");
            return;
        }

        // 100円の枚数
        System.out.print("100?: ");
        num100 = Integer.parseInt(br.readLine());

        // 100円の枚数チェック
        if (num100 < 0) {
            System.out.println("minus");
            return;
        }

        // 50円の枚数
        System.out.print("50?: ");
        num50 = Integer.parseInt(br.readLine());

        // 50円の枚数チェック
        if (num50 < 0) {
            System.out.println("minus");
            return;
        }

        // 10円の枚数
        System.out.print("10?: ");
        num10 = Integer.parseInt(br.readLine());

        // 10円の枚数チェック
        if (num10 < 0) {
            System.out.println("minus");
            return;
        }

        // 入力ストリーム開放
        br.close();

        // 合計金額計算
        totalInputMoney = 500 * num500 + 100 * num100 + 50 * num50 + 10 * num10;
        System.out.println("total input money: " + totalInputMoney);

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