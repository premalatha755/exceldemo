
package com.sample.excel.exceldemo.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class DynamicFolders {

    @JsonIgnore
    private Map<String, KeyObj> keyProperties = new HashMap<String, KeyObj>();


    @JsonAnyGetter
    public Map<String, KeyObj> getkeyProperties() {
        return this.keyProperties;
    }

    @JsonAnySetter
    public void setKeyProperty(String name, KeyObj value) {
        this.keyProperties.put(name, value);
    }

}
