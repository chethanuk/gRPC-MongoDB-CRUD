package blog.server;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proto.blog.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    // Create a mongo Client: Used in rest of methods to access mongoDB
    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    // Access the DB if exist or else it will create a DB
    private MongoDatabase mongoDatabase = mongoClient.getDatabase("myBlog");

    // bson Document
    // get Collection named "blog
    private MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("blog");

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {

        System.out.println("Received, Create Blog Request");

        // Use MongoDb and create Blog
        Blog blog = request.getBlog();

        Document document = new Document("authorId", blog.getAuthorId())
                .append("title", blog.getTitle())
                .append("content", blog.getContent()
                );

        System.out.println("Inserting Blog to MongoDB");
        // Inserted one Document
        mongoCollection.insertOne(document);

        // get the Id from MongoDb
        String id = document.getObjectId("_id").toString();
        System.out.println("Inserted Blog Doc with id: " + id);

        // Create MongoResponse
        CreateBlogResponse blogResponse = CreateBlogResponse.newBuilder()
                .setBlog(
//                        Blog.newBuilder()
//                        .setAuthorId(blog.getAuthorId())
//                        .setId(blog.getId())
//                        .setContent(blog.getContent())
//                        .setTitle(blog.getTitle())
                        blog.toBuilder().setId(id).build()
                )
                .build();

        responseObserver.onNext(blogResponse);

        responseObserver.onCompleted();

    }

    @Override
    public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {

        System.out.println("Received Read Blog Request");
        // get Blog Id
        String blogId = request.getId();

        Document document = null;
        try {
            // Find collection: Impliment Filters.eq()
            document = mongoCollection.find(eq("_id", new ObjectId(blogId)))
                    .first(); // from the LIST, fetch the first one
        } catch (Exception e) {
//            e.printStackTrace();
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Blog is not found for id:" + blogId)
                    .augmentDescription(e.getLocalizedMessage())
                    .asRuntimeException());
        }


        System.out.println("Searching for doc");
        if (document == null) {
            // Since Doc is not available
            System.out.println("Blog is not found");
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Blog is not found for id:" + blogId)
                    .asRuntimeException()
            );

        } else {
            // Document is found
            System.out.println("Blog is found");
            Blog blog = Blog.newBuilder()
                    .setTitle(document.getString("title"))
                    .setAuthorId(document.getString("authorId"))
//                    .setId(document.getObjectId(""))
                    .setContent(document.getString("content"))
                    .build();

            responseObserver.onNext(
                    ReadBlogResponse.newBuilder()
                            .setBlog(blog)
                            .build());
            System.out.println("Sent the Response");

            responseObserver.onCompleted();
            System.out.println("Server done");
        }

    }
}
