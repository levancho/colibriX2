package com.despani.core.events;

import com.despani.core.annotations.DespAsyncEvent;
import com.despani.core.interfaces.IManager;
import org.jsoup.nodes.Document;

@DespAsyncEvent(async = false)
public class DespOnContentEvent extends DespAppEvent {

    public Document getDoc() {
        return (Document) this.getSource();
    }
    public DespOnContentEvent(Document doc, IManager managaer) {
        super(doc,managaer);
    }

}
