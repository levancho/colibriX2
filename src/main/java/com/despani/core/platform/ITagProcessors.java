package com.despani.core.platform;


import org.jsoup.nodes.Document;

public interface ITagProcessors {

    public Document process(Document content, IDespTagGenerator p) throws Exception;

}
