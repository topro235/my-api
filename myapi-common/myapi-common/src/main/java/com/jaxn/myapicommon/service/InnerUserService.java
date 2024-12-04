package com.jaxn.myapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jaxn.myapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/lijaxn">程序员鱼皮</a>
 * @from <a href="https://jaxn.icu">编程导航知识星球</a>
 */
public interface InnerUserService extends IService<User> {

    /*
    数据库查是否已经分配秘钥
     */
    User getInvokeUser(String accessKey);

}
