package com.eying.pcss.workflow.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.eying.pcss.core.entity.common.MimeTypeEnum;
import com.eying.pcss.core.util.CommonUtil;
import com.eying.pcss.core.util.OssFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 文件处理service
 */
@Service
public class FileService {
    /**
     * 阿里云API的内或外网域名
     */
    @Value("${oss.endpoint}")
    private String endpoint;
    /**
     * 阿里云API的密钥Access Key ID
     */
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    /**
     * 阿里云API的密钥Access Key Secret
     */
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    /**
     * 存储空间
     */
    @Value("${oss.bucketName}")
    private String bucketName;
    /**
     * 存放头像和logo的bucket
     */
    @Value("${oss.avatarBucketName}")
    private String avatarBucketName;

    /**
     * 将文件从临时目录移到正式目录
     *
     * @param uri      临时文件key值（路径+文件名）
     * @param destPath 正式目录路径
     * @param fileType 文件后缀类型
     * @return 返回正式目录的key（路径+文件名）
     */
    public String copyAttachment(String uri, String destPath,String fileType) {
        // 如果uri是临时访问地址，则需要截取一下
        String url = CommonUtil.subFileUrl(uri);
        OSSClient client = OssFileUtil.getOSSClient(endpoint, accessKeyId, accessKeySecret);
        //处理路径
        destPath = destPath.endsWith("/") ? destPath : destPath + "/";
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        // 文件后缀
        if(StringUtils.isBlank(fileType)){
            OSSClient client1 = OssFileUtil.getOSSClient(endpoint, accessKeyId, accessKeySecret);
            ObjectMetadata metadata = OssFileUtil.getObjectMetadata(client1, bucketName, url);
            fileType = MimeTypeEnum.getExtByCode(metadata.getContentType());
        }else{
            fileType = fileType.startsWith(".") ? fileType : "." + fileType;
        }
        // 正式目录的key
        String destKey = destPath + fileName;
        if(!fileName.contains(".")){
            destKey += fileType;
        }
        OssFileUtil.copyObject(client, bucketName, bucketName, url, destKey);
        return destKey;
    }

    /**
     * 将头像和logo文件从临时目录移到正式目录
     *
     * @param uri      临时文件key值（路径+文件名）
     * @param destPath 正式目录路径
     * @return 返回直接访问头像或logo的直接地址
     */
    public String copyAvatar(String uri, String destPath) {
        // 如果uri是临时访问地址，则需要截取一下
        String url = CommonUtil.subFileUrl(uri);
        //需要将头像移到正式的bucket中
        String host = "https://" + avatarBucketName + "." + endpoint.replace("http://", "").replace("https://", "");
        OSSClient client = OssFileUtil.getOSSClient(endpoint, accessKeyId, accessKeySecret);
        //处理路径
        destPath = destPath.endsWith("/") ? destPath : destPath + "/";
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        // 文件后缀
        OSSClient client1 = OssFileUtil.getOSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata metadata = OssFileUtil.getObjectMetadata(client1, bucketName, url);
        String fileType = MimeTypeEnum.getExtByCode(metadata.getContentType());
        // 正式目录的key
        String destKey = destPath + fileName;
        if(!fileName.contains(".")){
            destKey += fileType;
        }
        OssFileUtil.copyObject(client, bucketName, avatarBucketName, url, destKey);
        return  host + "/" + destKey;
    }
}
