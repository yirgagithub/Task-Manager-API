package com.avow.taskmanager.repositories;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.avow.taskmanager.models.Task;
import com.avow.taskmanager.utils.TaskStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TaskRepository implements ITaskRepository {

	    private static final Map<Long, Task> DATA = new HashMap<>();
	    private static long ID_COUNTER = 1L;

	    static {
	        Arrays.asList("First Task", "Second Task")
	            .stream()
	            .forEach((java.lang.String title) -> {
	                long id = ID_COUNTER++;
	                Task task = Task.builder().id(id).title(title).description(title +" description").status("backlog").priority("p1").build();
	                DATA.put(Long.valueOf(id), task);
	            }
	            );
	    }

	    @Override
		public Flux<Task> findAll() {
	        return Flux.fromIterable(DATA.values());
	    }

	    @Override
	    public Mono<Task> findById(Long id) {
	    	Task task = DATA.get(id);
	        return task != null ? Mono.just(task) : Mono.empty();
	    }

	    @Override
	    public Mono<Task> create(Task Task) {
	        long id = ID_COUNTER++;
	        Task.setId(id);
	        DATA.put(id, Task);
	        return Mono.just(Task);
	    }

	    @Override
	    public Mono<Task> update(Task task) {
	        DATA.put(task.getId(), task);
	        return Mono.just(task);
	    }
	    
	    @Override
	    public  Mono<Void> delete(Long id) {
	    	Mono<Task> value = Mono.just(DATA.remove(id));
	       return value.then();
	    }
	    
	    public Mono<Task> completeTask(Long id){
	        Task task = DATA.get(id);
	        task.setStatus(TaskStatus.completed.name());
	        DATA.put(id, task);
	        return Mono.just(task);
	   }

		

}
