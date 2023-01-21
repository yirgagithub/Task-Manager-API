package com.avow.taskmanager.controllers;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.avow.taskmanager.models.Task;
import com.avow.taskmanager.repositories.ITaskRepository;
import com.avow.taskmanager.services.ITaskService;
import com.avow.taskmanager.services.TaskService;
import com.avow.taskmanager.utils.TaskCreator;

import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class TaskControllerTest {

	@InjectMocks
	TaskController taskController;
	
	@Autowired
	ITaskService taskService;
	
	public TaskControllerTest() {
		taskService = Mockito.mock(TaskService.class);
		taskController = Mockito.mock(TaskController.class);
		taskController.iTaskService = taskService;
	}

	private final Task task = TaskCreator.createTask();

	@BeforeAll
	public void blockHoundSetUp() {
		BlockHound.install();
	}

	@Test
	public void blockHoundWorks() {
		try {
			FutureTask<?> task = new FutureTask<>(() -> {
				Thread.sleep(0);
				return "";
			});
			Schedulers.parallel().schedule(task);

			task.get(10, TimeUnit.SECONDS);
			Assertions.fail("should fail");
		} catch (Exception e) {
			Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
		}
	}

	    @Test
	    public void findAll_ReturnFluxOfTask_WhenSuccess() {
	    	Mockito.when(taskService.getAllTask()).thenReturn(Flux.just(task));
	    	StepVerifier.create(taskController.findAllTasks())
	    	.expectSubscription()
	    	.expectNext(task)
	    	.verifyComplete();
	    }
	    
	    @Test
	    public void findTaskById_ReturnMonoTask_WhenSuccess() {
	    	
	    	Mockito.when(taskService.getTaskById(1L)).thenReturn(Mono.just(task));
	    	StepVerifier.create(taskController.findTaskById(1L))
	    	.expectSubscription()
	    	.expectNext(task)
	    	.verifyComplete();
	    }
	    
	    @Test
	    public void findTaskById_ReturnMonoError_WhenEmptyMonoIsReturned() {
	    	Mockito.when(taskService.getTaskById(100L)).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")));
	    	StepVerifier.create(taskController.findTaskById(100L))
	    	.expectError(ResponseStatusException.class)
	    	.verify();
	    }
	    


	    @Test
	    public void addTask_ReturnMonoTask_WhenSuccess() {
	    	
	    	Task taskToBeSaved = TaskCreator.createTaskTobeSave();
	    	Mockito.when(taskService.add(taskToBeSaved)).thenReturn(Mono.just(taskToBeSaved));
	    	StepVerifier.create(taskController.saveTask(taskToBeSaved))
	    	.expectSubscription()
	    	.expectNext(taskToBeSaved)
	    	.verifyComplete();
	    }
	    
	    @Test
	    public void deleteTaskById_ReturnMonEmtpty_WheSuccess() {
	    	 doCallRealMethod().when(taskController).deleteTask(null);
	    	 verify(taskController, times(1)).deleteTask(null);
	    	 //verify(iTaskRepository, times(1)).delete(1L);
	    	
	    }
	    
	    
	    @Test
	    public void updateTask_ReturnMonoTask_WhenSuccess() {
	    	
	    	Task taskToBeSaved = TaskCreator.updateTaskTobeSave();
	    	Mockito.when(taskService.update(taskToBeSaved)).thenReturn(Mono.just(taskToBeSaved));
	    	StepVerifier.create(taskController.updateTask(taskToBeSaved))
	    	.expectSubscription()
	    	.expectNext(taskToBeSaved)
	    	.verifyComplete();
	    }
	    
}
