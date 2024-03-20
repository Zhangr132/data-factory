package com.data.service;

import com.data.dto.CodeValue.AddCodeValueDto;
import com.data.dto.CodeValue.DeleteCodeValueDto;
import com.data.entity.CodeValue;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.utils.R;

/**
 * <p>
 * 码值表 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
public interface CodeValueService extends IService<CodeValue> {

    R addCodeValue(AddCodeValueDto addCodeValueDto);

    boolean deleteCodeValue(DeleteCodeValueDto deleteCodeValueDto);
}
