package com.despani.x2.core.xusers.beans.domains;

import com.despani.x2.core.xusers.beans.enums.DespFriendStatus;
import lombok.Data;

@Data
public class DespFriend extends DespaniUser {
    public DespFriend() {
    }

    private DespFriendStatus status;

}
