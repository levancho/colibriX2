package com.despani.core.platform;


import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

public interface IDespTagGenerator {


    String processor(Map variableMap, String s) throws Exception;

}
