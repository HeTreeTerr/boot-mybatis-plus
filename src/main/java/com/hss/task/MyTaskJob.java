package com.hss.task;

import com.hss.domain.MpUser;
import com.hss.service.MpUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author Hss
 * @date 2024-03-03
 */
@Service
public class MyTaskJob {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MpUserService mpUserService;

    @Scheduled(cron = "0 0/1 * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void myJob01(){
        logger.info("==========MyTaskJob.myJob01 is begin...");
        MpUser mpUser = mpUserService.getById(1);
        try {
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            logger.error("线程休眠异常！",e);
        }
        logger.info("==========MyTaskJob.myJob01 is end...;mpUser={}",mpUser);
    }
}
