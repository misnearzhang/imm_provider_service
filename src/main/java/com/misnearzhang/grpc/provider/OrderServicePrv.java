package com.misnearzhang.grpc.provider;

import com.google.gson.Gson;
import com.misnearzhang.common.grpcdl.RpcServiceGrpc;
import com.misnearzhang.common.grpcdl.proto;
import com.misnearzhang.grpc.config.annotation.GRpcService;
import com.misnearzhang.pojo.User;
import com.misnearzhang.service.UserService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Hello world!
 */
@GRpcService
public class OrderServicePrv extends RpcServiceGrpc.RpcServiceImplBase {
    @Autowired
    UserService userService;

    private final Gson gson = new Gson();

    @Override
    public void getUserDate(proto.Request request, StreamObserver<proto.Response> responseObserver) {
        List<User> userList = userService.getUserInfo();
        proto.Response response = proto.Response.newBuilder().setId(1000).setStatus(proto.status.OK).setData(gson.toJson(userList)).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void makeOrder(proto.Request request, StreamObserver<proto.Response> responseObserver) {
        System.out.println(request.getData());
        proto.Response response = proto.Response.newBuilder().setId(2000).setStatus(proto.status.ERROR).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}



