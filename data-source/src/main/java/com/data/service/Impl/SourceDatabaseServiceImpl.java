package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.dto.SourceDatabase.*;
import com.data.entity.SourceDatabase;
import com.data.mapper.SourceDatabaseMapper;
import com.data.service.SourceDatabaseService;
import com.data.utils.DBHelper;
import com.data.utils.JacksonUtil;
import com.data.utils.R;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 数据库数据源 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-04-02
 */
@Service
@Slf4j
public class SourceDatabaseServiceImpl extends ServiceImpl<SourceDatabaseMapper, SourceDatabase> implements SourceDatabaseService {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private SourceDatabaseMapper sourceDatabaseMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 数据库数据源查询
     * @param sourceDatabasePageDto
     * @return
     */
    @Override
    public R selectSourceDatabase(SourceDatabasePageDto sourceDatabasePageDto) {
        logger.info("正在处理数据库数据源查询请求");

        //将 pageSize 和 pageNumber 放入Page中
        Page<SourceDatabase> page=new Page<>(sourceDatabasePageDto.getPageNumber(),sourceDatabasePageDto.getPageSize());
        //查询条件
        MPJLambdaWrapper<SourceDatabase> wrapper=new MPJLambdaWrapper<>();
        wrapper
                .selectAll(SourceDatabase.class)
                .like(!ObjectUtils.isEmpty(sourceDatabasePageDto.getDatabaseSourceName()),SourceDatabase::getDatabaseSourceName,sourceDatabasePageDto.getDatabaseSourceName())
                .eq(!ObjectUtils.isEmpty(sourceDatabasePageDto.getDatabaseSourceState()),SourceDatabase::getDatabaseSourceState,sourceDatabasePageDto.getDatabaseSourceState());

        IPage<SourceDatabase> sourceDatabaseIPage=sourceDatabaseMapper.selectPage(page,wrapper);

        List<SourceDatabase> records=sourceDatabaseIPage.getRecords();
        Map responseData=new HashMap<>();
        responseData.put("data", records);
        System.out.println("responseData："+responseData);
        responseData.put("total", sourceDatabaseIPage.getTotal()); // 总记录数
        responseData.put("pageSize", sourceDatabaseIPage.getSize()); // 每页显示数量
        responseData.put("pageNumber", sourceDatabaseIPage.getCurrent()); // 当前页码
//        responseData.put("orders", sourceDatabaseIPage.orders()); // 排序信息
//        responseData.put("optimizeCountSql", sourceDatabaseIPage.optimizeCountSql()); // 是否优化count语句
        responseData.put("pages", sourceDatabaseIPage.getPages()); // 总页数
        logger.info("处理数据库数据源查询请求结束");
        return R.Success(responseData);
    }

    /**
     * 数据库数据源新增
     * @param addSourceDatabaseDto
     * @return
     */
    @Override
    public R addSourceDatabase(AddSourceDatabaseDto addSourceDatabaseDto) {
        logger.info("正在处理数据库数据源新增请求");

        String databaseSourceProp=null;

        try {
            //判断数据库名称是否重复
            //构建查询条件
            MPJLambdaWrapper<SourceDatabase> lambdaQueryWrapperName = new MPJLambdaWrapper<>();
            //输入查询条件——数据库名称判空
            lambdaQueryWrapperName
                    .selectAll(SourceDatabase.class)
                    .eq(SourceDatabase::getDatabaseSourceName,addSourceDatabaseDto.getDatabaseSourceName())
                    .eq(SourceDatabase::getDeleteFlag,0);
            logger.info("检验数据库名称是否为空");
            SourceDatabase sourceDatabaseName = sourceDatabaseMapper.selectOne(lambdaQueryWrapperName);
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(sourceDatabaseName)){
                return R.BAD_REQUEST("该数据库名称已存在");
            }

            //输入查询条件——连接信息判空
            MPJLambdaWrapper<SourceDatabase> lambdaQueryWrapperUrl = new MPJLambdaWrapper<>();
            lambdaQueryWrapperUrl
                    .selectAll(SourceDatabase.class)
                    .eq(SourceDatabase::getDatabaseSourceUrl,addSourceDatabaseDto.getDatabaseSourceUrl())
                    .eq(SourceDatabase::getDeleteFlag,0);
            SourceDatabase sourceDatabaseUrl = sourceDatabaseMapper.selectOne(lambdaQueryWrapperUrl);
            logger.info("检查连接信息是否为空");
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(sourceDatabaseUrl)){
                return R.BAD_REQUEST("该连接信息已存在");
            }
            //将参数配置转为JSON
            if (!ObjectUtils.isEmpty(addSourceDatabaseDto.getDatabaseSourceProp())){
                //将 Java 对象转换为 JSON 字符串
                databaseSourceProp= JacksonUtil.bean2Json(addSourceDatabaseDto.getDatabaseSourceProp());
            }

