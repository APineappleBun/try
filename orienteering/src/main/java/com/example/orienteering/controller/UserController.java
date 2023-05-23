package com.example.orienteering.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.orienteering.constant.SystemConstant;
import com.example.orienteering.entity.R;
import com.example.orienteering.entity.User;
import com.example.orienteering.properties.WeixinProperties;
import com.example.orienteering.service.UserService;
import com.example.orienteering.util.HttpClientUtil;
import com.example.orienteering.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户Controller
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private WeixinProperties weixinProperties;

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Autowired
    private UserService userService;

    /**
     * 微信登录
     * @param wxUserInfo 微信用户信息
     * @return resultMap 包含token以及userId
     */
    @RequestMapping("/wxlogin")
    public R wxLogin(@RequestBody User wxUserInfo){
        System.out.println(wxUserInfo);
        System.out.println("code=" + wxUserInfo.getCode());

        // 向微信小程序官方发送请求获取openId
        String jscode2sessionUrl = weixinProperties.getJscode2sessionUrl() + "?appid="+weixinProperties.getAppid() + "&secret="+weixinProperties.getSecret() + "&js_code="+wxUserInfo.getCode() + "&grant_type=authorization_code"; // 参数包含微信小程序的APPId，APPSecret以及微信用户的code
        System.out.println(jscode2sessionUrl);
        String result = httpClientUtil.sendHttpGet(jscode2sessionUrl);
        System.out.println(result); // 请求结果
        JSONObject jsonObject = JSON.parseObject(result); // 解析为Json
        String openid = jsonObject.get("openid").toString(); // 获取openId
//        String session_key = jsonObject.get("session_key").toString(); // 获取session_key

        // 利用openid判断数据库中是否存在相应用户信息（是否新用户）
        User resultUserInfo = userService.getOne(new QueryWrapper<User>().eq("openid", openid)); // 查库
        if(resultUserInfo == null) {
            // 不存在: insert
            System.out.println("Sign up. Insert.");
            wxUserInfo.setOpenid(openid);
            wxUserInfo.setRegisterTime(LocalDateTime.now());
            userService.save(wxUserInfo);
        } else {
            // 存在: update
            System.out.println("Login.");
//            System.out.println("Login. Update.");
            resultUserInfo.setLastLoginTime(LocalDateTime.now());
//            resultUserInfo.setNickname(wxUserInfo.getNickname());
//            resultUserInfo.setAvatarUrl(wxUserInfo.getAvatarUrl());
            userService.updateById(resultUserInfo);
            wxUserInfo.setId(resultUserInfo.getId());
        }

        // 向微信小程序前端返回token以及userId
        String token = JwtUtils.createJWT(openid, wxUserInfo.getNickname(), SystemConstant.JWT_TTL); // 创建json web token
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("token", token); // token
        System.out.println("token" + token);
        Integer userId = Math.toIntExact(wxUserInfo.getId());
        resultMap.put("userId", userId); // userId
        return R.ok(resultMap);
    }


    @RequestMapping ("/my/getUserInfo")
    public R getUserInfo(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
        int userId = (int) json.get("userId");
        System.out.println("userId: " + userId);
        // 权限验证
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            if(claims != null) {
                String openid = claims.getId();
                System.out.println("openId: " + openid);
                if (userId != userService.getOne(new QueryWrapper<User>().eq("openid", openid).last("limit 1")).getId()) {
                    return R.error(500,"无权限访问！");
                }
            } else {
                return R.error(500,"鉴权失败！");
            }
        } else {
            return R.error(500,"无权限访问！");
        }


        User user = userService.getById(userId);
        Map<String, Object> map=new HashMap<>();
        map.put("userInfo", user);
        return R.ok(map);
    }

    @RequestMapping("/my/setPhoneNumber")
    public R setPhoneNumber(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
        int userId = (int) json.get("userId");
        String code = (String) json.get("code");

        // 权限验证
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            if(claims != null) {
                String openid = claims.getId();
                System.out.println("openId: " + openid);
                if (userId != userService.getOne(new QueryWrapper<User>().eq("openid", openid).last("limit 1")).getId()) {
                    return R.error(500,"无权限访问！");
                }
            } else {
                return R.error(500,"鉴权失败！");
            }
        } else {
            return R.error(500,"无权限访问！");
        }

        // 向微信小程序官方发送请求获取access_token
        String getAccessTokenUrl = weixinProperties.getGetAccessTokenUrl() + "?grant_type=client_credential" + "&appid="+weixinProperties.getAppid() + "&secret=" + weixinProperties.getSecret();
        String result = httpClientUtil.sendHttpGet(getAccessTokenUrl);
        System.out.println("result:" + result);
        JSONObject jsonObject = JSON.parseObject(result); // 解析为Json
        String access_token = jsonObject.get("access_token").toString(); // 获取access_token
        System.out.println("access_token:" + access_token);

        // 向微信小程序官方发送请求获取手机号
        String getPhoneNumberUrl = weixinProperties.getGetPhoneNumberUrl() + "?access_token=" + access_token;
