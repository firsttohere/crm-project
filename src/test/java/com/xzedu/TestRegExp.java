package com.xzedu;

import org.junit.Test;

public class TestRegExp {
	@Test
	public void t() {
		String s = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";
		String date = "2022-01-11";
		System.out.println("匹配成功了吗" + date.matches(s));
		
	}
}
