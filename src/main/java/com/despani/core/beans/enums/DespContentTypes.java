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

  public enum DespContentTypes implements IDespCodeEnum {

      CONTENT(1, "content"),
      CATEGORY(2, "category"),
      MODULE(3, "module"),
      USER(4, "user"),
      MENU(0,"menu"),
      ROLE(5,"role"),
      PRODUCT(6,"product"),
      COMPANY(7,"company");

      private static final Map<Integer, DespContentTypes> lookup = new HashMap<Integer, DespContentTypes>();
      static {
          for ( DespContentTypes s : EnumSet.allOf(DespContentTypes.class) ) {
              lookup.put(s.getCode(), s);
          }
      }
      public static DespContentTypes get(int code) {
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

      private DespContentTypes(int code, String value) {
          this.code = code;
          this.value = value;
      }

      @Override
      public String toString() {
          return this.value;
      }
      public static Collection<String> getItems() {
          DespContentTypes[] values = DespContentTypes.values();
          ArrayList<String> items = new ArrayList<>(values.length);
          for ( DespContentTypes type : values ) {
              items.add(new String(type.value));
          }
          return items;
      }
  }
