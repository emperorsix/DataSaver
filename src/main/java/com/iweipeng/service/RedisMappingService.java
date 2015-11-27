package com.iweipeng.service;

import java.util.Date;
import java.util.List;

import com.weiju.common.model.RedisMapping;

public interface RedisMappingService {

	List<RedisMapping> getRedisMappingList();

	void updateUpdateDateByMapId(Integer mapId, Date updateDate);

}
