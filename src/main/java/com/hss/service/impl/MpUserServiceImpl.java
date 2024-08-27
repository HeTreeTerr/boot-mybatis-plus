package com.hss.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hss.domain.MpUser;
import com.hss.mapper.MpUserMapper;
import com.hss.service.MpUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hss
 * @since 2020-12-08
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MpUserServiceImpl extends ServiceImpl<MpUserMapper, MpUser> implements MpUserService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MpUserMapper mpUserMapper;

    @Override
    public MpUser getById(Long id) {
        return mpUserMapper.selectById(id);
    }

    @Override
    public void transactionVerification() {
        //1、删除
        Integer remove = mpUserMapper.delete(Wrappers.<MpUser>lambdaQuery()
                .eq(MpUser::getUsername, "hpp2")
                .eq(MpUser::getAddress, "我是地址2"));
        logger.info("remove={}",remove);
        //2、查询
        MpUser mpUser = mpUserMapper.selectOne(Wrappers.<MpUser>lambdaQuery()
                .eq(MpUser::getUsername, "hpp2")
                .eq(MpUser::getOpenid, "我是地址2"));
        logger.info("mpUser={}",mpUser);
        if(Objects.isNull(mpUser)){
            logger.info("======新增");
        }else {
            logger.info("======修改");
        }
    }
}
