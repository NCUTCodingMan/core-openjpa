package com.rain.service;

import com.rain.entity.Teacher;

public interface TeacherService {
	public Teacher save(Teacher teacher) throws Exception;
	public <T> Teacher find(T primaryKey) throws Exception;
	public <T> void remove(T primaryKey) throws Exception;
	public Teacher edit(Teacher teacher) throws Exception;
}
