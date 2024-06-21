package com.despani.x2.contents.controllers.mvc;

import com.despani.x2.contents.beans.form.ContentForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ContentValidator implements Validator {

    /**
     * This Validator validates just Person instances
     */
    public boolean supports(Class clazz) {
        return ContentForm.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
//        e.
//        e.rejectValue("contentForm", "jondo");
        e.reject("juansheri");

        ContentForm c = (ContentForm) obj;
        if (c.getTitle()==null) {
            e.rejectValue("title", "is null");
        } else if (c.getTitle().trim().equalsIgnoreCase("")) {
            e.rejectValue("title", "empty");
        }
    }


}
