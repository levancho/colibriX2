package com.despani.core.exception;

public class DespFriendReqiesterStatusNotValid extends DespUserException{
    public DespFriendReqiesterStatusNotValid(String requesterStatus) {
        super($.DESP_FRIEND_REQUESTER_STATUS_NOT_VALID, requesterStatus);
    }
}
