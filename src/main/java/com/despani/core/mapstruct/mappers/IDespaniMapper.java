package com.despani.core.mapstruct.mappers;

import com.despani.core.beans.DespReferenceData;
import com.despani.core.beans.domains.*;
import com.despani.core.beans.form.*;
import com.despani.core.beans.mongo.DespComment;
import com.despani.core.beans.mongo.DespaniMongoUser;
import com.despani.core.utils.DespPropertyX2;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IDespaniMapper {

    IDespaniMapper MAPPER = Mappers.getMapper( IDespaniMapper.class );

    DespContent toDespaniContent(ContentForm s );

    DespPropertyX2 toDespPropertyX2(PropertyDataForm p);


    @Mapping(source = "name", target = "typeKey")
    @Mapping(source = "propValue", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "published", target = "published")
    @Mapping(source = "refValues", target = "values")
    DespReferenceData toDespReferenceData(PropertyDataForm r);


    @Mapping(source = "oid", target = "oid")
    DespComment toDespaniComment(CommentForm s );

    DespMenuItem toDespaniMenu(MenuFormItem mf );

    @Mapping(source = "oid", target = "oid")
    @Mapping(source = "oid", target = "_id")
    DespaniMongoUser toMongoUser(DespaniUser du);

    @Mapping(source = "name", target="title")
    DespCategoryItem toDespaniCategory(CategoryFormItem s );

    @Mapping(target = "roles", ignore = true)
    DespaniUser toDespaniUser(UserForm form);


    DespModule toDespaniModule(ModuleForm m);

    DespRole toDespRole(RoleForm roleForm);




}
