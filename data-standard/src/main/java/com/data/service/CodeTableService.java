package com.data.service;

import com.data.dto.CodeTable.AddCodeTableDto;
import com.data.dto.CodeTable.CodeTablePageDto;
import com.data.dto.CodeTable.UpdateCodeTableDto;
import com.data.entity.CodeTable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.utils.R;

/**
 * <p>
 * 码表管理 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
public interface CodeTableService extends IService<CodeTable> {

    R selectCodeTable(CodeTablePageDto codeTablePageDto);

    R addCodeTable(AddCodeTableDto addCodeTableDto);

    boolean updateCodeTable(UpdateCodeTableDto updateCodeTableDto);
}
