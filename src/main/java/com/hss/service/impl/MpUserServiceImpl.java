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
import java.util.UUID;

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
        /*
        注意：mpUserParam对象中username和address字段值相等
         */
        MpUser mpUserParam = new MpUser("hss","hss","4396");
        //生成readview
        MpUser mpUserOld = mpUserMapper.selectOne(Wrappers.<MpUser>query().lambda()
                .eq(MpUser::getAddress, mpUserParam.getAddress())
                .eq(MpUser::getOpenid, mpUserParam.getOpenid()));
        logger.info("mpUserOld={}",mpUserOld);
        //1、删除
        //查询和删除，字段不对齐会触发修改
        /*Integer remove = mpUserMapper.delete(Wrappers.<MpUser>query().lambda()
                .eq(MpUser::getUsername, mpUserParam.getUsername())
                .eq(MpUser::getOpenid, mpUserParam.getOpenid()));*/
        //查询和删除，字段对齐会触发修改
        Integer remove = mpUserMapper.delete(Wrappers.<MpUser>query().lambda()
                .eq(MpUser::getAddress, mpUserParam.getAddress())
                .eq(MpUser::getOpenid, mpUserParam.getOpenid()));
        logger.info("remove={}",remove);
        //2、查询（关键点在于，可重复度的隔离级别下，是否识别到删除触发readview的重新生成）
        MpUser mpUser = mpUserMapper.selectOne(Wrappers.<MpUser>query().lambda()
                .eq(MpUser::getAddress, mpUserParam.getAddress())
                .eq(MpUser::getOpenid, mpUserParam.getOpenid()));
        logger.info("mpUser={}",mpUser);
        if(Objects.isNull(mpUser)){
            logger.info("======新增");
            mpUserMapper.insert(mpUserParam);
        }else {
            logger.info("=====================修改");
            mpUserParam.setId(mpUser.getId());
            mpUserMapper.updateById(mpUserParam);
        }
    }
}
