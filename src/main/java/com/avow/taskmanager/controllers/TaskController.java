package com.avow.taskmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.avow.taskmanager.models.Task;
import com.avow.taskmanager.services.ITaskService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/task")
public class TaskController {

	@Autowired
	public ITaskService iTaskService;
	
	
	@GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Flux<Task> findAllTasks(){
        return iTaskService.getAllTask();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Task> findTaskById(@PathVariable Long id){return iTaskService.getTaskById(id);}
    
    @GetMapping("/complete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Task> completeTaskById(@PathVariable Long id){
    	 Task task = iTaskService.getTaskById(id).block();
    	 task.setStatus("completed");
    	 return iTaskService.update(task);
    	 
    	}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Task> saveTask(@RequestBody Task task){ return iTaskService.add(task);}


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Task> updateTask(@RequestBody Task task){return iTaskService.update(task);}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  void deleteTask(@PathVariable Long id){  iTaskService.delete(id);}
	
}
