package com.lannstark.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SuperClassReflectionUtils {

	private SuperClassReflectionUtils() {

	}

	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, true)) {
			fields.addAll(Arrays.asList(clazzInClasses.getDeclaredFields()));
		}
		return fields;
	}

	public static Annotation getAnnotation(Class<?> clazz,
										   Class<? extends Annotation> targetAnnotation) {
		for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, false)) {
			if (clazzInClasses.isAnnotationPresent(targetAnnotation)) {
				return clazzInClasses.getAnnotation(targetAnnotation);
			}
		}
		return null;
	}

	public static Field getField(Class<?> clazz, String name) throws Exception {
		for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, false)) {
			for (Field field : clazzInClasses.getDeclaredFields()) {
				if (field.getName().equals(name)) {
					return clazzInClasses.getDeclaredField(name);
				}
			}
		}
		throw new NoSuchFieldException();
	}

	private static List<Class<?>> getAllClassesIncludingSuperClasses(Class<?> clazz, boolean fromSuper) {
		List<Class<?>> classes = new ArrayList<>();
		while (clazz != null) {
			classes.add(clazz);
			clazz = clazz.getSuperclass();
		}
		if (fromSuper) {
			Collections.reverse(classes);
		}
		return classes;
	}

}
