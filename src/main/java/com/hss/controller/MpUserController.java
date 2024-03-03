package com.hss.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hss.domain.MpUser;
import com.hss.service.MpUserService;
import com.hss.util.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hss
 * @since 2020-12-08
 */
@RestController
@RequestMapping("/mpUser")
public class MpUserController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MpUserService mpUserService;

    @RequestMapping(value = "/save")
    public Msg save(@ModelAttribute(value = "mpUser") MpUser mpUser){

        String voitle = voitleSaveMpUserParam(mpUser);
        if(voitle != null){
            logger.info("传参异常");
            return Msg.success().add("errorMessage",voitle);
        }else{
            try{
                logger.info("保存用户信息");
                boolean save = mpUserService.save(mpUser);
                return Msg.success().add("flag",save);
            }catch (Exception e){
                logger.error("信息保存出错："+e.getStackTrace());
                return Msg.fail();
            }
        }
    }

    @RequestMapping(value = "/updateById")
    public Msg updateById(@ModelAttribute(value = "mpUser") MpUser mpUser){

        if(null == mpUser.getId()){
            //修改编号为空，直接抛出异常
            logger.info("传参异常");
            throw new RuntimeException("传参异常");
        }
        String voitle = voitleSaveMpUserParam(mpUser);
        if(voitle != null){
            //其他字段出错，返回message信息，提示用户修正
            logger.info("传参异常");
            return Msg.success().add("errorMessage",voitle);
        }else{
            try {
                logger.info("修改信息");
                boolean update = mpUserService.updateById(mpUser);
                return Msg.success().add("flag",update);
            }catch (Exception e){
                logger.error("修改出错："+e.getStackTrace());
                return Msg.fail();
            }
        }
    }

    @RequestMapping(value = "/updateOpenid")
    public Msg updateOpenId(@RequestParam(value = "id",required=true) Long id,
                            @RequestParam(value = "openid",required = true) String openid){
        if(null == id || null == openid || "".equals(openid)){
            logger.info("传参异常");
            return Msg.success().add("errorMessage","用户编号或openid不能为空");
        }else{
            try {
                UpdateWrapper<MpUser> wrapper = new UpdateWrapper<>();
                wrapper.eq("id",id).set("openid",openid);
                boolean update = mpUserService.update(wrapper);
                return Msg.success().add("flag",update);
            }catch (Exception e){
                logger.info("修改用户openid信息出错:"+e.getStackTrace());
                return Msg.fail();
            }
        }
    }

    @RequestMapping(value = "/selectById")
    @Transactional(rollbackFor = Exception.class)
    public Msg selectById(@RequestParam(value = "id",required = true) Long id){
        try{
            logger.info("查询用户信息by id");
            MpUser mpUser = mpUserService.getById(id);
            try {
                TimeUnit.MINUTES.sleep(3);
            } catch (InterruptedException e) {
                logger.error("线程休眠异常！",e);
            }
            return Msg.success().add("data",mpUser);
        }catch (Exception e){
            logger.error("查询用户信息出错");
            return Msg.fail();
        }
    }

    //MpUser 参数效验
    private String voitleSaveMpUserParam(MpUser mpUser){
        String message = null;
        if(null == mpUser.getUsername() || "".equals(mpUser.getUsername())){
            message = "用户名不能为空!";
            return message;
        }/*else if(null != mpUser.getAddress() && (mpUser.getAddress().length() < 5 || mpUser.getAddress().length() > 50)){
            message = "地址只能是5-50位的字符！";
            return message;
        }*/else if(null == mpUser.getOpenid() || "".equals(mpUser.getOpenid())){
            message = "openid不能为空！";
            return message;
        }
        return null;
    }
}
