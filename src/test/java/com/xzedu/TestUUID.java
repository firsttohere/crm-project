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
	@Test
	public void t2(){
		//这是在github上写的代码，看看会不会·同步到pull的本地
		System.out.println("hello github!");
	}

}
