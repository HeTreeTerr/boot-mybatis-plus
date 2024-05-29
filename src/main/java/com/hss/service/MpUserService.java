package com.hss.service;

import com.hss.domain.MpUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hss
 * @since 2020-12-08
 */
public interface MpUserService extends IService<MpUser> {

    MpUser getById(Long id);

    void transactionVerification();
}
