package com.bs.social;

import com.bs.social.models.Comment;
import com.bs.social.models.ContentSection;
import com.bs.social.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.core.io.FileSystemResource;
import java.util.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(path="/api")
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentSectionRepository contentSectionRepository;

    @Autowired
    private  CommentRepository commentRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(path = "/register_user")
    public @ResponseBody
    ResponseEntity<Map> addNewUser(@RequestBody User user) {
        Map<String, Object> response = new LinkedHashMap<>();
        user.setTimestamp(System.currentTimeMillis());
        User userData = userRepository.findByMobileNumber(user.getMobileNumber());
        if(userData!=null){
            response.put("id", userData.getId());
            response.put("status", "Already Created user");
        }
        else{
            if(user.getMobileNumber()==null){
                return new ResponseEntity<Map>(response, HttpStatus.BAD_REQUEST);
            }
            if(user.getGender()==null){
                user.setGender("Male");
            }
            user.setLightWeightImage("https://1.img-dpreview.com/files/p/TS1200x900~sample_galleries/4465348876/2284269311.jpg");
            user.setUserImageUrl("https://1.img-dpreview.com/files/p/TS1200x900~sample_galleries/4465348876/2284269311.jpg");
            user.setLanguage("English");
            user.setTimestamp(System.currentTimeMillis());
            userRepository.save(user);
            response.put("id", user.getId());
            response.put("status", "New user creatd");
        }
        return new ResponseEntity<Map>(response, HttpStatus.CREATED);
    }


    @PostMapping(path = "/add_content_section")
    public @ResponseBody
    ResponseEntity<Map> addContentSection(@RequestBody ContentSection contentSection) {
        Map<String, Object> response = new LinkedHashMap<>();
        Date date= new Date();
        contentSection.setTimestamp(System.currentTimeMillis());
        contentSectionRepository.save(contentSection);
        response.put("id", contentSection.getId());
        return new ResponseEntity<Map>(response, HttpStatus.CREATED);
    }


    @PostMapping(path = "/add_comments")
    public @ResponseBody
    ResponseEntity<Map> addComments(@RequestBody Comment comment) {
        Map<String, Object> response = new LinkedHashMap<>();
        comment.setTimestamp(System.currentTimeMillis());
        commentRepository.save(comment);
        response.put("id", comment.getId());
        ContentSection contentSection = contentSectionRepository.findById(comment.getContentSectionId());
        contentSection.setNoOfComment(contentSection.getNoOfComment()+1);
        contentSectionRepository.save(contentSection);
        return new ResponseEntity<Map>(response, HttpStatus.CREATED);
    }

    @PostMapping("/upload")
    public @ResponseBody ResponseEntity<Map> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new LinkedHashMap<>();
        System.out.println("welcome");
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(fileName)
                .toUriString();
        String lightFileName = "1540928534077simplefile.jpg";

        if(fileName.endsWith("jpg")){
            lightFileName = fileName;
        }

        response.put("fileName", fileName);
        response.put("fileDownloadUri", fileDownloadUri);
        response.put("size", file.getSize());
        response.put("lightFileName", lightFileName);

        return new ResponseEntity<Map>(response, HttpStatus.OK);
    }


    @GetMapping("/downloadFile/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource

        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @RequestMapping(value = "/get_comments/{someID}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Map> getCommentSectionId(@PathVariable(value = "someID") Long contentSectionId) {

        Map<String, Object> response = new LinkedHashMap<>();
        List<Comment> comments = commentRepository.findByContentSectionIdOrderByTimestampDesc(contentSectionId);
        List<Map<String, Object> > commentList = new ArrayList<>();

        for(Comment comment: comments) {
            Map<String, Object> responseComment = new LinkedHashMap<>();
            responseComment.put("id", comment.getId());
            responseComment.put("contentSectionId", comment.getContentSectionId());
            responseComment.put("text", comment.getText());
            responseComment.put("userId", comment.getUserId());
            responseComment.put("timestamp", comment.getTimestamp());
            User user = userRepository.findById(comment.getUserId());
            responseComment.put("userName", user.getName());
            commentList.add(responseComment);
        }
        response.put("comments", commentList);
        return new ResponseEntity<Map>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/get_user/{someID}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Map> getUserData(@PathVariable(value = "someID") Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        User user = userRepository.findById(id);
        if(user==null){
            return new ResponseEntity<Map>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("name", user.getName());
        response.put("gender", user.getGender());
        response.put("language", user.getLanguage());
        response.put("lightWeightImage", user.getLightWeightImage());
        response.put("mobileNumber", user.getMobileNumber());
        response.put("imageUrl", user.getUserImageUrl());
        return new ResponseEntity<Map>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/increase/{which}/{what}/{contentID}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Map> modifyItems(@PathVariable(value = "which") String which,
                                                    @PathVariable(value = "what") String what,
                                                    @PathVariable(value = "contentID") Long contentID) {
        Map<String, Object> response = new LinkedHashMap<>();
        ContentSection contentSection  = contentSectionRepository.findById(contentID);
        if(which.equals("like")){
            contentSection.setLikeStatus(!contentSection.getLikeStatus());
            if(what.equals("add")){
                contentSection.setNoOfLikes(contentSection.getNoOfLikes()+1);
            }else if(what.equals("del")){
                contentSection.setNoOfLikes(contentSection.getNoOfLikes()-1);
            }
        }else if(which.equals("share")){
            if(what.equals("add")){
                contentSection.setNoOfShares(contentSection.getNoOfShares()+1);
            }else if(what.equals("del")){
                contentSection.setNoOfShares(contentSection.getNoOfShares()-1);
            }
        }
        contentSectionRepository.save(contentSection);

        response.put("id", contentSection.getId());
        response.put("likes", contentSection.getNoOfLikes());
        response.put("shares", contentSection.getNoOfShares());
        response.put("noOfComment", contentSection.getNoOfComment());
        response.put("likeStatus", contentSection.getLikeStatus());

        return new ResponseEntity<Map>(response, HttpStatus.OK);
    }



    @RequestMapping(value = "/get_content_section/{someID}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Map> getContentSectionData(@PathVariable(value = "someID") Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<ContentSection> contentList = contentSectionRepository.findAllByOrderByTimestampDesc();
        List<Map<String, Object> > contentMapList = new ArrayList<>();
        for(ContentSection contentSection: contentList){
            Map<String,Object> contentMap = new LinkedHashMap<>();
            contentMap.put("language",contentSection.getLanguage());
            contentMap.put("title",contentSection.getTitle());
            contentMap.put("noOfComment",contentSection.getNoOfComment());
            contentMap.put("contentType", contentSection.getContentType());
            contentMap.put("user", contentSection.getUserId());
            contentMap.put("likes", contentSection.getNoOfLikes());
            contentMap.put("shares", contentSection.getNoOfShares());
            contentMap.put("timestamp", contentSection.getTimestamp());
            contentMap.put("url",contentSection.getUrl());
            contentMap.put("lightWeightUrl",contentSection.getLightWeightUrl());
            contentMap.put("about", contentSection.getAbout());
            response.put("likeStatus", contentSection.getLikeStatus());
            contentMapList.add(contentMap);
        }
        response.put("contentList",contentList);
        return new ResponseEntity<Map>(response, HttpStatus.OK);
    }

    }