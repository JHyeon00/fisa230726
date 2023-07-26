package com.fisa.model.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity	(name = "fisadept")
public class DeptCopy {	//dept_copy
	@Id
	private int deptno;	//dept_no
	private String dname;
	private String loc;
	
}
