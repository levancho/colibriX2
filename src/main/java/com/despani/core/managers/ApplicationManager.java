package com.despani.core.managers;

import com.despani.core.managers.base.ADespContentManager;
import com.despani.core.platform.IDespTagGenerator;
import com.despani.core.services.ModuleService;
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
