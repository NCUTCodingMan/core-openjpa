package com.rain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 默认情况下采用Property的方式映射属性信息,在Field(get())上设置注解信息将会报错
 * 	错误的信息主要是不能采用两种混合的方式Access属性
 * */
@Entity
@Table(name = "tab_grade")
public class Grade implements Serializable {
	@Transient
	private static final long serialVersionUID = 3273014492171285838L;
	
	// 主键的生成方式
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "grade_no",length = 10)
	private Integer graderNo;
	
	@Column(name = "grade_name",length = 100,nullable = false)
	private String gradeName;
	
	/**
	 * 关于映射规则
	 * 	(1) 一对一
	 * 		任意选择一方为关系的维护方,关系被维护方表需要增加外键.使用@OneToOne
	 * 	(2) 一对多
	 * 		@OneToMany	@ManyToOne
	 * 		例如 grade -> teacher
	 * 		关系的维护端是一端
	 * 			cascade	级联操作
	 * 				ALL	
	 * 				PERSIST	持久化
	 * 				REFRESH	刷新
	 * 				MERGE	更新
	 * 				REMOVE	级联删除
	 * 			fetch	加载类型,懒加载以及饿加载
	 * 			mappedBy	映射关系,只能在一端维护,多端不能维护
	 * 	(3) 多对多
	 * 		通过@ManyToMany来表示
	 * 			多对多的关系需要设置一张中间表,两个主键作为外键关联
	 * 		
	 * */
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "grade")
	private List<Teacher> teachers = new ArrayList<Teacher>();
	
	public Integer getGraderNo() {
		return graderNo;
	}
	public void setGraderNo(Integer graderNo) {
		this.graderNo = graderNo;
	}
	
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	
	public List<Teacher> getTeachers() {
		return teachers;
	}
	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}
	@Override
	public String toString() {
		return "Grade [graderNo=" + graderNo + ", gradeName=" + gradeName + ", teachers=" + teachers + "]";
	}
}
