package com.avow.taskmanager.services;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import com.avow.taskmanager.models.Task;
import com.avow.taskmanager.repositories.ITaskRepository;
import com.avow.taskmanager.utils.TaskCreator;

import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class TaskServiceTest {

	@InjectMocks
	TaskService taskService;
	
	@Autowired
	ITaskRepository iTaskRepository;
	
	public TaskServiceTest() {
		taskService = Mockito.mock(TaskService.class);
		iTaskRepository = Mockito.mock(ITaskRepository.class);
		taskService.iTaskRepository = iTaskRepository;
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
	    	Mockito.when(iTaskRepository.findAll()).thenReturn(Flux.just(task));
	    	Mockito.when(taskService.getAllTask()).thenCallRealMethod();
	    	StepVerifier.create(taskService.getAllTask())
	    	.expectSubscription()
	    	.expectNext(task)
	    	.verifyComplete();
	    }
	    
	    @Test
	    public void findTaskById_ReturnMonoTask_WhenSuccess() {
	    	
	    	Mockito.when(taskService.getTaskById(1L)).thenReturn(Mono.just(task));
	    	StepVerifier.create(taskService.getTaskById(1L))
	    	.expectSubscription()
	    	.expectNext(task)
	    	.verifyComplete();
	    }
	    
	    @Test
	    public void findTaskById_ReturnMonoError_WhenEmptyMonoIsReturned() {
	    	Mockito.when(taskService.getTaskById(100L)).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")));
	    	StepVerifier.create(taskService.getTaskById(100L))
	    	.expectError(ResponseStatusException.class)
	    	.verify();
	    }
	    


	    @Test
	    public void addTask_ReturnMonoTask_WhenSuccess() {
	    	
	    	Task taskToBeSaved = TaskCreator.createTaskTobeSave();
	    	Mockito.when(iTaskRepository.create(taskToBeSaved)).thenReturn(Mono.just(taskToBeSaved));
	    	Mockito.when(taskService.add(taskToBeSaved)).thenCallRealMethod();
	    	StepVerifier.create(taskService.add(taskToBeSaved))
	    	.expectSubscription()
	    	.expectNext(taskToBeSaved)
	    	.verifyComplete();
	    }
	    
	    @Test
	    public void deleteTaskById_ReturnMonEmtpty_WheSuccess() {
	    	 doCallRealMethod().when(taskService).delete(1L);
	    	 verify(taskService, times(1)).delete(1L);
	    	 //verify(iTaskRepository, times(1)).delete(1L);
	    	
	    }
	    
	    
	    @Test
	    public void updateTask_ReturnMonoTask_WhenSuccess() {
	    	
	    	Task taskToBeSaved = TaskCreator.updateTaskTobeSave();
	    	Mockito.when(iTaskRepository.update(taskToBeSaved)).thenReturn(Mono.just(taskToBeSaved));
	    	Mockito.when(taskService.update(taskToBeSaved)).thenCallRealMethod();
	    	StepVerifier.create(taskService.update(taskToBeSaved))
	    	.expectSubscription()
	    	.expectNext(taskToBeSaved)
	    	.verifyComplete();
	    }
	    
}
