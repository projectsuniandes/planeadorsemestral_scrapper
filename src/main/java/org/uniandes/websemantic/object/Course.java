package org.uniandes.websemantic.object;

import java.util.HashSet;
import java.util.Set;

public class Course {

	private String code, name;

	private Set<String> prerequisites;

	public Course() {
		super();
		prerequisites = new HashSet<String>();
	}

	public Course(String code, String name, Set<String> prerequisites) {
		this.code = code;
		this.name = name;
		this.prerequisites = prerequisites;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<String> getRequisites() {
		return prerequisites;
	}

	public void setRequisites(Set<String> prerequisites) {
		this.prerequisites = prerequisites;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "{ code: " + code + ",\n"
			+ "name: " + name + ",\n"
			+ "prerequisites: " + prerequisites;
	}
}
