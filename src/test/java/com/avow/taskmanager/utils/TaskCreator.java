package com.avow.taskmanager.utils;

import com.avow.taskmanager.models.Task;

public class TaskCreator {

	public static Task createTask() {
		return Task.builder().id(1).title("task 1").description("task 1 description").status("backlog").priority("p1").build();
	}
	
	public static Task createTaskTobeSave() {
		return Task.builder().id(2).title("task 2").description("task 2 description").status("backlog").priority("p1").build();
	}
	
	public static Task updateTaskTobeSave() {
		return Task.builder().id(2).title("task 2 updated").description("task 2 updated").status("backlog").priority("p1").build();
	}
	
	
	
	
	
}
