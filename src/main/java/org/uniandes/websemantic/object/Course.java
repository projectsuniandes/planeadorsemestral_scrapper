package org.uniandes.websemantic.object;

import java.util.HashSet;
import java.util.Set;

public class Course {

	private String code, name;

	private Set<Course> prerequisites;

	public Course() {
		super();
		prerequisites = new HashSet<Course>();
	}

	public Course(String code, String name, Set<Course> prerequisites) {
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

	public Set<Course> getRequisites() {
		return prerequisites;
	}

	public void setRequisites(Set<Course> prerequisites) {
		this.prerequisites = prerequisites;
	}

	public void setName(String name) {
		this.name = name;
	}
}
