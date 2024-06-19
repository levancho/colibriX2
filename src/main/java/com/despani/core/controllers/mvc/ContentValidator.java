package com.despani.core.controllers.mvc;

import com.despani.core.beans.form.ContentForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import jakarta.validation.ConstraintViolation;
import java.util.Set;

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
