package com.avow.taskmanager.integration;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.avow.taskmanager.controllers.TaskController;
import com.avow.taskmanager.models.Task;
import com.avow.taskmanager.repositories.ITaskRepository;
import com.avow.taskmanager.services.ITaskService;
import com.avow.taskmanager.services.TaskService;
import com.avow.taskmanager.utils.TaskCreator;

import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Import(ITaskService.class)
@WebFluxTest
@SpringBootTest
public class TaskControllerTestIntegration {

	@MockBean
	private ITaskRepository iTaskRepository;
	
	@Autowired
	private ITaskService iTaskService;
	
	@Autowired
	private WebTestClient testClient;
	
	@Autowired
	private TaskController iTaskController;
	
	private final Task task = TaskCreator.createTaskTobeSave();
	
	public TaskControllerTestIntegration() {
		iTaskRepository = Mockito.mock(ITaskRepository.class);
		iTaskService = Mockito.mock(ITaskService.class);
		iTaskController = Mockito.mock(TaskController.class);
	}
	
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
		   Mockito.when(iTaskRepository.findAll()).thenReturn(Flux.just(task));
		   WebTestClient.bindToController(iTaskController)
		      .build()
	    		.get()
	    		.uri("/api/task")
	    		.exchange()
	    		.expectStatus().isOk()
	    		.expectBody();
	    }
	
	
	
}
