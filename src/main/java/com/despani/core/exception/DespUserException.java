package com.despani.core.exception;

import com.despani.core.exceptions.base.ADespBaseException;
import com.despani.core.interfaces.IdespEnumTypes;

import java.text.MessageFormat;

public class DespUserException extends ADespBaseException {


    public DespUserException(IdespEnumTypes extTypeParam, Object... arguments) {
        super(extTypeParam, arguments);
    }

    public enum $ implements IdespEnumTypes {


        DESP_FRIEND_REQUESTER_NOT_EXISTS(7000,"desp.friend.requester.error","Requester with oid {0} doesn't exists"),
        DESP_FRIEND_REQUESTER_STATUS_NOT_VALID(7001,"desp.friend.requester.status.error","Friend Requester status : {0} is not valid"),
        DESP_USERS_NOT_FRIENDS_ERROR(7002,"desp.user.not.friends.error","Despani user : {0} and despani user {1} are nit friends"),

        DESP_USER_GENERIC_ERROR(1000, "desp.user.generic.error", "Generic error occured"),
        DESP_USER_NOT_ACTIVE(1001, "desp.user.inactive", "Your email activation link has already  expired."),
        DESP_USER_EMAIL_TOKEN_NOT_VERIFIED(1002, "desp.user.email.token.not.verified", "Your email activation link is not verified "),
        DESP_USER_EMAIL_TOKEN_EXPIRED(1003, "desp.user.email.token.expired", "Your email activation link has already  expired."),
        DESP_USER_EMAIL_TOKEN_INVALID(1004, "desp.user.email.token.invalid", "Your email activation link is invalid."),
        DESP_USER_EMAIL_ALREADY_VERIFIED(1005, "desp.user.email.aready.verified", "Your email activation link is already verified."),
        DESP_USER_EMAIL_TOKEN_ALREADY_VERIFIED(1006, "desp.user.email.token.aready.verified", "Your email activation link is already  verified."),
        DESP_USER_PASSWORDS_NOT_EQUAL(1007,"desp.user.password.not.equal","Confirmation password is not same"),
        DESP_USER_EMAIL_INVALID(1008,"desp.user.email.invalid","Provided Email {0} is Invalid"),
        DESP_USER_NAME_INVALID(1009,"desp.user.name.invalid","Provided Username {0} is Invalid"),
        DESP_USER_CREATED_ON(1010,"desp.user.created.on","As time went on more than 1 day"),
        DESP_USER_EMAIL_ALREADY_EXISTS(1011,"desp.user.email.already.exists","User with this email {0} is already registered"),
        DESP_USER_NAME_ALREADY_EXISTS(1012,"desp.user.username.already.exists","Username: {0} is already taken"),
        DESP_USER_PASSWORD_LENGTH(1013,"desp.user.password.length","Password is too short"),
        DESP_USER_PASSWORD_STRENGTH(1014,"desp.user.password.strength","Password is not strong enough"),
        DESP_USER_NOT_FOUND(1015,"desp.user.not.found","User not found"),
        DESP_USER_REALITY_CHECK(1016,"desp.user.realitycheck.invalid","reality check error"),
        DESP_USER_DUPLICATE_RECORD(1017,"desp.user.duplicate.record","Another record found with same name and address,"),
        DESP_USER_SAME_PASSWORDS(1018,"desp.user.password.same","Original and new Passwords are the same"),
        DESP_USER_INVALID_PASS_OR_ID(1019,"desp.user.password.invalid","Original password supplied is invalid"),
        DESP_USER_INVALID_USER_HASH(1020,"desp.user.hash.invalid","Provided user hash is invalid"),
        DESP_USER_INVALID_TOKEN(1021,"desp.user.token.invalid","User Token is not valid");

        private short code;
        private String key;
        private String value;

        public short getCode() {
            return code;
        }

        public String getKey() {
            return key;
        }

        public String getValue(Object ... arguments){
            if(arguments!=null) {
              return  MessageFormat.format(
                        value,
                        arguments);
            }else {
                return value;
            }
        }

        private $(int codeParam, String keyParam, String valueParam) {
            this.code = (short) codeParam;
            this.key = keyParam;
            this.value = valueParam;
        }

        @Override
        public String toString() {

            return this.code+":"+this.key+":"+this.value;

        }

    }


}
