package blog.client;

import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {

    public static void main(String[] args) {

        System.out.println("Hello, this is Blog Client");
        BlogClient client = new BlogClient();

        client.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        System.out.println("Connected to Channel");
        // Create sync Stub
        BlogServiceGrpc.BlogServiceBlockingStub blogServiceBlockingStub = BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = Blog.newBuilder()
                .setTitle("Blog 2")
                .setContent("Hello, this is second blog")
                .setAuthorId("B")
                .build();

        CreateBlogResponse createBlogResponse = blogServiceBlockingStub.createBlog(CreateBlogRequest.newBuilder()
                .setBlog(blog)
                .build());

        System.out.println("Received create Blog Response from Server: "+ createBlogResponse.toString());

        channel.shutdown();

    }

}
