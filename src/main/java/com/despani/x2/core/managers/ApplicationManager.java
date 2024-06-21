package com.despani.x2.core.managers;

import com.despani.x2.core.managers.base.ADespContentManager;
import com.despani.x2.contents.processor.IDespTagGenerator;
import com.despani.x2.core.xmodules.services.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationManager extends ADespContentManager {

    @Autowired
    private ModuleService modService;

    @Autowired
    private IDespTagGenerator tagGen;


    public  String  getManagerbaseURI(){
        return "app";
    }
}
