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

import javax.servlet.http.HttpServletRequest;
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

    R selectCodeTable(CodeTablePageDto codeTablePageDto, HttpServletRequest request);

    R addCodeTable(AddCodeTableDto addCodeTableDto, HttpServletRequest request);

    boolean updateCodeTable(UpdateCodeTableDto updateCodeTableDto, HttpServletRequest request);

    boolean stateCodeTable(StateCodeTableDto stateCodeTableDto, HttpServletRequest request);

    boolean deleteCodeTable(DeleteCodeTableDto deleteCodeTableDto, HttpServletRequest request);

    boolean batchPublishCodeTable(List<DeleteCodeTableDto> deleteCodeTableDtos, HttpServletRequest request);

    boolean batchStopCodeTable(List<DeleteCodeTableDto> deleteCodeTableDtos, HttpServletRequest request);


    R saveCodeTableExcels(CodeTableExcel newCodeTableExcel);

}
