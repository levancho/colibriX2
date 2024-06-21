package com.despani.x2.core.xmodules.beans.form;


import com.despani.x2.core.beans.form.ABaseForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleForm extends ABaseForm {
    private String title;
    private String internalTitle;
    private String content;
    private String module;
    private String position;
    private int ordering;
    private boolean showTitle;
    private boolean published;
}
