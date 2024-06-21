package com.despani.x2.core.managers;

import com.despani.x2.core.beans.DynamicActionBarItem;
import com.despani.x2.core.beans.DespReferenceData;
import com.despani.x2.core.interfaces.IActionBarItem;
import com.despani.x2.core.beans.StaticActionBarItem;
import com.despani.x2.core.beans.base.ADespaniDisplayObject;
import com.despani.x2.core.managers.base.ADespContentManager;
import com.despani.x2.core.utils.DespGlobals;
import org.springframework.context.ApplicationEventPublisher;
import com.despani.x2.core.services.RefDataServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class AdminManager extends ADespContentManager {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private RefDataServices refDataService;

    private String action;
    private String styleName;
    private String label;
    private String name;
    private String icon;
    private int xorder;


    public IActionBarItem getActionBarItem(ADespaniDisplayObject obj, String action, String name, String label, String styleName, String icon) {
        return new DynamicActionBarItem(obj, action, name, label, styleName, icon);
    }


    public IActionBarItem getStaticActionBarItem(String action, String name, String label, String styleName, String icon) {
        return new StaticActionBarItem(action, name, label, styleName, icon);
    }


    public String getManagerbaseURI() {
        return "admin";
    }


    public DespReferenceData getRefData(String key) {
        return refDataService.getReferenceData(key);
    }


    public DespReferenceData getReferenceDataByOid (Integer oid) {
        return refDataService.getReferenceDataByOid(oid);
    }



    public boolean hasRefData(String key) {
        return refDataService.hasRefData(key);
    }


    public String getLocaleLabel(String languageCode) {
        if (!DespGlobals.sitelangs.containsKey(languageCode)) return languageCode;
        return DespGlobals.sitelangs.get(languageCode);

    }

}

