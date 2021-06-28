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
	name="tbl_fixed_extensions",
	uniqueConstraints={
		@UniqueConstraint(
			columnNames={"name"},
			name = "uq_fixed_name"
		)
	}
)
public class FixedExtensions {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	
	@Column
	@Expose
	String name;
	
	@Column
	@Expose
	boolean use_yn;
	
	public FixedExtensions() {
		
	}
	
	public FixedExtensions(String name , boolean use_yn) {
		this.name		= name;
		this.use_yn 	= use_yn;
	}

	public Integer getId() {
		return id;
	}
	
	public boolean getUse_yn() {
		return use_yn;
	}

	public String getName() {
		return name;
	}
	
	public void changeUse_yn( boolean use_yn ) {
		this.use_yn 	=  use_yn;
	}
	
}
