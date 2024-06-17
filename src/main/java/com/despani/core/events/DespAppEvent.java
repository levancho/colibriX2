package com.despani.core.events;

import com.despani.core.interfaces.IManager;
import org.springframework.context.ApplicationEvent;

public class DespAppEvent extends ApplicationEvent {


    private IManager target;
    public IManager getTarget() {
        return target;
    }

    public void setTarget(IManager target) {
        this.target = target;
    }

    public DespAppEvent(Object source, IManager target) {
        super(source);
        this.target = target;
    }
}
