package com.misnearzhang.grpc.provider;

import com.google.gson.Gson;
import com.misnearzhang.grpc.config.annotation.GRpcService;
import com.misnearzhang.pojo.User;
import com.misnearzhang.service.UserService;
import grpcstart.RpcServiceGrpc;
import grpcstart.proto;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 *
 */
@GRpcService
public class OrderServicePrv extends RpcServiceGrpc.RpcServiceImplBase {
    @Autowired
    UserService userService;

    private final Gson gson = new Gson();
    @Override
    public void getUserDate(proto.Request request, StreamObserver<proto.Response> responseObserver) {
        List<User> userList =  userService.getUserInfo();
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
  /*  private int port = 3000;
    private Server server;

    private void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new RpcService())
                .build()
                .start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                OrderServicePrv.this.stop();
            }
        });
    }

    private void stop() {
        if (server != null) {
            //通知zookeeper清除链接信息

            // TODO
            server.shutdown();
        }
    }
    // block 一直到退出程序
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {

        final OrderServicePrv server = new OrderServicePrv();
        server.start();
        server.blockUntilShutdown();
    }*/



