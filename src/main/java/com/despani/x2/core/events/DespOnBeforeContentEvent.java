package com.despani.x2.core.events;

import com.despani.x2.core.annotations.DespAsyncEvent;
import com.despani.x2.core.interfaces.IManager;

@DespAsyncEvent
public class DespOnBeforeContentEvent extends DespAppEvent {


    public DespOnBeforeContentEvent(Object source, IManager manager) {
        super(source, manager);
    }
}
