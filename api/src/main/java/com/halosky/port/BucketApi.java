package com.halosky.port;

import org.springframework.web.bind.annotation.*;

/**
 * packageName com.halosky.port
 *
 * @author huan.yang
 * @className BucketApi
 * @date 2026/3/23
 * @description bucket api
 */
@RestController
@RequestMapping("/bucket")
public class BucketApi {

    @PutMapping("/{bucketName}")
    public void createBucket(@PathVariable String bucketName) {

    }

    @DeleteMapping("/{bucketName}")
    public void deleteBucket(@PathVariable String bucketName) {

    }

    @GetMapping("/bucketName")
    public void getBucket(@PathVariable String bucketName) {

    }
}
