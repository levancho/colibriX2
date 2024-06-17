package com.despani.core.events;

import com.despani.core.annotations.DespAsyncEvent;
import com.despani.core.interfaces.IManager;

@DespAsyncEvent
public class DespOnBeforeContentEvent extends DespAppEvent {


    public DespOnBeforeContentEvent(Object source, IManager manager) {
        super(source, manager);
    }
}
