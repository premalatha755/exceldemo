
package com.sample.excel.exceldemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Snapshot {

	@JsonProperty("dynamic_folders")
	private DynamicFolders dynamicFolders;

	@JsonProperty("dynamic_folders")
	public DynamicFolders getDynamicFolders() {
		return dynamicFolders;
	}

	@JsonProperty("dynamic_folders")
	public void setDynamicFolders(DynamicFolders dynamicFolders) {
		this.dynamicFolders = dynamicFolders;
	}

}
