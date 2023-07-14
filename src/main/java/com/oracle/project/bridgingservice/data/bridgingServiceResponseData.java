package com.oracle.project.bridgingservice.data;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class bridgingServiceResponseData {
	private String Prefix;
    private String SubsId;
    private String OrderType;
    private String AttributesNames;
    private String AttributesValue;
    private String System;
    private String Type;
    private String Vid;
}
