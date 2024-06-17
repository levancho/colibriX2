package com.despani.core.beans.domains;

import com.despani.core.beans.enums.DespFriendStatus;
import lombok.Data;

@Data
public class DespFriend extends DespaniUser {
    public DespFriend() {
    }

    private DespFriendStatus status;

}
