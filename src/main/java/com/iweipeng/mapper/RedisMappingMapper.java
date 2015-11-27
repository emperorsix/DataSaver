package com.iweipeng.mapper;

import java.util.List;
import java.util.Map;

import com.weiju.common.model.RedisMapping;

public interface RedisMappingMapper extends BaseMapper<RedisMapping, String> {

	List<RedisMapping> findRedisMappingList();

	void updateUpdateDateByMapId(Map<String, Object> params);

}