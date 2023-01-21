package com.avow.taskmanager.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

	
	private long id;
	private String title;
	private String description;
	private String status;
	private String priority;
	
}
