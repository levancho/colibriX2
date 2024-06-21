  package com.despani.x2.core.interfaces;


import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

  public interface IdespEnumTypes {

      int getCode();
      String getKey();
      String getTitle();
      String getDefaultValue();
//      String getValue(Object ... args);

      default String getValue(Object ... arguments){
          if(arguments!=null) {
              return  MessageFormat.format(
                      getDefaultValue(),
                      arguments);
          }else {
              return getDefaultValue();
          }
      }

      public default Map toJSON(Object ... arguments) {
          Map m = new HashMap();
          m.put("title",this.getTitle());
          m.put("code",this.getCode());
          m.put("key",this.getKey());
          m.put("value",this.getValue(arguments));
          return m;
      }

  }
