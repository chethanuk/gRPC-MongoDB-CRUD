package blog.client;

import com.proto.blog.*;
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
                .setTitle("Blog 3")
                .setContent("Hello, this is second blog")
                .setAuthorId("C")
                .build();

        CreateBlogResponse createBlogResponse = blogServiceBlockingStub.createBlog(CreateBlogRequest.newBuilder()
                .setBlog(blog)
                .build());

        System.out.println("Received create Blog Response from Server: " + createBlogResponse.toString());

//
//        // Read the Created Blog
//        // Case 1: Reading existing blog
//        ReadBlogResponse readBlogResponse = blogServiceBlockingStub.readBlog(ReadBlogRequest.newBuilder()
//                .setId(createBlogResponse.getBlog().getId())
//                .build());
//
//        System.out.println("Recieved Read Blog Response: " + readBlogResponse.toString());

        // Case 2: Reading existing blog: Triggered Error (NOTFOUND)
//        ReadBlogResponse readBlogResponse1 = blogServiceBlockingStub.readBlog(ReadBlogRequest.newBuilder()
//                .setId("5c8e8fc75486a17e1b8c57b9")
//                .build());
//
//        System.out.println("Recieved Read Blog Response: " + readBlogResponse1.toString());

        // Create new Blog to update
        Blog newBlog = Blog.newBuilder()
                .setId(createBlogResponse.getBlog().getId())
                .setTitle("Blog 3")
                .setContent("Hello, this is third blog")
                .setAuthorId("D")
                .build();

        System.out.println("Updating a blog");
        UpdateBlogResponse updateBlogResponse =
                blogServiceBlockingStub.updateBlog(UpdateBlogRequest.newBuilder()
                        .setBlog(newBlog)
                        .build());
//        System.out.println("Recieved response from update Request");

        System.out.println("Recieved Update blog: " + updateBlogResponse.toString());

        // Delete the updated Blog
        System.out.println("Deleting Blog..");
        DeleteBlogResponse deleteBlogResponse =
                blogServiceBlockingStub.deleteBlog(DeleteBlogRequest.newBuilder()
                        .setBlogId(updateBlogResponse.getBlog().getId())
                        .build());

        System.out.println("Deleted blog: " + deleteBlogResponse.getBlogId());

        // Check if Blog was deleted successfully
        ReadBlogResponse readBlogResponse1 = blogServiceBlockingStub.readBlog(ReadBlogRequest.newBuilder()
                .setId(updateBlogResponse.getBlog().getId())
                .build());

        System.out.println("Recieved Read Blog Response: " + readBlogResponse1.toString());


        channel.shutdown();

    }

}
