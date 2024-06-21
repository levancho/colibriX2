package com.despani.x2.core.mapstruct;

import com.despani.x2.core.beans.DespReferenceData;
import com.despani.x2.core.beans.form.*;
import com.despani.x2.core.xmenus.beans.domains.DespMenuItem;
import com.despani.x2.core.xmenus.beans.form.MenuFormItem;
import com.despani.x2.core.xusers.beans.domains.DespRole;
import com.despani.x2.core.xusers.beans.domains.DespaniUser;
import com.despani.x2.core.xusers.beans.form.RoleForm;
import com.despani.x2.core.xusers.beans.form.UserForm;
import com.despani.x2.core.utils.DespPropertyX2;
import com.despani.x2.core.xmodules.beans.domains.DespModule;
import com.despani.x2.core.xmodules.beans.form.ModuleForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IDespaniMapper {

    IDespaniMapper MAPPER = Mappers.getMapper( IDespaniMapper.class );


    DespPropertyX2 toDespPropertyX2(PropertyDataForm p);


    @Mapping(source = "name", target = "typeKey")
    @Mapping(source = "propValue", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "published", target = "published")
    @Mapping(source = "refValues", target = "values")
    DespReferenceData toDespReferenceData(PropertyDataForm r);



    DespMenuItem toDespaniMenu(MenuFormItem mf );



    @Mapping(target = "roles", ignore = true)
    DespaniUser toDespaniUser(UserForm form);


    DespModule toDespaniModule(ModuleForm m);

    DespRole toDespRole(RoleForm roleForm);




}
