package com.avow.taskmanager.repositories;

import org.springframework.stereotype.Repository;

import com.avow.taskmanager.models.Task;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository  
public interface ITaskRepository {
	
	    Flux<Task> findAll();

	    Mono<Task> findById(Long id);
	    
	    Mono<Task> create(Task Task);
	    
	    Mono<Task> update(Task task);
	    
	    Mono<Void> delete(Long id);
	    
	    Mono<Task> completeTask(Long id);


}
