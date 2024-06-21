package com.despani.x2.core.xusers.exceptions;

import com.despani.x2.core.exceptions.base.ADespBaseException;
import com.despani.x2.core.interfaces.IdespEnumTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class DespUserException extends ADespBaseException {


    public DespUserException(IdespEnumTypes extTypeParam, Object... arguments) {
        super(extTypeParam, arguments);
    }

    @AllArgsConstructor
    @Getter
    public enum $ implements IdespEnumTypes {


        DESP_FRIEND_REQUESTER_NOT_EXISTS(7000,"User Exception","desp.friend.requester.error","Requester with oid {0} doesn't exists"),
        DESP_FRIEND_REQUESTER_STATUS_NOT_VALID(7001,"User Exception","desp.friend.requester.status.error","Friend Requester status : {0} is not valid"),
        DESP_USERS_NOT_FRIENDS_ERROR(7002,"User Exception","desp.user.not.friends.error","Despani user : {0} and despani user {1} are nit friends"),

        DESP_USER_NOT_ACTIVE(7011, "User Exception","desp.user.inactive", "Your email activation link has already  expired."),
        DESP_USER_EMAIL_TOKEN_NOT_VERIFIED(7012, "User Exception","desp.user.email.token.not.verified", "Your email activation link is not verified "),
        DESP_USER_EMAIL_TOKEN_EXPIRED(7003, "User Exception","desp.user.email.token.expired", "Your email activation link has already  expired."),
        DESP_USER_EMAIL_TOKEN_INVALID(7004, "User Exception","desp.user.email.token.invalid", "Your email activation link is invalid."),
        DESP_USER_EMAIL_ALREADY_VERIFIED(7005,"User Exception", "desp.user.email.aready.verified", "Your email activation link is already verified."),
        DESP_USER_EMAIL_TOKEN_ALREADY_VERIFIED(7006,"User Exception", "desp.user.email.token.aready.verified", "Your email activation link is already  verified."),
        DESP_USER_PASSWORDS_NOT_EQUAL(7007,"User Exception","desp.user.password.not.equal","Confirmation password is not same"),
        DESP_USER_EMAIL_INVALID(7008,"User Exception","desp.user.email.invalid","Provided Email {0} is Invalid"),
        DESP_USER_NAME_INVALID(7009,"User Exception","desp.user.name.invalid","Provided Username {0} is Invalid"),
        DESP_USER_CREATED_ON(7010,"User Exception","desp.user.created.on","As time went on more than 1 day"),
        DESP_USER_EMAIL_ALREADY_EXISTS(7011,"User Exception","desp.user.email.already.exists","User with this email {0} is already registered"),
        DESP_USER_NAME_ALREADY_EXISTS(7012,"User Exception","desp.user.username.already.exists","Username: {0} is already taken"),
        DESP_USER_PASSWORD_LENGTH(7013,"User Exception","desp.user.password.length","Password is too short"),
        DESP_USER_PASSWORD_STRENGTH(7014,"User Exception","desp.user.password.strength","Password is not strong enough"),
        DESP_USER_NOT_FOUND(7015,"User Exception","desp.user.not.found","User not found"),
        DESP_USER_REALITY_CHECK(7016,"User Exception","desp.user.realitycheck.invalid","reality check error"),
        DESP_USER_DUPLICATE_RECORD(7017,"User Exception","desp.user.duplicate.record","Another record found with same name and address,"),
        DESP_USER_SAME_PASSWORDS(7018,"User Exception","desp.user.password.same","Original and new Passwords are the same"),
        DESP_USER_INVALID_PASS_OR_ID(7019,"User Exception","desp.user.password.invalid","Original password supplied is invalid"),
        DESP_USER_INVALID_USER_HASH(7020,"User Exception","desp.user.hash.invalid","Provided user hash is invalid"),
        DESP_USER_INVALID_TOKEN(7021,"User Exception","desp.user.token.invalid","User Token is not valid");

        final private int code;
        final private String title;
        final private String key;
        final private String defaultValue;

        @Override
        public String toString() {
            return this.code+":"+this.title+":"+this.key+":"+this.defaultValue;
        }

    }


}
