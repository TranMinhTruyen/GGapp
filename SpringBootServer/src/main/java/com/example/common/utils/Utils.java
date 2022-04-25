package com.example.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	private static List<String> getMethodNameOfClass(Class<?> clazz){
		List<String> methodName = new ArrayList<>();
		Field [] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++){
			methodName.add(fields[i].getName());
		}
		return methodName;
	}
}
