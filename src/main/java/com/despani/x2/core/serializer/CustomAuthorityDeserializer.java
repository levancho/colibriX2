package com.despani.x2.core.serializer;

import  com.despani.x2.core.xusers.beans.domains.DespRole;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomAuthorityDeserializer extends JsonDeserializer {

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        List<DespRole> grantedAuthorities = new ArrayList<>();

        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()) {
            JsonNode next = elements.next();


            DespRole dr = new DespRole(next.get("role").asText());
            dr.setOid(next.get("oid").asInt());
            dr.setRoleDescription(next.get("roleDescription").asText());
            dr.setCreatedByOid(next.get("createdByOid").asInt());
            dr.setUpdatedByOid(next.get("updatedByOid").asInt());
            dr.setRole(next.get("role").asText());
            dr.setWeight(next.get("weight").asInt());

            grantedAuthorities.add(dr);
        }
        return grantedAuthorities;
    }
}
