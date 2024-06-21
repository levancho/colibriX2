  package com.despani.x2.core.exceptions.base;


  import com.despani.x2.core.interfaces.IdespEnumTypes;
  import lombok.AllArgsConstructor;
  import lombok.Getter;
  import lombok.Setter;


  @Getter
  public  class DespSecurityException extends ADespBaseException {

      public DespSecurityException() {
          super($.DESP_Security_GENRIC_EXC,"!");
      }
      public DespSecurityException(String extTypeParam) {
          super($.DESP_Security_GENRIC_EXC,extTypeParam);
      }

      public DespSecurityException(IdespEnumTypes extTypeParam, Object... arguments) {
          super(extTypeParam, arguments);
      }

      @AllArgsConstructor
      @Getter
      public enum $ implements IdespEnumTypes {

          DESP_Security_GENRIC_EXC(9000,"","desp.security.generic.msg","Generic Security Exception {0}");

          final private int code;
          final private String title;
          final private String key;
          final private String defaultValue;

          @Override
          public String toString() {
              return this.code+":"+this.title+":"+this.key+":"+this.defaultValue;
          }


      }
  }
