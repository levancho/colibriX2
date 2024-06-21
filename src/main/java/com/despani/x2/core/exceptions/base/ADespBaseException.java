  package com.despani.x2.core.exceptions.base;


  import com.despani.x2.core.interfaces.IdespEnumTypes;
  import lombok.Getter;
  import lombok.Setter;


  @Getter
  public abstract class ADespBaseException extends Exception {

      private IdespEnumTypes extType;
      @Setter
      private String key;
      @Setter
      private String title;
      @Setter
      private int code;
      @Setter
      private String value;
      @Setter
      private String serialValue;

      public void setExtType(IdespEnumTypes extType) {
          this.extType = extType;
          if(this.extType!=null) {
              this.key = this.extType.getKey();
              this.code = this.extType.getCode();
              this.title = this.extType.getTitle();
              this.value = this.extType.getValue();
              this.serialValue =   this.code+":"+this.title+":"+this.key+":"+this.value;
          }
      }

      public ADespBaseException(IdespEnumTypes extTypeParam) {
          this(extTypeParam, null);
      }
      public ADespBaseException(IdespEnumTypes extTypeParam, Object... arguments) {
          super(extTypeParam.getValue(arguments));
          setExtType(extTypeParam);
  //        this.setExtType(extTypeParam);
      }
  }
