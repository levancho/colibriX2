package com.despani.core.exception;

public class DespFriendRequesterNotFound extends DespUserException{
    public DespFriendRequesterNotFound(String requesterOid) {
        super($.DESP_FRIEND_REQUESTER_NOT_EXISTS, requesterOid);
    }
}
