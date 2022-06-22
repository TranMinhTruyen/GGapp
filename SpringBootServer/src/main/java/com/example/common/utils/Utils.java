package com.example.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
@Component
public class Utils {
	private static List<String> getMethodNameOfClass(Class<?> clazz){
		List<String> methodName = new ArrayList<>();
		Field [] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++){
			methodName.add(fields[i].getName());
		}
		return methodName;
	}

	public boolean checkRole (Authentication authentication, String role) {
		if (authentication.getAuthorities().stream().anyMatch(a ->a.getAuthority().equals(role))) {
			return true;
		} else return false;
	}
}
