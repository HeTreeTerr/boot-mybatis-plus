package com.hss.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.domain.MpUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MpUserMapperTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MpUserMapper mpUserMapper;

    @Test
    public void getById(){
        MpUser user = mpUserMapper.selectById(1L);
        logger.info("userInfo:"+user);
    }

    @Test
    public void save(){
        MpUser mpUser = new MpUser();
        mpUser.setUsername("hpp");
        mpUser.setAddress("我是地址");
        mpUser.setOpenid("openid");
        mpUserMapper.insert(mpUser);
        logger.info(mpUser.toString());
    }

    @Test
    public void update(){
        MpUser mpUser = new MpUser();
        mpUser.setId(2L);
        mpUser.setUsername("hpp2");
        mpUser.setAddress("");
//        mpUser.setOpenid("openid1");
        //传那些参数才会修改那些参数
        int updateById = mpUserMapper.updateById(mpUser);
        logger.info("修改编号："+updateById);
    }

    @Test
    public void update1(){
        MpUser mpUser = new MpUser();
        mpUser.setAddress("我是地址2");

        /*UpdateWrapper<MpUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id","2").set("address","我是地址2");
        mpUserMapper.update(null,updateWrapper);*/

        Integer update = mpUserMapper.update(mpUser,
                Wrappers.<MpUser>lambdaUpdate().eq(MpUser::getUsername, "批量二"));
        logger.info("update={}",update);
    }

    @Test
    public void findPage(){
        // 分页查询
        int pageNum = 1;
        int pageSize = 10;
        /*IPage<MpUser> mpUserIPage = mpUserMapper.selectPage(new Page<>(pageNum, pageSize),
                new QueryWrapper<MpUser>().eq("openid", "openid")
                        .orderByDesc("id"));*/
        IPage<MpUser> mpUserIPage = mpUserMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<MpUser>lambdaQuery().eq(MpUser::getOpenid,"openid").orderByDesc(MpUser::getId));
        logger.info("总记录数："+mpUserIPage.getTotal());
        logger.info("记录信息："+mpUserIPage.getRecords());
    }

    @Test
    public void delete(){
        int i = mpUserMapper.deleteById(2L);
        logger.info("删除成功："+i);
    }
}