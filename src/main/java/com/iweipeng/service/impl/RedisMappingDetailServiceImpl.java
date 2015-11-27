package com.iweipeng.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iweipeng.mapper.RedisMappingDetailMapper;
import com.iweipeng.service.RedisMappingDetailService;
import com.weiju.common.model.RedisMappingDetail;

@Service
public class RedisMappingDetailServiceImpl implements RedisMappingDetailService {

	private static Logger				logger	= Logger.getLogger(RedisMappingDetailServiceImpl.class);

	@Autowired
	private RedisMappingDetailMapper	redisMappingDetailMapper;

	@Override
	public List<RedisMappingDetail> getRedisMappingDetailListByMapId(Integer mapId) {
		logger.debug("查询RedisMappingDetail列表，查询条件mapId为：" + mapId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mapId", mapId);
		return redisMappingDetailMapper.findRedisDataRuleList(params);
	}

}
