package com.jaxn.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jaxn.myapicommon.model.entity.UserInterfaceInfo;
import com.jaxn.project.common.ErrorCode;
import com.jaxn.project.exception.BusinessException;
import com.jaxn.project.service.UserInterfaceInfoService;
import com.jaxn.project.mapper.UserInterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author 26971
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
 * @createDate 2024-09-02 15:09:30
 */

@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Override
    public void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //
        if (add) {
            if (userInterfaceInfo.getInterfaceInfoId() < 0 || userInterfaceInfo.getUserId() < 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }
        }
        //
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }
    }

    @Override
    public boolean invokeCount(long userId, long interfaceInfoId) {
        //验证参数是否合法
        if (userId < 0 || interfaceInfoId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
        }
        //进行更新操作
        UpdateWrapper<UserInterfaceInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("userId", userId)
                .eq("interfaceInfoId", interfaceInfoId);
        wrapper.setSql("leftNum=leftNum-1,totalNum=totalNum+1");
        return this.update(wrapper);
    }
}


