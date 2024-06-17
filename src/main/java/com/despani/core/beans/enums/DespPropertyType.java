  package com.despani.core.beans.enums;



import com.despani.core.interfaces.IDespCodeEnum;

import java.util.*;

  /**
   * Created by IntelliJ IDEA.
   * Yougar Group
   * User: Owner
   * Date: 9/1/11
   * Time: 12:21 PM
   * To change this template use File | Settings | File Templates.
   */

  public enum DespPropertyType implements IDespCodeEnum {
      TEXT(1, "Text Property"),
      BOOL(2, "boolean Property"),
      SELECT(3,"Select Property");

      private static final Map<Integer, DespPropertyType> lookup = new HashMap<Integer, DespPropertyType>();
      static {
          for ( DespPropertyType s : EnumSet.allOf(DespPropertyType.class) ) {
              lookup.put(s.getCode(), s);
          }
      }
      public static DespPropertyType get(int code) {
          return lookup.get(code);
      }

      private int code;
      public int getCode() {
          return code;
      }

      private String value;
      public String getValue() {
          return value;
      }

      private DespPropertyType(int code, String value) {
          this.code = code;
          this.value = value;
      }

      @Override
      public String toString() {
          return this.value;
      }
      public static Collection<String> getItems() {
          DespPropertyType[] values = DespPropertyType.values();
          ArrayList<String> items = new ArrayList<>(values.length);
          for ( DespPropertyType type : values ) {
              items.add(new String(type.value));
          }
          return items;
      }
  }
