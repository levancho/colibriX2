package com.despani.x2.contents.beans.mongo;

import com.despani.x2.contents.mongo.annotation.CascadeSave;
import com.despani.x2.core.xusers.beans.mongo.DespaniMongoUser;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//  {
//          _id: ObjectId(...),
//          discussion_id: ObjectId(...),
//          parent_id: ObjectId(...),
//          slug: '34db/8bda'
//          full_slug: '2012.02.08.12.21.08:34db/2012.02.09.22.19.16:8bda',
//          posted: ISODateTime(...),
//          author: {
//          id: ObjectId(...),
//          name: 'Rick'
//          },
//          text: 'This is so bogus ... '
//          }
//
//CREATE TABLE `despaniJoomla2`.`Untitled`  (
//        `id` int(10) NOT NULL AUTO_INCREMENT,
//        `parentid` int(10) NOT NULL DEFAULT 0,
//        `status` int(10) NOT NULL DEFAULT 0,
//        `contentid` int(10) NOT NULL DEFAULT 0,
//        `ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
//        `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
//        `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
//        `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
//        `preview` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
//        `date` datetime(0) NOT NULL DEFAULT '0000-00-00 00:00:00',
//        `published` tinyint(1) NOT NULL DEFAULT 0,
//        `ordering` int(11) NOT NULL DEFAULT 0,
//        `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
//        `website` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
//        `updateme` smallint(5) UNSIGNED NOT NULL DEFAULT 0,
//        `custom1` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
//        `custom2` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
//        `custom3` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
//        `custom4` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
//        `custom5` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
//        `star` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
//        `user_id` int(10) UNSIGNED NOT NULL DEFAULT 0,
//        `option` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'com_content',
//        `voted` smallint(6) NOT NULL DEFAULT 0,
//        `referrer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
//        `referer` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
//        PRIMARY KEY (`id`) USING BTREE,
//        UNIQUE INDEX `id_idx`(`id`) USING BTREE,
//        INDEX `option`(`option`) USING BTREE,
//        INDEX `published`(`published`) USING BTREE,
//        INDEX `contentid`(`contentid`) USING BTREE,
//        INDEX `user_id`(`user_id`) USING BTREE
//        ) ENGINE = MyISAM AUTO_INCREMENT = 103435 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'added user_id index and id index' ROW_FORMAT = Dynamic;


@Document
@Data
public class DespComment {

    @Id
    protected String oid;
    protected String parent_id;// parent comment object oid
    protected int content_id;// content id from DB
    protected String ip;
    protected String title;
    protected String comment;
    protected String preview;
    protected boolean published;
    protected String slug;
    protected String fullSlug;
    protected int ordering;

    protected int totalvotes;
    protected int totalvotessum;


    @Field("votes")
    private List<DespVote> votes = new ArrayList<DespVote>();


    @Field("reports")
    private List<DespComplaint> reports = new ArrayList<DespComplaint>();

    @DBRef
    @CascadeSave
    @Field("user")
    protected DespaniMongoUser user;

    @DateTimeFormat(style = "M-")
    protected Date createdOn;
    @DateTimeFormat(style = "M-")
    protected Date updatedOn;

    @PersistenceConstructor
    public DespComment() {
        this.user = new DespaniMongoUser();
        this.user.setUsername("anon");
        this.createdOn=new Date();
        this.slug= RandomStringUtils.random(20, true, true);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        String dateString = format.format( createdOn );
        this.fullSlug =dateString+":"+this.slug;
        this.published=true;

    }
}
