package com.data.service;

import com.data.dto.CodeTable.*;
import com.data.dto.CodeTable.excel.CodeTableExcel;
import com.data.dto.CodeTable.excel.ExportCodeTableExcel;
import com.data.entity.CodeTable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.utils.R;
import org.springframework.core.io.Resource;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    boolean stateCodeTable(StateCodeTableDto stateCodeTableDto);

    boolean deleteCodeTable(DeleteCodeTableDto deleteCodeTableDto);

    boolean batchPublish(List<StateCodeTableDto> stateCodeTableDtos);

    boolean batchStop(List<StateCodeTableDto> stateCodeTableDtos);


    R saveCodeTableExcels(CodeTableExcel newCodeTableExcel);
}
