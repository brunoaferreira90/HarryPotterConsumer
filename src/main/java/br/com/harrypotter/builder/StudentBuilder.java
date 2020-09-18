package br.com.harrypotter.builder;

import org.modelmapper.ModelMapper;

import br.com.harrypotter.dto.StudentDTO;
import br.com.harrypotter.model.Student;

public class StudentBuilder {
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static Student dtoToEntity(StudentDTO dto) {
		
		return modelMapper.map(dto, Student.class);
		
	}
	
	public static StudentDTO entityToDto(Student dto) {
		
		return modelMapper.map(dto, StudentDTO.class);
		
	}

}
