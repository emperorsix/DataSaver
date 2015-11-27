package com.iweipeng.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iweipeng.mapper.RedisMappingMapper;
import com.iweipeng.service.RedisMappingService;
import com.weiju.common.model.RedisMapping;

@Service("redisMappingService")
public class RedisMappingServiceImpl implements RedisMappingService {

	private static Logger		logger	= Logger.getLogger(RedisMappingServiceImpl.class);

	@Autowired
	private RedisMappingMapper	redisMappingMapper;

	@Override
	public void updateUpdateDateByMapId(Integer mapId, Date updateDate) {
		logger.debug("修改redis_mapping表的更新时间，作为上一次的数据提取时间。");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mapId", mapId);
		params.put("lastUpdate", updateDate);
//		params.put("status", AppParams.ConvertStatus.YES.getValue());
		redisMappingMapper.updateUpdateDateByMapId(params);
	}

	@Override
	public List<RedisMapping> getRedisMappingList() {
		logger.debug("开始查询RedisMapping数据");
		return redisMappingMapper.findRedisMappingList();
	}
}
