package com.foxminded.school.model;

public class Course {

	private Integer id;
	private String name;
	private String description;	
	
	public Course() {
	}	

	public Course(Integer id, String courseName) {
		this.id = id;
		this.name = courseName;
	}

	public Course(String courseName, String courseDescription) {
		this.name = courseName;
		this.description = courseDescription;
	}
	
	public Course(Integer id, String courseName, String courseDescription) {
		this.id = id;
		this.name = courseName;
		this.description = courseDescription;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setCourseName(String courseName) {
		this.name = courseName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setCourseDescription(String courseDescription) {
		this.description = courseDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id + " " + name;
	}
}
