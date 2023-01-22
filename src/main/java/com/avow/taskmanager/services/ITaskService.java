package com.avow.taskmanager.services;

import org.springframework.stereotype.Service;

import com.avow.taskmanager.models.Task;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ITaskService {

	 Mono<Task> getTaskById(Long id);

     Flux<Task> getAllTask();


     Mono<Task> add(Task task);

     Mono<Task> update(Task task);

     void delete(Long id);
     
     Mono<Task> completeTask(Long id);
}
