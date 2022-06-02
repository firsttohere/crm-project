package com.xzedu;

import java.util.UUID;

import org.junit.Test;

public class TestUUID {

	@Test
	public void t1() {
		
		UUID randomUUID = UUID.randomUUID();
		String string = randomUUID.toString().replaceAll("-", "");
		System.out.println(string.length());
		
	}
}
