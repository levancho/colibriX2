package com.despani.core.events;

import com.despani.core.annotations.DespAsyncEvent;
import com.despani.core.beans.mongo.DespComment;
import com.despani.core.interfaces.IManager;
import lombok.Getter;

@DespAsyncEvent(async = true)
public class DespOnEditCommentEvent extends DespAppEvent {

    @Getter
    private DespComment comment;

    public DespOnEditCommentEvent(Object source, IManager target) {
        super(source, target);
    }
}
