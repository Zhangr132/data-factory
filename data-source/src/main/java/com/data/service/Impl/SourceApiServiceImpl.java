package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.dto.SourceApi.*;
import com.data.entity.SourceApi;
import com.data.mapper.CategoryInfoMapper;
import com.data.mapper.SourceApiMapper;
import com.data.service.SourceApiService;
import com.data.utils.JacksonUtil;
import com.data.utils.R;
import com.data.vo.CategoryInfoVo;
import com.data.vo.SourceApiVo;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 接口数据源 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-04-02
 */
@Slf4j
@Service
public class SourceApiServiceImpl extends ServiceImpl<SourceApiMapper, SourceApi> implements SourceApiService {
    @Autowired
    private SourceApiMapper sourceApiMapper;

    @Autowired
    private CategoryInfoMapper categoryInfoMapper;


    /**
     * 接口数据源查询
     * @param sourceApiPageDto
     * @return
     */
    @Override
    public R selectSourceApi(SourceApiPageDto sourceApiPageDto) {
        log.info("正在处理接口数据源查询请求");

        MPJLambdaWrapper<SourceApi> queryWrapper=new MPJLambdaWrapper<>();
        //将pageSize和pageNumber放入Page中
        Page<SourceApi> page=new Page<>(sourceApiPageDto.getPageNumber(),sourceApiPageDto.getPageSize());
        queryWrapper
                .select(SourceApi::getId,SourceApi::getApiCode,SourceApi::getApiName,SourceApi::getApiDesc,SourceApi::getApiCategoryCode,SourceApi::getApiOrigin,SourceApi::getApiState,SourceApi::getUpdateTime)
                .like(!ObjectUtils.isEmpty(sourceApiPageDto.getApiName()),SourceApi::getApiName,sourceApiPageDto.getApiName())
                .eq(!ObjectUtils.isEmpty(sourceApiPageDto.getApiState()),SourceApi::getApiState,sourceApiPageDto.getApiState())
                .eq(!ObjectUtils.isEmpty(sourceApiPageDto.getApiOrigin()),SourceApi::getApiOrigin,sourceApiPageDto.getApiOrigin())
                .orderByAsc(SourceApi::getApiState);
        queryWrapper.orderByDesc(SourceApi::getUpdateTime);

        IPage<SourceApi> sourceApiIPage=sourceApiMapper.selectPage(page,queryWrapper);

        List<SourceApiVo> sourceApiVoList=new ArrayList<>();
        List<SourceApi> records=sourceApiIPage.getRecords();

        records.forEach(
                sourceApi->{
                    //输出给前端的数据
                    SourceApiVo sourceApiVo = new SourceApiVo();
                    //将一个对象（sourceApi）的属性复制到另一个对象（sourceApiVo）
                    BeanUtils.copyProperties(sourceApi, sourceApiVo);
                    //通过调用 categoryInfoMapper 里的方法查询出分类名称和父名称
                    CategoryInfoVo categoryInfoVo=categoryInfoMapper.selectCNameCode(sourceApiVo.getApiCategoryCode());
                    //将查出的分类名称和父名称传给 sourceApiVo
                    sourceApiVo.setParentCategoryName(categoryInfoVo.getParentCategoryName());
                    sourceApiVo.setCategoryName(categoryInfoVo.getCategoryName());

                    //将单个数据存入 List
                    sourceApiVoList.add(sourceApiVo);
                }
        );

        Map responseData=new HashMap<>();
        responseData.put("data", sourceApiVoList);
        System.out.println("responseData："+responseData);
        responseData.put("total", sourceApiIPage.getTotal()); // 总记录数
        responseData.put("pageSize", sourceApiIPage.getSize()); // 每页显示数量
        responseData.put("pageNumber", sourceApiIPage.getCurrent()); // 当前页码
//        responseData.put("orders", sourceApiIPage.orders()); // 排序信息
//        responseData.put("optimizeCountSql", sourceApiIPage.optimizeCountSql()); // 是否优化count语句
        responseData.put("pages", sourceApiIPage.getPages()); // 总页数
        log.info("处理接口数据源查询请求结束");
        return R.Success(responseData);
    }

