package com.despani.x2.contents.processor;

import com.despani.x2.contents.managers.ContentManager;
import com.despani.x2.core.events.DespOnContentEvent;
import com.despani.x2.core.interfaces.IManager;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TabsTagProcessor implements ITagProcessors {



    public TabsTagProcessor() {
    }

    // @Async
    @EventListener
    public void handleOnContent(DespOnContentEvent cse) throws Exception {
        log.info("Handling context started event.{}",cse);

        IManager manager = cse.getTarget();
        if(manager instanceof ContentManager) {
            ContentManager cm= (ContentManager) manager;
            process(cse.getDoc(),cm);
        }
    }


    @Override
    public Document process(Document doc , IDespTagGenerator p) throws Exception {

        System.out.println("------ {tabs} START-------------");

        Elements allTabs = doc.select("tabs");

        if(allTabs==null || allTabs.size()<=0) {
            return doc;
        }


        allTabs.forEach(tabs -> {
//            tabs.tagName("div");
            tabs.tagName("div");

            String style = "style1";
           if(tabs.hasAttr("type")){
               style=tabs.attr("type");
           }

            Elements Alltab = tabs.select("tab");
            tabs.select("tab").remove();


            final Map variableMap = new HashMap();
            variableMap.put("tabz", Alltab); // may be any Java object
            variableMap.put("type", style); // may be any Java object

            try {
                String processor =  p.processor(variableMap, "/tags/tabs.ftl");
                tabs.append(processor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return doc;
    }

}
