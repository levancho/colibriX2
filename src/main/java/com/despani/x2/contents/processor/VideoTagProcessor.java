package com.despani.x2.contents.processor;

import com.despani.x2.contents.beans.VideoTag;
import com.despani.x2.contents.managers.ContentManager;
import com.despani.x2.core.events.DespOnContentEvent;
import com.despani.x2.core.interfaces.IManager;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class VideoTagProcessor implements ITagProcessors {

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
    public Document process(Document doc, IDespTagGenerator p) throws Exception {


        Elements allyoutubes = doc.select("avi,m4v,mkv,mp4,ogv,youtube,vimeo,dailymotion,facebook");
        if(allyoutubes==null || allyoutubes.size()<=0) {
            return doc;
        }


        allyoutubes.forEach(youtube -> {
            VideoTag t =  new VideoTag(youtube.text());
            t.setType(youtube.tagName());
            final Map variableMap = new HashMap();
            variableMap.put("avideo", t); // may be any Java object
            Element parent = youtube.parent();
            youtube.remove();
            try {
                String processor =  p.processor(variableMap, "/tags/amedia.ftl");
                parent.append(processor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return doc;
    }

    public  String replaceGroup(String regex, String source, int groupToReplace, int groupOccurrence, String replacement) {
        Matcher m = Pattern.compile(regex).matcher(source);
        for (int i = 0; i < groupOccurrence; i++)
            if (!m.find()) return source; // pattern not met, may also throw an exception here
        return new StringBuilder(source).replace(m.start(groupToReplace), m.end(groupToReplace), replacement).toString();
    }
}