    /**
     * 接口数据源新增
     * @param addSourceApiDto
     * @return
     */
    @Transactional
    @Override
    public R addSourceApi(AddSourceApiDto addSourceApiDto/*, RequestParamsDto requestParamsDto, RequestBodyDto requestBodyDto, ResponseDto responseDto*/) {
        log.info("正在处理数据接口源新增请求");

        String requestParams=null;   //输入参数
        String requestBody=null;     //请求Body
        String response=null;        //返回参数

        try {
            //判断接口名称是否重复
            //构建查询条件
            MPJLambdaWrapper<SourceApi> lambdaQueryWrapperName = new MPJLambdaWrapper<>();
            //输入查询条件——名称判空
            lambdaQueryWrapperName
                    .selectAll(SourceApi.class)
                    .eq(SourceApi::getApiName,addSourceApiDto.getApiName())
                    .eq(SourceApi::getDeleteFlag,0);
            log.info("检验接口名称是否已存在");
            SourceApi sourceApiName = sourceApiMapper.selectOne(lambdaQueryWrapperName);
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(sourceApiName)){
                return R.BAD_REQUEST("该名称已存在");
            }

            //输入查询条件——Path判空
            MPJLambdaWrapper<SourceApi> lambdaQueryWrapperPath = new MPJLambdaWrapper<>();
            lambdaQueryWrapperPath
                    .selectAll(SourceApi.class)
                    .eq(SourceApi::getApiPath,addSourceApiDto.getApiPath())
                    .eq(SourceApi::getDeleteFlag,0);
            SourceApi sourceApiPath = sourceApiMapper.selectOne(lambdaQueryWrapperPath);
            log.info("检查Path路径是否重复");
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(sourceApiPath)){
                return R.BAD_REQUEST("该Path路径已存在");
            }

            //生成编号
            String API="API";       //前缀
            String newApiCode;
            MPJLambdaWrapper<SourceApi> lambdaQueryWrapper1 = new MPJLambdaWrapper<>();
            //查询符合条件的最后一条数据
            lambdaQueryWrapper1
                    .selectAll(SourceApi.class)
                    .orderByDesc(SourceApi::getApiCode)
                    .last("limit 1");
            log.info("查询符合条件的最后一条数据");
            SourceApi sourceApi = sourceApiMapper.selectOne(lambdaQueryWrapper1);

            if (ObjectUtils.isEmpty(sourceApi)) {
                //如果数据库没有 0001
                newApiCode =API + "00001";
            }else {
                //如果数据库中有数据 拿最后一条数据的序号
                //最后一条数据账号
                String lastApiCode = sourceApi.getApiCode();
                //截取序号部分，并将其转换为整数
                String idStr = lastApiCode.substring(3, lastApiCode.length());
                //将序号加一，并格式化为五位数的字符串
                Integer id = Integer.valueOf(idStr) + 1;
                String formatId = String.format("%05d", id);
                //拼接成新的 newApiCode，并将其设置到 addSourceApiDto 对象中
                newApiCode = API + (formatId);
            }

            //将 Java 对象转换为 JSON 字符串
            requestParams= JacksonUtil.bean2Json(addSourceApiDto.getApiRequestParamsList());
            requestBody=JacksonUtil.bean2Json(addSourceApiDto.getApiRequestBodyList());
            response=JacksonUtil.bean2Json(addSourceApiDto.getApiResponseList());

            //存入数据到 SourceApi
            SourceApi sourceApi1 = SourceApi.builder()
                    .apiCode(newApiCode)
                    .apiName(addSourceApiDto.getApiName())
                    .apiCategoryCode(addSourceApiDto.getApiCategoryCode())
                    .apiDesc(addSourceApiDto.getApiDesc())
                    .apiOrigin(addSourceApiDto.getApiOrigin())
                    .apiPath(addSourceApiDto.getApiPath())
                    .apiPort(addSourceApiDto.getApiPort())
                    .apiProtocol(addSourceApiDto.getApiProtocol())
                    .apiTimeoutTime(addSourceApiDto.getApiTimeoutTime())
                    .apiRequestMethod(addSourceApiDto.getApiRequestMethod())
                    .apiRequestParams(requestParams)
                    .apiRequestBody(requestBody)
                    .apiResponse(response)
                    .build();
            log.info("构建 SourceApi 对象");

