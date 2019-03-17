## Java gRPC mongoDB CRUD

- Java gRPC MongoDB:
    - Create a document in mongo Collection
    - Read a document from mongo Collection
    - Update a document in mongo Collection
    - Delete a document from mongo Collection
    - List all documents from mongo Collection using **stream**
    
- Once Reflection is enabled:
    - Run `./evans --cli -p 50051 -r`    
    - Test CRUD and List all
        - Run `show service`
        - Run `call RPCCallName` : example: `call CreateBlog` and test all