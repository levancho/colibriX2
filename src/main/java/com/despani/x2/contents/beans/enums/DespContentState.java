  package com.despani.x2.contents.beans.enums;



import com.despani.x2.core.interfaces.IDespCodeEnum;

import java.util.*;

  /**
   * Created by IntelliJ IDEA.
   * Yougar Group
   * User: Owner
   * Date: 9/1/11
   * Time: 12:21 PM
   * To change this template use File | Settings | File Templates.
   */

  public enum DespContentState implements IDespCodeEnum {
      LIGHT(1, "Light state without action bar"),
      FULL(0,"full state ");

      private static final Map<Integer, DespContentState> lookup = new HashMap<Integer, DespContentState>();
      static {
          for ( DespContentState s : EnumSet.allOf(DespContentState.class) ) {
              lookup.put(s.getCode(), s);
          }
      }
      public static DespContentState get(int code) {
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

      private DespContentState(int code, String value) {
          this.code = code;
          this.value = value;
      }

      @Override
      public String toString() {
          return this.value;
      }
      public static Collection<String> getItems() {
          DespContentState[] values = DespContentState.values();
          ArrayList<String> items = new ArrayList<>(values.length);
          for ( DespContentState type : values ) {
              items.add(new String(type.value));
          }
          return items;
      }
  }
