package com.jinglun.guard.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.jinglun.guard.user.domain.User;

/**
 * 
 * @author wangxiwei
 *
 */

@Mapper
public interface UserMapper {
	
	@Select("select * from t_user")
    User queryAllUser();
}
