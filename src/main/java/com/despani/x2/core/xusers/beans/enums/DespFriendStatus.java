  package com.despani.x2.core.xusers.beans.enums;



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

  public enum DespFriendStatus implements IDespCodeEnum {

      APPROVED(1, "Approved"),
      DECLIEND(2, "Declined"),
      PENDING(0, "Pending");

      private static final Map<Integer, DespFriendStatus> lookup = new HashMap<Integer, DespFriendStatus>();
      static {
          for ( DespFriendStatus s : EnumSet.allOf(DespFriendStatus.class) ) {
              lookup.put(s.getCode(), s);
          }
      }
      public static DespFriendStatus get(int code) {
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

      private DespFriendStatus(int code, String value) {
          this.code = code;
          this.value = value;
      }

      @Override
      public String toString() {
          return this.value;
      }

      public static Collection<String> getItems() {
          DespFriendStatus[] values = DespFriendStatus.values();
          ArrayList<String> items = new ArrayList<>(values.length);
          for ( DespFriendStatus type : values ) {
              items.add(new String(type.value));
          }
          return items;
      }
  }
