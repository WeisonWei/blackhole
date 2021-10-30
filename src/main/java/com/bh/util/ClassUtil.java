package com.bh.util;

import io.micrometer.core.instrument.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClassUtil {

  public static String getClassVar(String className) {
    return className.substring(0, 1).toLowerCase() + className.substring(1);
  }

  public static List<Field> getAllFieldList(Class clazz) {
    List<Field> fieldList = new ArrayList();
    for (Class tempClass = clazz; tempClass != null; tempClass = tempClass.getSuperclass()) {
      fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
    }
    Iterator var3 = fieldList.iterator();
    while (var3.hasNext()) {
      Field field = (Field) var3.next();
      field.setAccessible(true);
    }
    return fieldList;
  }

  public static Map<String, Object> beanToMap(Object bean) {
    try {
      Map<String, Object> map = new LinkedHashMap();
      List<Field> fieldList = getAllFieldList(bean.getClass());
      Iterator var3 = fieldList.iterator();
      while (var3.hasNext()) {
        Field field = (Field) var3.next();
        String fieldName = field.getName();
        Object fieldValue = field.get(bean);
        map.put(fieldName, fieldValue);
      }
      return map;
    } catch (IllegalAccessException var7) {
      throw new RuntimeException(var7);
    }
  }

  public static Map<String, String> beanToMapString(Object bean) {
    List<Field> fieldList = getAllFieldList(bean.getClass());
    return fieldList.stream()
        .filter(field -> !field.getName().equals("log"))
        .filter(field -> StringUtils.isNotBlank(getValueString(bean, field)))
        .collect(Collectors.toMap(Field::getName,
            field -> getValueString(bean, field),
            (field1, field2) -> field2));
  }

  private static String getValueString(Object bean, Field field) {
    try {
      return Optional.ofNullable(field.get(bean)).map(String.class::cast).orElse("");
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static <T> T newInstance(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (IllegalAccessException | InstantiationException var2) {
      throw new RuntimeException(var2);
    }
  }

  public static <T> T newException(int code, String desc, Class<T> type) {
    T t = null;
    try {
      t = type.getDeclaredConstructor(int.class, String.class).newInstance(code, desc);
    } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return t;
  }
}
