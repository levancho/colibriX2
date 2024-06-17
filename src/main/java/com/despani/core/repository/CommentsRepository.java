package com.despani.core.repository;

import com.despani.core.beans.mongo.DespComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends MongoRepository<DespComment, String> {

    @Query("{ 'parent_id' : ?0 }")

    List<DespComment> findCommentsByParentId(String name);

    @Query("{ 'content_id' : ?0 }")
    Optional<List<DespComment>> findCommentsByContentOid(int name);

//    @Query("{ '_id':?0, 'votes': { 'value': ?1 ,'user._id':?2} } ")
//    @Query("{ '_id':?0, 'votes': { 'value': ?1} } ")
//    @Query("{ '_id':?0, 'votes.value':?1,'votes.user._id':?2 } ")
    @Query("{ '_id':?0, 'votes.user._id':?2 } ")
//    @Query("{ '_id':?0} ")
    Optional<DespComment> findCommentByVotedUser(String cid, int value, String oid);

}
