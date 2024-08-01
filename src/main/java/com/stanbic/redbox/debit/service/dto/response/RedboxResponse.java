package com.stanbic.redbox.debit.service.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class RedboxResponse {
    private String responseCode;
    private String responseMsg;
    private Object responseDetails;

//    public RedboxResponse(String responseCode, String responseMsg, Object responseDetails) {
//        this.responseCode = responseCode;
//        this.responseMsg = responseMsg;
//        this.responseDetails = responseDetails;
//    }

//    public  RedboxResponse(){}
}
