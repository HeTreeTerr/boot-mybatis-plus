package com.hss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hss.domain.MpUser;
import com.hss.service.MpUserService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MpUserServiceImplTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MpUserService mpUserService;

    @Test
    public void save(){
        for (int i = 0; i < 1000; i++) {
            MpUser mpUser = new MpUser();
            mpUser.setUsername("hpp" + i);
            mpUser.setAddress("我是地址" + i);
            mpUser.setOpenid(null);
            mpUserService.save(mpUser);
        }
        //保存
        logger.info("success");
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

    @Test
    public void transactionVerification(){
        mpUserService.transactionVerification();
    }

    /**
     * 验证并发下，数据库的事务一致性
     */
    @SneakyThrows
    @Test
    public void jucVerification(){
        //创建一个可重用固定个数的线程池
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 100; i++) {
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        mpUserService.transactionVerification();
                    } catch (Exception e) {
                        logger.error("操作失败",e);
                    }
                }
            });
        }
        TimeUnit.SECONDS.sleep(60);
        fixedThreadPool.shutdown();
    }

    /*
    mysql5.7 查询缓存配置
    -- 查看配置是否启用
    SHOW VARIABLES LIKE 'query_cache%';

    my.cnf配置中
    [mysqld]
    #查询缓存
    query_cache_type=1
     */
}