  package com.despani.core.exceptions.base;


  import com.despani.core.interfaces.IdespEnumTypes;

  public abstract class ADespBaseRuntimeException extends RuntimeException {

      private IdespEnumTypes extType;
      private String key;
      private int code;

      public String getSerialValue() {
          return serialValue;
      }

      public void setSerialValue(String serialValue) {
          this.serialValue = serialValue;
      }

      private String serialValue;

      public String getKey() {
          return key;
      }

      public void setKey(String key) {
          this.key = key;
      }

      public String getValue() {
          return value;
      }

      public void setValue(String value) {
          this.value = value;
      }

      private String value;


      public IdespEnumTypes getExtType() {
          return extType;
      }

      public void setExtType(IdespEnumTypes extType) {
          this.extType = extType;

          if(this.extType!=null) {
              this.key = this.extType.getKey();
              this.code = this.extType.getCode();
              this.value = this.extType.getValue();
              this.serialValue =   this.code+":"+this.key+":"+this.value;
          }
      }

      public int getCode() {
          return code;
      }

      public void setCode(int code) {
          this.code = code;
      }


      public ADespBaseRuntimeException(IdespEnumTypes extTypeParam) {
          this(extTypeParam, null);
      }
      public ADespBaseRuntimeException(IdespEnumTypes extTypeParam, Object... arguments) {
          super(extTypeParam.getValue(arguments));
          setExtType(extTypeParam);
          //        this.setExtType(extTypeParam);
      }
  }