            //进行新增数据接口源操作
            sourceApiMapper.insert(sourceApi1);

            System.out.println("返回新增的数据");
            return R.Success(sourceApiMapper.selectOne(lambdaQueryWrapperName));
        } catch (Exception  e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return R.Failed("新增数据接口源数据异常");
    }

    /**
     * 接口数据源更新
     * @param updateSourceApiDto
     * @return
     */
    @Override
    public R updateSourceApi(UpdateSourceApiDto updateSourceApiDto) {
        log.info("正在处理数据接口源更新请求");
        String requestParams=null;   //输入参数
        String requestBody=null;     //请求Body
        String response=null;        //返回参数

        //将 Java 对象转换为 JSON 字符串
        requestParams= JacksonUtil.bean2Json(updateSourceApiDto.getApiRequestParamsList());
        requestBody=JacksonUtil.bean2Json(updateSourceApiDto.getApiRequestBodyList());
        response=JacksonUtil.bean2Json(updateSourceApiDto.getApiResponseList());

        //查询数据是否存在
        MPJLambdaWrapper<SourceApi> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper
                .selectAll(SourceApi.class)
                .eq(SourceApi::getApiCode,updateSourceApiDto.getApiCode());
        SourceApi sourceApi = sourceApiMapper.selectOne(lambdaQueryWrapper);

        if (sourceApi!=null){
            //构建更新对象
            SourceApi sourceApi1=SourceApi.builder()
                    .apiName(updateSourceApiDto.getApiName())
                    .apiCategoryCode(updateSourceApiDto.getApiCategoryCode())
                    .apiDesc(updateSourceApiDto.getApiDesc())
                    .apiOrigin(updateSourceApiDto.getApiOrigin())
                    .apiPath(updateSourceApiDto.getApiPath())
                    .apiPort(updateSourceApiDto.getApiPort())
                    .apiProtocol(updateSourceApiDto.getApiProtocol())
                    .apiTimeoutTime(updateSourceApiDto.getApiTimeoutTime())
                    .apiRequestMethod(updateSourceApiDto.getApiRequestMethod())
                    .apiRequestParams(requestParams)
                    .apiRequestBody(requestBody)
                    .apiResponse(response)
                    .build();
            //更新数据
            UpdateWrapper<SourceApi> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("api_code",updateSourceApiDto.getApiCode());
            sourceApiMapper.update(sourceApi1,updateWrapper);
            return R.Success("数据接口源更新成功");
        }
        return R.Failed("未找到数据");
    }

    /**
     * 接口数据源删除
     * @param deleteSourceApiDto
     * @return
     */
    @Override
    public R deleteSourceApi(DeleteSourceApiDto deleteSourceApiDto) {
        log.info("正在处理了数据库数据源删除请求");

        MPJLambdaWrapper<SourceApi> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper
                .selectAll(SourceApi.class)
                .eq(SourceApi::getApiCode,deleteSourceApiDto.getApiCode());
        SourceApi sourceApi = sourceApiMapper.selectOne(lambdaQueryWrapper);


        if (sourceApi!=null&&sourceApi.getApiState().equals(0)){
            //进行删除
            UpdateWrapper<SourceApi> updateWrapper =new UpdateWrapper<>();
            updateWrapper
                    .eq("api_code",deleteSourceApiDto.getApiCode());

            SourceApi sourceApi1=SourceApi.builder()
                    .deleteFlag(true)
                    .build();
            sourceApiMapper.update(sourceApi1,updateWrapper);
            return R.Success("数据库接口源删除成功");
        }
        return R.Failed("接口源不是未发布状态，无法删除");
    }

    @Override
    public R testSourceApi(TestSourceApiDto testSourceApiDto) {
        log.info("正在处理接口数据源测试请求");

        return R.Success(testSourceApiDto);
    }

    /**
     * 接口数据源状态更改
     * @param stateSourceApiDto
     * @return
     */
    @Override
    public R stateSourceApi(StateSourceApiDto stateSourceApiDto) {
        log.info("正在处理数据源状态更改请求");

        MPJLambdaWrapper<SourceApi> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper
                .selectAll(SourceApi.class)
                .eq(SourceApi::getApiCode,stateSourceApiDto.getApiCode());
        SourceApi sourceApi = sourceApiMapper.selectOne(lambdaQueryWrapper);

        if (sourceApi!=null){
            SourceApi sourceApi1=SourceApi.builder()
                    .apiState(stateSourceApiDto.getApiState())
                    .build();

            UpdateWrapper<SourceApi> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("api_code",stateSourceApiDto.getApiCode());
            sourceApiMapper.update(sourceApi1,updateWrapper);
            return R.Success("状态修改成功");
        }
        return R.Failed("未找到数据");
    }

    /**
     * 接口数据源批量发布
     * @param deleteSourceApiDtos
     * @return
     */
    @Override
    public R batchPublishSourceApi(List<DeleteSourceApiDto> deleteSourceApiDtos) {
        log.info("正在处理数据源批量发布请求");
        try {
            List<SourceApi> sourceApiList = new ArrayList<>();
            //根据其编号从数据库中获取对应的 SourceApi 对象，并将其状态更新为新状态
            for (DeleteSourceApiDto deleteSourceApiDto : deleteSourceApiDtos) {
                //通过 api_code 查询数据
                MPJLambdaWrapper<SourceApi> lambdaQueryWrapper = new MPJLambdaWrapper<>();
                lambdaQueryWrapper
                        .selectAll(SourceApi.class)
                        .eq(SourceApi::getApiCode,deleteSourceApiDto.getApiCode());
                SourceApi sourceApi = sourceApiMapper.selectOne(lambdaQueryWrapper);
                //将 apiState 的值赋给 SourceApi 并保存
                if (sourceApi != null&&sourceApi.getApiState()==0) {
                    sourceApi.setApiState(1);
                    sourceApiList.add(sourceApi);
                }
            }

            if (!sourceApiList.isEmpty()) {
                this.updateBatchById(sourceApiList);
                return R.Success("批量发布成功") ;
            }
        } catch (Exception e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return R.Failed("批量发布失败");
    }

    /**
     * 接口数据源批量停用
     * @param deleteSourceApiDtos
     * @return
     */
    @Override
    public R batchStopSourceApi(List<DeleteSourceApiDto> deleteSourceApiDtos) {
        log.info("正在处理接口数据源批量停用请求");
        try {
            List<SourceApi> sourceApiList = new ArrayList<>();
            //根据其编号从数据库中获取对应的 SourceApi 对象，并将其状态更新为新状态
            for (DeleteSourceApiDto deleteSourceApiDto : deleteSourceApiDtos) {
                //通过 api_code 查询数据
                MPJLambdaWrapper<SourceApi> wrapper=new MPJLambdaWrapper<>();
                wrapper
                        .selectAll(SourceApi.class)
                        .eq(SourceApi::getApiCode,deleteSourceApiDto.getApiCode());
                SourceApi sourceApi = sourceApiMapper.selectOne(wrapper);
                //将 apiCode 赋给 SourceApi 并保存
                if (!ObjectUtils.isEmpty(sourceApi)&&sourceApi.getApiState()==1&&sourceApi.getDeleteFlag().equals(false)) {
                    sourceApi.setApiState(2);
                    sourceApiList.add(sourceApi);
                }
            }

            if (!sourceApiList.isEmpty()) {
                this.updateBatchById(sourceApiList);
                return R.Success("批量修改成功");
            }
        } catch (Exception e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return R.Failed("批量停用出现异常");
    }


}
