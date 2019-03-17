package blog.server;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import io.grpc.stub.StreamObserver;
import org.bson.Document;

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
}
