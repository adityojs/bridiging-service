package com.oracle.project.bridgingservice.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import com.oracle.project.bridgingservice.data.bridgingServiceResponseData;
import com.oracle.project.bridgingservice.repo.bridgingRepo;
import com.oracle.project.bridgingservice.soap.ws.soap.AuthenticationHeader;
import com.oracle.project.bridgingservice.soap.ws.soap.Bridgingservicerq;
import com.oracle.project.bridgingservice.soap.ws.soap.Bridgingservicers;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

@Endpoint
@Service
public class ServiceEndpoint {

	@JacksonInject
	RestTemplate restTemplate;
	@Value("${rest.post.url}")
	private String url;
	public void post() {
		String responseEntity = restTemplate.postForObject(url, null, String.class);
//		ResponseEntity<String> response = restTemplate.getForObject(url, List<BridgingResponse>, Map.of("id", "1"));
		System.out.println("[POST] - INFO : Success to send REST API");
		System.out.println(responseEntity);
	}
	
	@Autowired
	private bridgingRepo bridgingRepoRequest;
	
	private static final String NAMESPACE_URI = "http://www.telkomsel.co.id/ufo/services/bridging-service/request/v1.0";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "bridgingservicerq")
	@ResponsePayload //annotation makes Spring WS map the returned value to the response payload.
	public Bridgingservicers Service(@RequestPayload JAXBElement<Bridgingservicerq> req, @SoapHeader("{http://www.oracle.com}AuthenticationHeader") SoapHeaderElement auth) {
			
		//Make new object for return
		Bridgingservicers response = new Bridgingservicers();
		
		//Unpack request object from jaxbelement
		Bridgingservicerq request = req.getValue();
		
		//Get the header value
		AuthenticationHeader header = this.getAuthentication(auth);
		
		if("osb".equalsIgnoreCase(header.getUsername()) && "welcome1".equalsIgnoreCase(header.getPassword())) {
			
			//Set the variable value for object responfact
			List<bridgingServiceResponseData> bridgingServiceResponse = getAllBridgingServiceResponse(request);

			if("w".equalsIgnoreCase(bridgingServiceResponse.get(0).getType()))
			{
				response.setStatus(true);
				response.setErrorCode("0000");
				response.setErrorMessage("Success");
				response.setTrxId(request.getTrxId());
			}
			else if ("b".equalsIgnoreCase(bridgingServiceResponse.get(0).getType())) 
			{
				response.setStatus(false);
				response.setErrorCode("0000");
				response.setErrorMessage("Success");
				response.setTrxId(request.getTrxId());
			}
			else
			{
				response.setStatus(false);
				response.setErrorCode("0000");
				response.setErrorMessage("Success");
				response.setTrxId(request.getTrxId());
			}
			
		}else {
			//Set the variable value for object responfact
			response.setErrorCode("400");
			response.setErrorMessage("Wrong Username & Password");
			response.setTrxId(request.getTrxId());
		}
		return response;
//		return createJaxbElement(response, Bridgingservicers.class);
	}
	
	// set method for response list value from DB
	public List<bridgingServiceResponseData> getAllBridgingServiceResponse(Bridgingservicerq request) {
		List<Map<String, Object>> dataBridging = bridgingRepoRequest.getAllBridging(request);
		List<bridgingServiceResponseData> response = new ArrayList<>();
		
		
		if(dataBridging.size() > 0) {
			
			for(Map<String, Object> data : dataBridging) {

				bridgingServiceResponseData rs = new bridgingServiceResponseData();
				if (data.get("PREFIX")!= null) {
					rs.setPrefix(data.get("PREFIX").toString());	
				}
				if (data.get("SUBSID")!= null) {
					rs.setSubsId(data.get("SUBSID").toString());	
				}
				if (data.get("ORDER_TYPE")!= null) {
					rs.setOrderType(data.get("ORDER_TYPE").toString());	
				}
				if (data.get("SYSTEM")!= null) {
					rs.setSystem(data.get("SYSTEM").toString());	
				}

				if (data.get("TYPE")!= null) {
					rs.setType(data.get("TYPE").toString());	
				}
				if (data.get("V_ID")!= null) {
					rs.setVid(data.get("V_ID").toString());	
				}
				if (data.get("ATTRIBUTES_NAME")!= null) {
					rs.setAttributesNames(data.get("ATTRIBUTES_NAME").toString());	
				}
				if (data.get("ATTRIBUTES_VALUE")!= null) {
					rs.setAttributesValue(data.get("ATTRIBUTES_VALUE").toString());	
				}
	            
	            response.add(rs);

			}

		}
		
		return response;
	}
	
	private AuthenticationHeader getAuthentication(SoapHeaderElement header){
		AuthenticationHeader authentication = null;
        try {

            JAXBContext context = JAXBContext.newInstance(AuthenticationHeader.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            authentication = (AuthenticationHeader) unmarshaller.unmarshal(header.getSource());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return authentication;
    }
	
	private <T> JAXBElement<T> createJaxbElement(T object, Class<T> clazz) {
	    return new JAXBElement<>(new QName(clazz.getSimpleName()), clazz, object);
	}
	
}
