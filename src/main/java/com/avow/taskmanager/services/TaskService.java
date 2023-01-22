package com.avow.taskmanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.avow.taskmanager.models.Task;
import com.avow.taskmanager.repositories.ITaskRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TaskService implements ITaskService {

	@Autowired
	ITaskRepository iTaskRepository;

    public Flux<Task> getAllTask(){
        return  iTaskRepository.findAll();
    }

    public Mono<Task> getTaskById(Long id){
    	return iTaskRepository.findById(id).switchIfEmpty(monoResponseStatusNotFoundException());
    }
	
	public <T> Mono<T> monoResponseStatusNotFoundException(){
		return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
	}
	
    public Mono<Task> add(Task task){
        return iTaskRepository.create(task);
    }

    public Mono<Task> update(Task task){
        return iTaskRepository.update(task);
    }

    public void delete(Long id){
         iTaskRepository.delete(id);
    }
    
    public Mono<Task> completeTask(Long id){
       return iTaskRepository.completeTask(id);
   }
}
