package com.oracle.project.bridgingservice.repo;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.oracle.project.bridgingservice.endpoint.ServiceEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.SqlOutParameter;
//import org.springframework.jdbc.core.SqlParameter;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.oracle.project.bridgingservice.soap.ws.soap.Bridgingservicerq;

import oracle.jdbc.internal.OracleTypes;
import org.springframework.web.bind.annotation.GetMapping;

@Repository
public class bridgingRepo {
//	@Autowired
//	private JdbcTemplate jdbcTemplate;

	@JacksonInject
	private ServiceEndpoint serviceEndpoint;
	@GetMapping("/bridging-service")
	public void bridge() throws JsonMappingException, JsonProcessingException {
		serviceEndpoint.post();
	}

	public List<Map<String, Object>> getAllBridging(Bridgingservicerq request) {
////		Declare method for execute to db
//		SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName("BRIDGING_GET").declareParameters(
//				new SqlParameter("IN_MSISDN", Types.VARCHAR), //INPUT
//				new SqlParameter("IN_SUBSID", Types.VARCHAR),  //INPUT
//				new SqlParameter("IN_ORDER_TYPE", Types.VARCHAR),  //INPUT
//				new SqlParameter("IN_SUB_TYPE", Types.VARCHAR),  //INPUT
//				new SqlParameter("IN_SYSTEM", Types.VARCHAR),  //INPUT
//				new SqlOutParameter("OUT_DATA", OracleTypes.CURSOR)); //OUTPUT
//
//		//Insert the data input
//		MapSqlParameterSource parameter = new MapSqlParameterSource();
//		parameter.addValue("IN_MSISDN", request.getMsisdn());
//		parameter.addValue("IN_SUBSID", request.getSubsid());
//		parameter.addValue("IN_ORDER_TYPE", request.getOrderType());
//		parameter.addValue("IN_SUB_TYPE", request.getSubType());
//		parameter.addValue("IN_SYSTEM", request.getSystem());
//
//		//Execute to DB
//		List<Map<String, Object>> execute = (List<Map<String, Object>>) call.execute(parameter).get("OUT_DATA");
//
////		System.out.println("repo"+execute);
//		return execute;
		// Insert the data input into a map
		List<Map<String, Object>> result = new ArrayList<>();

		// Insert the data input
		Map<String, Object> data = new HashMap<>();
		data.put("IN_MSISDN", request.getMsisdn());
		data.put("IN_SUBSID", request.getSubsid());
		data.put("IN_ORDER_TYPE", request.getOrderType());
		data.put("IN_SUB_TYPE", request.getSubType());
		data.put("IN_SYSTEM", request.getSystem());
		result.add(data);

		// Declare the desired response data
		Map<String, Object> response1 = new HashMap<>();
		response1.put("TYPE", "w");
		List<Map<String, Object>> execute = new ArrayList<>();
		execute.add(response1); //Add response1 object to the list
		Map<String, Object> response2 = new HashMap<>();
		response2.put("TYPE", "b");
		List<Map<String, Object>> bridgingServiceResponse1 = new ArrayList<>();
		execute.add(response2); // Add response2 object to the list

		Map<String, Object> outputData = new HashMap<>();
		outputData.put("OUT_DATA", execute);

		if (data.get("PREFIX")!= null) {
			System.out.println("w" + execute);
		} else if (data.get("SUBSID")!= null){
			System.out.println("w" + execute);
		}else if (data.get("ORDER_TYPE")!= null) {
			System.out.println("w" + execute);
		}else if (data.get("SYSTEM")!= null) {
			System.out.println("w" + execute);
		}else if (data.get("TYPE")!= null) {
			System.out.println("w" + execute);
		}else if (data.get("V_ID")!= null) {
			System.out.println("w" + execute);
		}else if (data.get("ATTRIBUTES_NAME")!= null) {
			System.out.println("w" + execute);
		}else if (data.get("ATTRIBUTES_VALUE")!= null) {
			System.out.println("w" + execute);
		}else {
			System.out.println("b" + execute);
		}

		return execute;
	}
}
