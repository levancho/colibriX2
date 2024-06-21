package com.despani.x2.core.xusers.exceptions;

public class DespFriendRequesterNotFound extends DespUserException {
    public DespFriendRequesterNotFound(String requesterOid) {
        super($.DESP_FRIEND_REQUESTER_NOT_EXISTS, requesterOid);
    }
}
