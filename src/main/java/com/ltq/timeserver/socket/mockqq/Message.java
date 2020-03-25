package com.ltq.timeserver.socket.mockqq;

public class Message {
    public String from="",to="",content="";
    public Message(String msg){
        //a&b&content
        String[] split = msg.split("&");
        if(split.length>=3){
            this.from=split[0];  
            this.to=split[1];  
            this.content=split[2];  
        }

    }
    public String getString(){
      return from+"&"+to+"&"+content;
    }
}
