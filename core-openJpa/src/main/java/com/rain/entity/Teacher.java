package com.rain.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.openjpa.persistence.DataCache;

/**
 * 常用的注解与使用方式
 * */
@Entity
@Table(name = "tab_teacher")
@DataCache(timeout = 300000)
public class Teacher implements Serializable {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "teacher_id",length = 10,nullable = false)
	private Integer teacherId;
	
	@Column(name = "teacher_name",length = 100,nullable = false)
	private String teacherName;
	
	@Column(name = "teacher_age",length = 10,nullable = false)
	private Integer teacherAge;
	
	@Column(name = "contact",length = 11,nullable = true)	
	private String contact;
	
	@Column(name = "address",length = 100,nullable = true)
	private String address;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "grade_id")
	private Grade grade;
	
	public Integer getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
	public Integer getTeacherAge() {
		return teacherAge;
	}
	public void setTeacherAge(Integer teacherAge) {
		this.teacherAge = teacherAge;
	}
	
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	@Override
	public String toString() {
		return "Teacher [teacherId=" + teacherId + ", teacherName=" + teacherName + ", teacherAge=" + teacherAge
				+ ", contact=" + contact + ", address=" + address + "]";
	}
}
