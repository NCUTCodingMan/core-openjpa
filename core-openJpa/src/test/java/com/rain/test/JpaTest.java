package com.rain.test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rain.entity.Grade;
import com.rain.entity.Teacher;

public class JpaTest {
	private static String persistenceUnitName;
	private static EntityManagerFactory entityManagerFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		persistenceUnitName = "jpa-demo";
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
	}
	
	@Test
	public void testPersist() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();

		transaction.begin();

		Grade grade = new Grade();
		grade.setGradeName("senior III");

		Teacher teacher = new Teacher();

		teacher.setTeacherName("smi");
		teacher.setTeacherAge(49);
		teacher.setContact("13659851048");
		teacher.setAddress("北京市石景山区晋元庄路5号北方工业大学");
		teacher.setGrade(grade);

		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher);

		grade.setTeachers(teachers);

		entityManager.persist(grade);

		transaction.commit();

		entityManager.close();
	}
	
	@Test
	public void testQuery() throws Exception{
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		
		Grade grade = entityManager.find(Grade.class, 14);
		
		List<Teacher> teachers = grade.getTeachers();
		
		for(Teacher teacher : teachers){
			System.out.println(teacher.getTeacherName());
		}
		
		System.out.println(grade);
		
		
	}
	
	@Test
	public void testRemove() throws Exception{
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		
		Grade grade = entityManager.find(Grade.class, 4);
		
		entityManager.remove(grade);
		
		transaction.commit();

		entityManager.close();
	}
	
	@Test
	public void testGet() throws Exception{
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		
		Grade grade = entityManager.find(Grade.class, 5);
		
		grade.setGradeName("senior 4");
		
		System.out.println(grade.getGradeName());
		
		transaction.commit();
		
		entityManager.close();
	}
	
	@AfterClass
	public static void destory(){
		entityManagerFactory.close();
	}
}