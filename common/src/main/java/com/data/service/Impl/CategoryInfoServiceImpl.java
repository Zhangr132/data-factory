package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.dto.AddCategoryInfoDto;
import com.data.dto.DeleteCategoryInfoDto;
import com.data.dto.SelectCategoryInfoDto;
import com.data.dto.UpdateCategoryInfoDto;
import com.data.entity.CategoryInfo;
import com.data.mapper.CategoryInfoMapper;
import com.data.service.CategoryInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.R;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分类信息表(导航目录) 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Slf4j
@Service
public class CategoryInfoServiceImpl extends ServiceImpl<CategoryInfoMapper, CategoryInfo> implements CategoryInfoService {
    @Autowired
    private CategoryInfoMapper categoryInfoMapper;

    @Override
    public R selectCategoryInfo(SelectCategoryInfoDto selectCategoryInfoDto) {
        log.info("正在处理分类信息查询请求");
        //查询数据
        MPJLambdaWrapper<CategoryInfo> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper
                .selectAll(CategoryInfo.class)
                .eq(CategoryInfo::getDeleteFlag,0)
                .eq(CategoryInfo::getParentCode, selectCategoryInfoDto.getParentCode())
                .or(qw -> qw
                        .eq(CategoryInfo::getDeleteFlag,0)
                        .eq(CategoryInfo::getParentCode, selectCategoryInfoDto.getParentCode()))
                .eq(CategoryInfo::getCategoryCode,selectCategoryInfoDto.getParentCode());
        log.info("分类信息查询成功");
        return R.Success("查询成功", categoryInfoMapper.selectList(lambdaQueryWrapper));
    }

    /**
     * 添加分类信息
     * @param addCategoryInfoDto
     * @return
     */
    @Override
    public R addCategoryInfo(AddCategoryInfoDto addCategoryInfoDto) {
        log.info("正在处理分类信息添加请求");

        //判断是否重名
        MPJLambdaWrapper<CategoryInfo> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper
                .selectAll(CategoryInfo.class)
                .eq(CategoryInfo::getCategoryName, addCategoryInfoDto.getCategoryName())
                .eq(CategoryInfo::getParentCode,addCategoryInfoDto.getParentCode());
        CategoryInfo categoryInfoName = categoryInfoMapper.selectOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(categoryInfoName)&&categoryInfoName.getDeleteFlag()==0) {
            log.error("分类名称已存在");
            return R.Failed("分类名称已存在");
        }


        //生成编号
        String CAT="CAT";       //前缀
        String newCategoryCode;
        MPJLambdaWrapper<CategoryInfo> lambdaQueryWrapper1 = new MPJLambdaWrapper<>();
        //查询符合条件的最后一条数据
        lambdaQueryWrapper1
                .selectAll(CategoryInfo.class)
                .orderByDesc(CategoryInfo::getCategoryCode)
                .last("limit 1");
        log.info("查询符合条件的最后一条数据");
        CategoryInfo categoryInfo = categoryInfoMapper.selectOne(lambdaQueryWrapper1);

        if (ObjectUtils.isEmpty(categoryInfo)) {
            //如果数据库没有 0001
            newCategoryCode =CAT + "00001";
        }else {
            //如果数据库中有数据 拿最后一条数据的序号
            //最后一条数据账号
            String lastCategoryCode = categoryInfo.getCategoryCode();
            //截取序号部分，并将其转换为整数
            String idStr = lastCategoryCode.substring(3, lastCategoryCode.length());
            //将序号加一，并格式化为五位数的字符串
            Integer id = Integer.valueOf(idStr) + 1;
            String formatId = String.format("%05d", id);
            //拼接成新的 newCategoryCode，并将其设置到 addCategoryInfoDto 对象中
            newCategoryCode = CAT + (formatId);
        }

        //存入数据到数据库
        CategoryInfo categoryInfo1 =CategoryInfo.builder()
                .categoryCode(newCategoryCode)
                .categoryName(addCategoryInfoDto.getCategoryName())
                .parentCode(addCategoryInfoDto.getParentCode())
                .categoryDesc(addCategoryInfoDto.getCategoryDesc())
                .build();
        categoryInfoMapper.insert(categoryInfo1);
        log.info("分类信息添加成功");
        return R.Success("分类信息添加成功", categoryInfo1);
    }

    /**
     * 更新分类信息
     * @param updateCategoryInfoDto
     * @return
     */
    @Override
    public R updateCategoryInfo(UpdateCategoryInfoDto updateCategoryInfoDto) {
        log.info("正在处理分类信息更新请求");
        //判断是否重名
        MPJLambdaWrapper<CategoryInfo> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper
                .selectAll(CategoryInfo.class)
                .eq(CategoryInfo::getCategoryName, updateCategoryInfoDto.getCategoryName())
                .eq(CategoryInfo::getParentCode,updateCategoryInfoDto.getParentCode());
        CategoryInfo categoryInfoName = categoryInfoMapper.selectOne(lambdaQueryWrapper);
        if (categoryInfoName.getDeleteFlag()==1){
            log.error("分类信息已删除，无法更改");
            return R.Failed("分类信息已删除，无法更改");
        }else if (ObjectUtils.isNotEmpty(categoryInfoName)) {
            log.error("分类名称已存在");
            return R.Failed("分类名称已存在");
        }
        //更新数据
        UpdateWrapper<CategoryInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("category_code", updateCategoryInfoDto.getCategoryCode());

        CategoryInfo categoryInfo =CategoryInfo.builder()
                .categoryName(updateCategoryInfoDto.getCategoryName())
                .parentCode(updateCategoryInfoDto.getParentCode())
                .categoryDesc(updateCategoryInfoDto.getCategoryDesc())
                .build();

        categoryInfoMapper.update(categoryInfo, updateWrapper);
        log.info("分类信息更新成功");
        return R.Success("分类信息更新成功");
    }

    /**
     * 删除分类信息
     * @param deleteCategoryInfoDto
     * @return
     */
    @Override
    public R deleteCategoryInfo(DeleteCategoryInfoDto deleteCategoryInfoDto) {
        log.info("正在处理分类信息删除请求");
        //判断数据是否存在
        MPJLambdaWrapper<CategoryInfo> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper
                .selectAll(CategoryInfo.class)
                .eq(CategoryInfo::getCategoryCode, deleteCategoryInfoDto.getCategoryCode());
        CategoryInfo categoryInfo = categoryInfoMapper.selectOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(categoryInfo)) {
            log.error("分类信息不存在");
            return R.Failed("分类信息不存在");
        }


//        //删除数据
//        QueryWrapper<CategoryInfo> deleteWrapper = new QueryWrapper<>();
//        deleteWrapper
//                .eq("category_code", deleteCategoryInfoDto.getCategoryCode());
//        categoryInfoMapper.delete(deleteWrapper);


        //逻辑删除
        UpdateWrapper<CategoryInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("category_code", deleteCategoryInfoDto.getCategoryCode())
                .or()
                .eq("parent_code", deleteCategoryInfoDto.getCategoryCode())
                .set("delete_flag", 1);
        categoryInfoMapper.update(null, updateWrapper);
        log.info("分类信息删除成功");
        return R.Success("分类信息删除成功");
    }
}
