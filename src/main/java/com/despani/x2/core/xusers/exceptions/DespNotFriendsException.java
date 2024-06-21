package com.despani.x2.core.xusers.exceptions;

public class DespNotFriendsException extends DespUserException {
    public DespNotFriendsException(String myOid,String friendOid) {
        super($.DESP_FRIEND_REQUESTER_NOT_EXISTS, myOid,friendOid);
    }
}
