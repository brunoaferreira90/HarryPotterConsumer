package br.com.harrypotter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import br.com.harrypotter.builder.StudentBuilder;
import br.com.harrypotter.dto.HouseResponseDTO;
import br.com.harrypotter.dto.StudentDTO;
import br.com.harrypotter.feign.HouseClient;
import br.com.harrypotter.repository.StudentRepository;
import feign.Feign;
import feign.Logger.Level;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

@Component
public class StudentListenerService {
	
	@Value("${potterapi.token}")
	private String token;
	
	@Value("${potterapi.url}")
	private String url;
	
	@Autowired
	private KafkaTemplate<String, StudentDTO> kafkaTemplate;
	
	@Autowired
	private StudentRepository repository;

	@KafkaListener(topics = "NEW_USER", containerFactory = "kafkaListenerContainerFactory")
	public void newStudentListener(StudentDTO dto) {
		
		if(houseExists(dto)) {
		
		ListenableFuture<SendResult<String, StudentDTO>> future = 
			      kafkaTemplate.send("VALIDATED_USER", dto);
				
			    future.addCallback(new ListenableFutureCallback<SendResult<String, StudentDTO>>() {
			 
			        @Override
			        public void onSuccess(SendResult<String, StudentDTO> result) {
			            System.out.println("Sent message=[" + dto + 
			              "] with offset=[" + result.getRecordMetadata().offset() + "]");
			        }
			        @Override
			        public void onFailure(Throwable ex) {
			            System.out.println("Unable to send message=[" 
			              + dto + "] due to : " + ex.getMessage());
			        }
			    });
		} else {
			
			kafkaTemplate.send("NOT_VALIDATED_USER", dto);
			System.out.println("Casa não existe");
		}
		
		System.out.println(dto);
		
	}
	
	private boolean houseExists(StudentDTO dto) {
		
		try {
		
		HouseClient houseClient = Feign.builder()
				  .client(new OkHttpClient())
				  .encoder(new GsonEncoder())
				  .decoder(new GsonDecoder())
				  .logger(new Slf4jLogger(HouseClient.class))
				  .logLevel(Level.FULL)
				  .target(HouseClient.class, url);
		
		Map<String, String> urlKey = new HashMap<>();
		urlKey.put("key", token);
		
		List<HouseResponseDTO> findByHouse = houseClient.findByHouse(dto.getHouse(), urlKey);
		
		return !findByHouse.isEmpty();
		
		} catch (Exception e) {
			System.out.println("Casa não encontrada");
			return false;
		}
		
	}
	
	@KafkaListener(topics = "VALIDATED_USER", containerFactory = "kafkaListenerContainerFactory")
	public void validatedUser(StudentDTO dto) {
		
		if(repository.findByName(dto.getName()).isEmpty()) {
			repository.save(StudentBuilder.dtoToEntity(dto));
		} else {
			kafkaTemplate.send("USER_ALREADY_EXISTS", dto);
		}
		
	}

}