//        Map<String, String> postMap = new HashMap<>();
//        postMap.put("code", code); // 请求体加入前端发送过来的code
//        String result2 = httpClientUtil.sendHttpPost(getPhoneNumberUrl, postMap); // 请求结果
        JSONObject postJASONObject = new JSONObject();
        postJASONObject.put("code", code);
        String result2 = httpClientUtil.sendHttpPost(getPhoneNumberUrl, postJASONObject); // 请求结果
        System.out.println("result2: " + result2);
        JSONObject jsonObject2 = JSON.parseObject(result2); // 解析为Json
        JSONObject jsonObject3 = jsonObject2.getJSONObject("phone_info"); // 第二层Json
        String phoneNumber = jsonObject3.get("phoneNumber").toString(); // 获取手机号

        // 设置手机号
        User user = userService.getById(userId);
        user.setPhoneNumber(phoneNumber);
        userService.updateById(user);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "success");
        return R.ok();
    }

    @RequestMapping("/my/setGender")
    public R setGender(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
        int userId = (int) json.get("userId");
        int gender = (int) json.get("gender");

        // 权限验证
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            if(claims != null) {
                String openid = claims.getId();
                System.out.println("openId: " + openid);
                if (userId != userService.getOne(new QueryWrapper<User>().eq("openid", openid).last("limit 1")).getId()) {
                    return R.error(500,"无权限访问！");
                }
            } else {
                return R.error(500,"鉴权失败！");
            }
        } else {
            return R.error(500,"无权限访问！");
        }

        // 设置
        User user = userService.getById(userId);
        user.setGender(gender);
        userService.updateById(user);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "success");
        return R.ok();
    }

    @GetMapping("/my/setNickname")
    public R setNickname(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
        int userId = (int) json.get("userId");
        String nickname = (String) json.get("nickname");

        // 权限验证
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            if(claims != null) {
                String openid = claims.getId();
                System.out.println("openId: " + openid);
                if (userId != userService.getOne(new QueryWrapper<User>().eq("openid", openid).last("limit 1")).getId()) {
                    return R.error(500,"无权限访问！");
                }
            } else {
                return R.error(500,"鉴权失败！");
            }
        } else {
            return R.error(500,"无权限访问！");
        }

        // 设置
        User user = userService.getById(userId);
        user.setNickname(nickname);
        userService.updateById(user);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "success");
        return R.ok();
    }

    @PostMapping(value="/my/setAvatar", produces = {"application/json;charset=UTF-8"})
    public R setAvatar(@RequestParam("file") MultipartFile file, @RequestParam("userId")int userId, @RequestHeader(value = "token") String token) throws IOException {

        // 权限验证
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            if(claims != null) {
                String openid = claims.getId();
                System.out.println("openId: " + openid);
                if (userId != userService.getOne(new QueryWrapper<User>().eq("openid", openid).last("limit 1")).getId()) {
                    return R.error(500,"无权限访问！");
                }
            } else {
                return R.error(500,"鉴权失败！");
            }
        } else {
            return R.error(500,"无权限访问！");
        }

        String fileSpace = System.getProperty("user.dir") + "/myResources"; // 文件保存的命名空间
        String uploadPathDB = "/avatar" + "/" + userId; // 保存到数据库中的相对路径
        FileOutputStream fileOutputStream = null; // 文件输出路径
        InputStream inputStream = null;

        try {
//            if (files != null && files.length > 0) { //有头像上传
                /**
                 * 上传头像默认是一个文件（下面直接取第[0]个），如果是多个就使用for循环以下的整个步骤
                 */
//                String fileName = files[0].getOriginalFilename(); //获取文件的原名称
                String originalFilename = file.getOriginalFilename(); //获取文件的原名称
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String fileName = userId + "_avatar_" + LocalDateTime.now() + extension;
                fileName = fileName.replace(':', '-');
                if (StringUtils.isNotBlank(fileName)) {
                    // 文件上传的最终保存路径
                    //例：C:/imooc_videos_dev/10010/face/*.png
                    String finalAvatarPath = fileSpace + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    //例：/10010/face/*.png
                    uploadPathDB += ("/" + fileName);

                    //打开文件
                    File outFile = new File(finalAvatarPath);
                    //getParentFile()获取抽象路径的父目录的抽象路径名
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs(); //outFile.mkdirs（） 就是*.png也变成了目录
                    }

                    fileOutputStream = new FileOutputStream(outFile);
//                    inputStream = files[0].getInputStream(); //获取文件流
                    inputStream = file.getInputStream(); //获取文件流
                    IOUtils.copy(inputStream, fileOutputStream);
//                }
//
//            } else {
//                Map<String, Object> map = new HashMap<>();
//                map.put("message", "上传出错");
//                return R.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = new HashMap<>();
            map.put("message", "上传出错");
            return R.ok();
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        // 设置
        User user = userService.getById(userId);
        user.setAvatarUrl(uploadPathDB);
        userService.updateById(user);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "success");
        return R.ok();
    }

}
