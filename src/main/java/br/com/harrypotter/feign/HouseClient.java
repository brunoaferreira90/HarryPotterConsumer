package br.com.harrypotter.feign;

import java.util.List;
import java.util.Map;

import br.com.harrypotter.dto.HouseResponseDTO;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface HouseClient {
	
	@RequestLine("GET /houses/{houseId}")
	List<HouseResponseDTO> findByHouse(@Param("houseId") String idHouse, @QueryMap Map<String, String> options);

}
