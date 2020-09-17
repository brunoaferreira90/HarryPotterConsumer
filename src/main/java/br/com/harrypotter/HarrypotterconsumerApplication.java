package br.com.harrypotter;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import br.com.harrypotter.dto.StudentDTO;

@EnableKafka
@SpringBootApplication
public class HarrypotterconsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HarrypotterconsumerApplication.class, args);
	}
	
	
	@Bean
	public ConsumerFactory<String, StudentDTO> consumerFactory(){
		
		JsonDeserializer<StudentDTO> deserializer = new JsonDeserializer<>(StudentDTO.class);
	    deserializer.setRemoveTypeHeaders(false);
	    deserializer.addTrustedPackages("*");
	    deserializer.setUseTypeMapperForKey(true);
		
		Map<String, Object> config = new HashMap<>();
	    
	    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	    config.put(ConsumerConfig.GROUP_ID_CONFIG, "group-id");
	    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	    config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
	    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	    
	    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
	    

	    return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
	}
	
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, StudentDTO> kafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, StudentDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory());
	    return factory;
	}

	
}
