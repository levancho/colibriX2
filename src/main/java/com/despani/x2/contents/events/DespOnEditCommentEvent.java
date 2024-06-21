package com.despani.x2.contents.events;

import com.despani.x2.contents.beans.mongo.DespComment;
import com.despani.x2.core.annotations.DespAsyncEvent;
import com.despani.x2.core.events.DespAppEvent;
import com.despani.x2.core.interfaces.IManager;
import lombok.Getter;

@DespAsyncEvent(async = true)
public class DespOnEditCommentEvent extends DespAppEvent {

    @Getter
    private DespComment comment;

    public DespOnEditCommentEvent(Object source, IManager target) {
        super(source, target);
    }
}
