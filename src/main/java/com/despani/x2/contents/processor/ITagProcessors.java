package com.despani.x2.contents.processor;


import org.jsoup.nodes.Document;

public interface ITagProcessors {

    public Document process(Document content, IDespTagGenerator p) throws Exception;

}
