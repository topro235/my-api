package com.jaxn.myapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jaxn.myapicommon.model.entity.UserInterfaceInfo;


/**
* @author 26971
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-09-02 15:09:30
*/
public interface InnerUserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /*
    调用次数加一
     */
    boolean invokeCount(long userId, long interfaceInfoId);
}
