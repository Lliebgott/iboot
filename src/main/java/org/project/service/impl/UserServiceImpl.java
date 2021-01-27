package org.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.project.common.attachment.Folders;
import org.project.common.redis.constant.RedisType;
import org.project.common.redis.operator.RedisService;
import org.project.common.security.utils.CurrentUserUtils;
import org.project.common.utils.FileUploads;
import org.project.entity.UserEntity;
import org.project.entity.ViewObject.UserMailVO;
import org.project.entity.ViewObject.UserVO;
import org.project.entity.condition.UserCondition;
import org.project.mapper.RoleMapper;
import org.project.mapper.UserMapper;
import org.project.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private FileUploads fileUploads;

    @Resource
    private RedisService redisService;

    @Override
    public List<UserVO> getPage(UserCondition condition) {
        return userMapper.getPage(condition);
    }

    @Override
    public void addUser(UserEntity userEntity) {
        userMapper.addUser(userEntity);
    }

    @Override
    public UserEntity getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public int updatePwd(UserEntity userEntity) {
        return userMapper.updatePwd(userEntity);
    }

    @Override
    public int updateUserInfo(UserEntity userEntity) {
        return userMapper.updateUserInfo(userEntity);
    }

    @Override
    public int update(UserEntity userEntity) {
        return userMapper.update(userEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public int deleteUser(int id) {
        int user = userMapper.deleteUser(id);
        roleMapper.deleteByUserId(id);
        return user;
    }

    @Override
    public UserEntity getUserByName(String username) {
        return userMapper.getUserByName(username);
    }

    @Override
    public List<Integer> getRoleListById(int id) {
        return userMapper.getRoleListById(id);
    }

    @Override
    public List<UserMailVO> getUserMailList() {
        return userMapper.getUserMailList();
    }

    @Override
    public String uploadavatar(MultipartFile file) {
        String userName = CurrentUserUtils.getCurrentUsername();
        String path = fileUploads.upload(Folders.Avatar, file);
        UserEntity entity = getUserByName(userName);
        entity.setAvatar(path);
        entity.setModifyTime(new Date());


        List<Object> list = new ArrayList<>();
        list.add(entity.getClass());
        list.add(entity);
        System.out.println(redisService.getKey("user::" + userName, "string"));
        redisService.updateByKey("user::" + userName, JSONArray.toJSONString(list), RedisType.STRING);
        System.out.println(redisService.getKey("user::" + userName, "string"));
        return path;
    }
}
