package com.rain.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.rain.entity.Teacher;
import com.rain.service.TeacherService;

public class TeacherServiceImpl implements TeacherService {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Teacher save(Teacher teacher) throws Exception {
		entityManager.persist(teacher);
		return entityManager.find(Teacher.class, teacher.getTeacherId());
	}

	@Override
	public <T> Teacher find(T primaryKey) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void remove(T primaryKey) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Teacher edit(Teacher teacher) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
