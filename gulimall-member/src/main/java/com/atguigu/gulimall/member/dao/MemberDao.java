package com.atguigu.gulimall.member.dao;

import com.atguigu.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zhusg02
 * @email zsg179@163.com
 * @date 2021-01-17 11:51:50
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
