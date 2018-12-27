package com.bs.social;

/**
 * Created by personal on 10/30/18.
 */

public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    private String lightFileName;

    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size, String lightFileName) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.lightFileName = lightFileName;
    }
// Getters and Setters (Omitted for brevity)
}