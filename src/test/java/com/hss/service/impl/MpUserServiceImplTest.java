package com.hss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hss.domain.MpUser;
import com.hss.service.MpUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MpUserServiceImplTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MpUserService mpUserService;

    @Test
    public void save(){
        MpUser mpUser = new MpUser();
        mpUser.setUsername("hpp");
        mpUser.setAddress("我是地址");
        mpUser.setOpenid(null);
        boolean flag = mpUserService.save(mpUser);
        //保存
        logger.info("flag="+flag);
    }

    @Test
    public void saveBatch(){
        List<MpUser> mpUserList = new ArrayList<MpUser>();
        MpUser mpUser1 = new MpUser();
        mpUser1.setUsername("批量一");
        mpUser1.setAddress("地址一");
        mpUser1.setOpenid("openid");

        MpUser mpUser2 = new MpUser();
        mpUser2.setUsername("批量二");
        mpUser2.setAddress("地址二");
        mpUser2.setOpenid("openid");

        mpUserList.add(mpUser1);
        mpUserList.add(mpUser2);
        boolean flag = mpUserService.saveBatch(mpUserList, mpUserList.size());
        logger.info("flag="+flag);
    }

    @Test
    public void saveOrUpdate(){
        MpUser mpUser = new MpUser();
        mpUser.setId(4L);
        mpUser.setUsername("hpp");
        mpUser.setAddress("我是地址");
        mpUser.setOpenid(null);

        /**
         * 保存或修改
         * 如果不传主键字段，执行新增操作（主键自增）
         * 如果传主键字段，先检查库中是否存在记录
         *   有则是修改
         *   无则是新增（数据库设置主键自增时遵循自增规则）
         * 非主键字段，如果传值不为空才会入库。
         *   传值为空或不传则不会操作该字段
         */
        boolean flag = mpUserService.saveOrUpdate(mpUser);
        logger.info("flag="+flag);
    }

    @Test
    public void count(){
        int count = mpUserService.count();
        logger.info("count="+count);
    }

    @Test
    public void countByParam(){
        QueryWrapper<MpUser> wrapper = new QueryWrapper<>();
        wrapper.eq("openid","openid");
        wrapper.or().isNull("openid");

        int count = mpUserService.count(wrapper);
        logger.info("count="+count);
    }

    @Test
    public void updateById(){
        MpUser mpUser = new MpUser();
        mpUser.setId(4L);
        mpUser.setUsername("hpp");
        mpUser.setAddress("我是地址");
        //mpUser.setOpenid("openid");

        mpUserService.updateById(mpUser);
    }

    @Test
    public void updateByParam(){
        UpdateWrapper<MpUser> wrapper = new UpdateWrapper<>();
        //条件 jdk1.8
        //wrapper.and(wrapper1 ->wrapper1.eq("openid","").or().isNull("openid"));
        //条件 jdk1.6写法
        wrapper.and(new Function<UpdateWrapper<MpUser>, UpdateWrapper<MpUser>>() {
            @Override
            public UpdateWrapper<MpUser> apply(UpdateWrapper<MpUser> mpUserUpdateWrapper) {
                return mpUserUpdateWrapper.eq("openid","").or().isNull("openid");
            }
        });
        wrapper.set("address","我是地址");
        mpUserService.update(wrapper);
    }

    @Test
    public void batchUpdate(){
        MpUser userWhere = new MpUser();
        userWhere.setAddress("我是地址");

        boolean update = mpUserService.update(userWhere,
                Wrappers.<MpUser>update().lambda().eq(MpUser::getUsername, "批量二"));
        logger.info("update={}",update);
    }

    @Test
    public void remove(){
        boolean remove = mpUserService.remove(Wrappers.<MpUser>query().lambda().eq(MpUser::getId, 3L));
        logger.info("remove={}",remove);
    }
}