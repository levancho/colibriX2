  package com.despani.core.interfaces;


import java.util.HashMap;
import java.util.Map;

  public interface IdespEnumTypes {

      short getCode();
      String getKey();
      String getValue(Object ... args);


      public default Map toJSON() {
          Map m = new HashMap();
          m.put("name",this.toString());
          m.put("code",this.getCode());
          m.put("key",this.getKey());
          m.put("value",this.getValue());
          return m;
      }

  }