            //存入码表数据到 DataStandard
            SourceDatabase sourceDatabase = SourceDatabase.builder()
                    .databaseSourceName(addSourceDatabaseDto.getDatabaseSourceName())
                    .databaseSourceType(addSourceDatabaseDto.getDatabaseSourceType())
                    .databaseSourceUrl(addSourceDatabaseDto.getDatabaseSourceUrl())
                    .databaseSourceDesc(addSourceDatabaseDto.getDatabaseSourceDesc())
                    .databaseSourceProp(databaseSourceProp)
                    .build();
            //进行新增码表操作
            sourceDatabaseMapper.insert(sourceDatabase);
            System.out.println("返回新增的数据");
            return R.Success(sourceDatabaseMapper.selectOne(lambdaQueryWrapperName));
        } catch (Exception  e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return R.Failed("新增数据库数据源信息异常");
    }

    /**
     * 数据库连通测试
     * @param connectionDatabaseDto
     * @return
     */
    @Override
    public R testDatabaseConnection(ConnectionDatabaseDto connectionDatabaseDto) {
        Connection conn = null;

        try {
            String type=connectionDatabaseDto.getDatabaseSourceType();
            String jdbcurl = connectionDatabaseDto.getDatabaseSourceUrl();
            String Name= connectionDatabaseDto.getDatabaseSourceProp().getDatabaseName();
            String password=connectionDatabaseDto.getDatabaseSourceProp().getDatabasePassword();
            System.out.println("type:"+type);
            System.out.println("jdbcurl:"+jdbcurl);
            System.out.println("Name:"+Name);
            System.out.println("password:"+password);
            //判断数据库类型
            if ("ORACLE".equals(type.toUpperCase())) {

                conn = DBHelper.initOracle(jdbcurl,Name,password, false);

            } else if ("MYSQL".equals(type.toUpperCase())) {

                conn =DBHelper.initMysql(jdbcurl, Name,password, false);

            } else if ("DB2".equals(type.toUpperCase())) {

                conn =DBHelper.initDB2(jdbcurl, Name,password, false);

            } else if ("SQLSERVER".equals(type.toUpperCase())) {

                conn =DBHelper.initSQLServer(jdbcurl, Name,password, false);

            }else if("GREENPLUM".equals(type.toUpperCase())){

                conn = DBHelper.initGreenPlum(jdbcurl, Name, password, false);

            }else if("HIVE".equals(type.toUpperCase())){
                conn = DBHelper.initHIVE(jdbcurl, Name, password, false);
            }else {
                return R.Failed("数据库类型错误");
            }

            if (!ObjectUtils.isEmpty(conn)){
                logger.info("数据源连接成功");
                return R.Success("数据源连接成功");
            }else {
                logger.info("数据源连接失败");
                return R.Failed("数据源连接失败");
            }

        } catch (Exception e) {
            logger.error("数据源连接失败",e);
            return R.Failed("数据源连接失败");
        }finally {
            //关闭数据库连接
            if (conn != null) {
                DBHelper.closeDB(conn, null, null);
            }
        }


    }

    /**
     * 数据库数据源编辑
     * @param updateSourceDatabaseDto
     * @return
     */
    @Override
    public R updateSourceDatabase(UpdateSourceDatabaseDto updateSourceDatabaseDto) {
        logger.info("正在处理数据库数据源编辑请求");
        String databaseSourceProp=null;

        SourceDatabase sourceDatabase=sourceDatabaseMapper.selectById(updateSourceDatabaseDto.getId());
        //判断数据库名称是否重复
        //构建查询条件
        MPJLambdaWrapper<SourceDatabase> lambdaQueryWrapperName = new MPJLambdaWrapper<>();
        //输入查询条件——数据库名称判空
        lambdaQueryWrapperName
                .selectAll(SourceDatabase.class)
                .eq(SourceDatabase::getDatabaseSourceName,updateSourceDatabaseDto.getDatabaseSourceName())
                .eq(SourceDatabase::getDeleteFlag,0);
        logger.info("检验数据库名称是否为空");
        SourceDatabase sourceDatabaseName = sourceDatabaseMapper.selectOne(lambdaQueryWrapperName);
        //检查查询结果是否为空
        if(!ObjectUtils.isEmpty(sourceDatabaseName)&&!sourceDatabase.getDatabaseSourceName().equals(sourceDatabaseName.getDatabaseSourceName())){
            return R.BAD_REQUEST("该数据库名称已存在");
        }

        //输入查询条件——连接信息判空
        MPJLambdaWrapper<SourceDatabase> lambdaQueryWrapperUrl = new MPJLambdaWrapper<>();
        lambdaQueryWrapperUrl
                .selectAll(SourceDatabase.class)
                .eq(SourceDatabase::getDatabaseSourceUrl,updateSourceDatabaseDto.getDatabaseSourceUrl())
                .eq(SourceDatabase::getDeleteFlag,0);
        SourceDatabase sourceDatabaseUrl = sourceDatabaseMapper.selectOne(lambdaQueryWrapperUrl);
        logger.info("检查连接信息是否为空");
        //检查查询结果是否为空
        if(!ObjectUtils.isEmpty(sourceDatabaseUrl)&&!sourceDatabase.getDatabaseSourceUrl().equals(sourceDatabaseUrl.getDatabaseSourceUrl())){
            return R.BAD_REQUEST("该连接信息已存在");
        }
        //将参数配置转为JSON
        if (!ObjectUtils.isEmpty(updateSourceDatabaseDto.getDatabaseSourceProp())){
            //将 Java 对象转换为 JSON 字符串
            databaseSourceProp= JacksonUtil.bean2Json(updateSourceDatabaseDto.getDatabaseSourceProp());
        }
        if (sourceDatabase!=null&&sourceDatabase.getDatabaseSourceState()!=1){
            if (sourceDatabase.getDeleteFlag().equals(true)){
                return R.Failed("数据库已删除，无法修改");
            }
            SourceDatabase sourceDatabase1=SourceDatabase.builder()
                    .databaseSourceName(updateSourceDatabaseDto.getDatabaseSourceName())
                    .databaseSourceType(updateSourceDatabaseDto.getDatabaseSourceType())
                    .databaseSourceDesc(updateSourceDatabaseDto.getDatabaseSourceDesc())
                    .databaseSourceUrl(updateSourceDatabaseDto.getDatabaseSourceUrl())
                    .databaseSourceProp(databaseSourceProp)
                    .build();

            UpdateWrapper<SourceDatabase> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",updateSourceDatabaseDto.getId());
            sourceDatabaseMapper.update(sourceDatabase1,updateWrapper);
            return R.Success("编辑成功");
        }
        return R.Failed("编辑失败");
    }

    /**
     * 数据库数据源删除
     * @param deleteSourceDatabaseDto
     * @return
     */
    @Override
    public R deleteSourceDatabase(DeleteSourceDatabaseDto deleteSourceDatabaseDto) {
        logger.info("正在处理了数据库数据源删除请求");

        SourceDatabase sourceDatabase=sourceDatabaseMapper.selectById(deleteSourceDatabaseDto.getId());
        if (sourceDatabase!=null&&sourceDatabase.getDeleteFlag().equals(false)){
            //进行删除
            UpdateWrapper<SourceDatabase> updateWrapper =new UpdateWrapper<>();
            updateWrapper
                    .eq(deleteSourceDatabaseDto.getId()!=null,"id",deleteSourceDatabaseDto.getId());

            SourceDatabase sourceDatabase1=SourceDatabase.builder()
                    .deleteFlag(true)
                    .build();
            sourceDatabaseMapper.update(sourceDatabase1,updateWrapper);
            return R.Success("数据库数据源删除成功");
        }
        return R.Failed("数据库数据源删除失败");
    }

    /**
     * 数据库数据源状态更改
     * @param stateSourceDatabaseDto
     * @return
     */
    @Override
    public R stateSourceDatabase(StateSourceDatabaseDto stateSourceDatabaseDto) {
        logger.info("正在处理数据库数据源状态更改请求");
        SourceDatabase sourceDatabase=sourceDatabaseMapper.selectById(stateSourceDatabaseDto.getId());
        if (sourceDatabase!=null){
            SourceDatabase dataStandard1=SourceDatabase.builder()
                    .databaseSourceState(stateSourceDatabaseDto.getDatabaseSourceState())
                    .build();

            UpdateWrapper<SourceDatabase> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",stateSourceDatabaseDto.getId());
            sourceDatabaseMapper.update(dataStandard1,updateWrapper);
            return R.Success("状态修改成功");
        }
        return R.Failed("状态修改失败");
    }

    /**
     * 数据库数据源批量发布
     * @param deleteSourceDatabaseDtos
     * @return
     */
    @Override
    public R batchPublishSourceDatabase(List<DeleteSourceDatabaseDto> deleteSourceDatabaseDtos) {
        logger.info("正在处理数据库数据源批量发布请求");
        try {
            List<SourceDatabase> sourceDatabaseList = new ArrayList<>();
            //根据其编号从数据库中获取对应的 SourceDatabase 对象，并将其状态更新为新状态
            for (DeleteSourceDatabaseDto deleteSourceDatabaseDto : deleteSourceDatabaseDtos) {
                //通过 id 查询数据
                SourceDatabase sourceDatabase = sourceDatabaseMapper.selectById(deleteSourceDatabaseDto.getId());
                //将 databaseSourceState 的值赋给 SourceDatabase 并保存
                if (sourceDatabase != null&&sourceDatabase.getDatabaseSourceState()==0) {
                    sourceDatabase.setDatabaseSourceState(1);
                    sourceDatabaseList.add(sourceDatabase);
                }
            }

            if (!sourceDatabaseList.isEmpty()) {
                this.updateBatchById(sourceDatabaseList);
                return R.Success("批量发布成功") ;
            }
        } catch (Exception e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return R.Failed("批量发布失败");
    }

    @Override
    public R batchStopSourceDatabase(List<DeleteSourceDatabaseDto> deleteSourceDatabaseDtos) {
        logger.info("正在处理数据库数据源批量停用请求");
        try {
            List<SourceDatabase> sourceDatabaseList = new ArrayList<>();
            //根据其编号从数据库中获取对应的 SourceDatabase 对象，并将其状态更新为新状态
            for (DeleteSourceDatabaseDto deleteSourceDatabaseDto : deleteSourceDatabaseDtos) {
                //通过 id 查询数据
                MPJLambdaWrapper<SourceDatabase> wrapper=new MPJLambdaWrapper<>();
                wrapper
                        .selectAll(SourceDatabase.class)
                        .eq(SourceDatabase::getId,deleteSourceDatabaseDto.getId());
                SourceDatabase sourceDatabase = sourceDatabaseMapper.selectOne(wrapper);
                //将 id 赋给 SourceDatabase 并保存
                if (!ObjectUtils.isEmpty(sourceDatabase)&&sourceDatabase.getDatabaseSourceState()==1&&sourceDatabase.getDeleteFlag().equals(false)) {
                    sourceDatabase.setDatabaseSourceState(2);
                    sourceDatabaseList.add(sourceDatabase);
                }
            }

            if (!sourceDatabaseList.isEmpty()) {
                this.updateBatchById(sourceDatabaseList);
                return R.Success("批量修改成功");
            }
        } catch (Exception e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return R.Failed("批量停用出现异常");
    }
}
