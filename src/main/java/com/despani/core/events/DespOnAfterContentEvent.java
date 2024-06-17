package com.despani.core.events;

import com.despani.core.annotations.DespAsyncEvent;
import com.despani.core.interfaces.IManager;

@DespAsyncEvent
public class DespOnAfterContentEvent extends DespAppEvent {
    public DespOnAfterContentEvent(String content, IManager managaer) {
        super(content,  managaer);
    }
}
