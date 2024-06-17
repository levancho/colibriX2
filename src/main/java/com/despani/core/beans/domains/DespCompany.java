package com.despani.core.beans.domains;

import com.despani.core.beans.base.ADespaniContentObject;
import com.despani.core.beans.enums.DespContentTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DespCompany extends ADespaniContentObject {


    @NotNull(message = "bla bla bla")
    protected String name;
    protected DespAddress address;
    protected DespaniUser representative;
    protected List<DespMedia> media;
    protected String type;
    protected String description;

    public DespCompany(int oid){
        this.oid = oid;
    }

    @Override
    public DespContentTypes getContentType() {
        return DespContentTypes.COMPANY;
    }


}
