package blog.client;

import blog.server.BlogServiceImpl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class BlogClient {

    public static void main(String[] args) {

        System.out.println("Hello, this is Blog Client");
        BlogClient client = new BlogClient();

        client.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        channel.shutdown();

    }

}
