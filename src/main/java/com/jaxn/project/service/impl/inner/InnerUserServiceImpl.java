package com.jaxn.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jaxn.myapicommon.model.entity.User;
import com.jaxn.myapicommon.service.InnerUserService;
import com.jaxn.project.common.ErrorCode;
import com.jaxn.project.exception.BusinessException;
import com.jaxn.project.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @description
 * @date 2024/9/6 16:49:47
 */
@DubboService
public class InnerUserServiceImpl extends ServiceImpl<UserMapper, User>
        implements InnerUserService {

    @Override
    public User getInvokeUser(String accessKey) {
        if(StringUtils.isAnyBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("accessKey",accessKey);
        return this.baseMapper.selectOne(wrapper);
    }
}
