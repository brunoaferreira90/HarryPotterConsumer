package br.com.harrypotter.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import br.com.harrypotter.dto.StudentDTO;

@Component
public class StudentListenerService {

	@KafkaListener(topics = "NEW_USER", containerFactory = "kafkaListenerContainerFactory")
	public void newStudentListener(StudentDTO message) {
		
		System.out.println(message);
		
	}

}
