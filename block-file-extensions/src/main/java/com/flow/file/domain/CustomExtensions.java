package com.flow.file.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.gson.annotations.Expose;

@Entity
@Table(
	name="tbl_custom_extensions",
	uniqueConstraints={
		@UniqueConstraint(
			columnNames={"name"},
			name = "uq_custom_name"
		)
	}
)
public class CustomExtensions {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@Column
	@Expose
	String name;
	
	public CustomExtensions() {
	}
	
	public CustomExtensions(String name) {
		this.name = name;
	}
	

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
