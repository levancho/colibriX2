package com.despani.x2.company.beans.domains;

import com.despani.x2.core.beans.base.ADespaniContentObject;
import com.despani.x2.core.beans.enums.DespContentTypes;
import com.despani.x2.core.xusers.beans.domains.DespAddress;
import com.despani.x2.core.xusers.beans.domains.DespaniUser;
import com.despani.x2.core.xmedia.beans.domains.DespMedia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
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
