package com.beta.web.exception;

public class InsufficientMoneyExceptioin extends RuntimeException {
   public InsufficientMoneyExceptioin(){
        super();
    }
    public InsufficientMoneyExceptioin(String message){
        super(message);
    }
}
