package com.ltq.timeserver.socket.rpc;

import java.io.Serializable;

public class RpcRequest implements Serializable{
    public String className;
    public String method;
    public Class<?>[] argsType;
    public Object[] args;
}