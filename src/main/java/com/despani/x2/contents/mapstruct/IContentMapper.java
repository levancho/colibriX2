package com.despani.x2.contents.mapstruct;

import com.despani.x2.categories.beans.domains.DespCategoryItem;
import com.despani.x2.categories.beans.form.CategoryFormItem;
import com.despani.x2.contents.beans.domains.DespContent;
import com.despani.x2.contents.beans.form.CommentForm;
import com.despani.x2.contents.beans.form.ContentForm;
import com.despani.x2.contents.beans.mongo.DespComment;
import com.despani.x2.core.xusers.beans.mongo.DespaniMongoUser;
import com.despani.x2.core.xusers.beans.domains.DespaniUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IContentMapper {

    IContentMapper MAPPER = Mappers.getMapper( IContentMapper.class );

    DespContent toDespaniContent(ContentForm s );


    @Mapping(source = "oid", target = "oid")
    DespComment toDespaniComment(CommentForm s );


    @Mapping(source = "oid", target = "oid")
    @Mapping(source = "oid", target = "_id")
    DespaniMongoUser toMongoUser(DespaniUser du);

    @Mapping(source = "name", target="label")
    DespCategoryItem toDespaniCategory(CategoryFormItem s );





}
