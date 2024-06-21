package com.despani.x2.contents.services;

import com.despani.x2.contents.beans.mongo.DespComment;
import com.despani.x2.contents.repository.CommentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CommentsService {


    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private MongoOperations mongoOps;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void createComment(DespComment comment) {
        commentsRepository.insert(comment);
    }

    public DespComment editComment(DespComment toEdit) {
        Optional<DespComment> comment = commentsRepository.findById(toEdit.getOid());


        if(comment.isPresent()){
//            DespComment despComment = comment.get();
            DespComment despComment = comment.get();
            despComment.setComment(toEdit.getComment());
            toEdit = commentsRepository.save(despComment);
            return despComment;
        }else {
            throw new RuntimeException("comment for that ID " + toEdit.getOid() + " does not exist");
        }
    }

    public DespComment deleteCOmment(String oid) {
        Optional<DespComment> comment = commentsRepository.findById(oid);
        if(comment.isPresent()){
//            DespComment despComment = comment.get();
            commentsRepository.delete(comment.get());
            return comment.get();
        }
        return null;
    }

    public Optional<List<DespComment>> getAllCommentsForContent(Integer oid) {
        return commentsRepository.findCommentsByContentOid(oid);
    }

    public Optional<DespComment> getCommentById(String oid) {
        return commentsRepository.findById(oid);
    }

    public void saveComment(DespComment despComment) {
        commentsRepository.save(despComment);
    }

    public DespComment findCommentByVotedUser(String cid, String oid) {
        Query que = new Query();
//        que.addCriteria(Criteria.where("votes.user.$id").is(oid).and("votes.value").is(value).and("$id").is(cid));
        que.addCriteria(Criteria.where("_id").is(cid).and("votes.user.$id").is(oid));
       return  mongoTemplate.findOne(que,DespComment.class);
//        return commentsRepository.findCommentByVotedUser(cid,value,oid);
    }

    public DespComment findCommentByVotedUser(String cid, Integer value, String oid) {
        Query que = new Query();
//        que.addCriteria(Criteria.where("votes.user.$id").is(oid).and("votes.value").is(value).and("$id").is(cid));
        que.addCriteria(Criteria.where("_id").is(cid).and("votes.user.$id").is(oid).and("votes.value").is(value));
        return  mongoTemplate.findOne(que,DespComment.class);
//        return commentsRepository.findCommentByVotedUser(cid,value,oid);
    }
}
