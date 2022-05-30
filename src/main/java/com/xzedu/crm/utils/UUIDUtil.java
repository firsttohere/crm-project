package com.xzedu.crm.utils;

import java.util.UUID;

public class UUIDUtil {
	
	public static String generate32UUID() {
		UUID randomUUID = UUID.randomUUID();
		return randomUUID.toString().replaceAll("-", "");
	}
}
