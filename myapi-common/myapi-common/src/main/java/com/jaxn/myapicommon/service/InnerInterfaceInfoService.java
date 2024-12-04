package com.jaxn.myapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jaxn.myapicommon.model.entity.InterfaceInfo;

/**
* @author 26971
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2024-08-29 16:34:17
*/
public interface InnerInterfaceInfoService extends IService<InterfaceInfo> {

    /*
    查询接口是否存在
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

}
