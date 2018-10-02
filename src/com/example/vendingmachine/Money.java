package com.example.vendingmachine;

/**
 * 貨幣を表す列挙です
 *
 */
public enum Money {
	// お金の種類を増やしたければここを増やせばいいだけ
//	YEN10000(10000),
//	YEN5000(5000),
	YEN500(500),
	YEN100(100),
	YEN50(50),
	YEN10(10),
	;
	
	private final int value;

    private Money(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
