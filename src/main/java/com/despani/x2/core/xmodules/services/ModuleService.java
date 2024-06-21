package com.despani.x2.core.xmodules.services;

import com.despani.x2.core.beans.DespReferenceData;
import com.despani.x2.core.beans.DespReferenceValue;
import com.despani.x2.core.xmodules.beans.domains.DespModule;
import com.despani.x2.core.services.RefDataServices;
import com.despani.x2.core.xmodules.beans.form.ModulePositionForm;
import com.despani.x2.core.xmodules.mybatis.mappers.IModulesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class ModuleService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected IModulesMapper modMapper;

    @Autowired
    protected RefDataServices refDataServices;

    List<DespModule> modules = null;
    Map<String, List<DespModule>> modulesAsMap = null;

    public void invalidateModules () {
        modules=null;
        loadModules();
    }

    public void loadModules() {
        if(modules==null) {
        modules = modMapper.loadModules();
        modulesAsMap = new HashMap<>();
        modules.stream().forEach(i -> {

            if (modulesAsMap.containsKey(i.getPosition())) {
                modulesAsMap.get(i.getPosition()).add(i);
            } else {
                List<DespModule> local = new ArrayList<>();
                local.add(i);
                modulesAsMap.put(i.getPosition(), local);
            }
        });
        }else {
            log.info("Data is already loaded");
        }
    }

    /** returns all modules in Hashmap grouped by module position **/
    public Map<String, List<DespModule>> getModulesMap() {
        loadModules();
        return modulesAsMap;
    }

    /** returns modules max ordering number as Integer by provided module position and module type
     * result is NULL, returns 0 **/
    public Integer getModuleMaxOrderingByPosition (String position){
        Integer maxOrder = modMapper.getModuleMaxOrderingByPosition(position);

        if (maxOrder==null){
            maxOrder=0;
            return maxOrder;
        }
        else{
            return maxOrder;
        }

    }

    public void reset() {
        modules = null;
        loadModules();
    }

    public List<DespModule> getModules() {
        loadModules();
        return modules;
    }

// TODO
//    public List<DespPropertyX2> getModuleTypes (){
//        List<DespPropertyX2> moduleTypes = DespGlobals.getListPropertyAsProperty("site.3.module-types");
//        return moduleTypes;
//    }

    public void insertModule (DespModule despModule) {
        modMapper.insertMODULE(despModule);
    }

//    HERE //
    public List<DespReferenceValue> getPositions(String key) {
        DespReferenceData refData = refDataServices.getReferenceData("site.3.module-position");
        return refData.getValues();
    }

    public void publishModule(int oid, boolean published){
        modMapper.publishModule(oid, published);
        invalidateModules();
    }

    public void showTitle(int oid, boolean showTitle){
        modMapper.showTitle(oid, showTitle);
        invalidateModules();
    }

    public DespModule getModuleByOid(Integer oid) {
        return modMapper.getModuleByOid(oid);
    }

    public void updateModulePositions(List <ModulePositionForm> positions){
                positions.forEach((position) ->  modMapper.updateModulePosition(position));
                invalidateModules();
    }

    public void updateModuleByOid(DespModule despModule) {
        modMapper.updateMODULE(despModule);
        invalidateModules();

    }

    public void deleteModule (Integer oid) {
        modMapper.deleteModule(oid);
        invalidateModules();
    }


}
