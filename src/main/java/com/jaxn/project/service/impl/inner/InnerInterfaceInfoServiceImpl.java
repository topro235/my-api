package com.jaxn.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jaxn.myapicommon.model.entity.InterfaceInfo;
import com.jaxn.myapicommon.service.InnerInterfaceInfoService;
import com.jaxn.project.common.ErrorCode;
import com.jaxn.project.exception.BusinessException;
import com.jaxn.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @description
 * @date 2024/9/6 16:52:18
 */
@DubboService
public class InnerInterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InnerInterfaceInfoService {
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if (StringUtils.isAnyBlank(path, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url", path)
                .eq("method", method);
        return this.baseMapper.selectOne(wrapper);
    }
}
