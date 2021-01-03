package com.backend.filetransfer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.filetransfer.model.FileDB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class FileService {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFsOperations operations;

    public String addFile(String title, MultipartFile file) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("title", title);
        ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData);
        return id.toString();
    }

    public FileDB getFile(String id) throws IllegalStateException, IOException {
        GridFSFile fileRecord = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        FileDB file = new FileDB();
        file.setTitle(fileRecord.getMetadata().get("title").toString());
        file.setStream(operations.getResource(fileRecord).getInputStream());
        file.setId(fileRecord.getId().toString());
        return file;
    }

    public List<FileDB> getAllFiles() throws IllegalStateException, IOException {
    	System.out.println("enter service");
        MongoCursor<GridFSFile> iterator = gridFsTemplate.find(new Query()).iterator();
        List<FileDB> list = new ArrayList<FileDB>();
        while (iterator.hasNext()) {
            GridFSFile fileRecord = iterator.next();
            FileDB file = new FileDB();
            String _id = fileRecord.getId().toString();
            _id = _id.replace("BsonObjectId{value=", "");
            _id = _id.replace("}", "");
            file.setId(_id);
            list.add(file);
        }
        return list;
    }
}
