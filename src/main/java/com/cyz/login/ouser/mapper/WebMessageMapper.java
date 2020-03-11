package com.cyz.login.ouser.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cyz.login.ouser.pojo.WebMessage;

@Mapper
public interface WebMessageMapper {
	
	@Select("select * from webmapper where code = #{code}")
	WebMessage getByCode(String code);

}
