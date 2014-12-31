/**
 * @path com.zdtx.ifms.specific.service.monitor
 * @file VideoDownloadManager.java
 */
package com.zdtx.ifms.specific.service.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.vo.monitor.MediaVO;

/**
 * @description 
 * @author Liu Jun
 * @since 2014年9月10日 上午11:42:52
 */
@Service
@Transactional
public class VideoDownloadManager {
	
	@Autowired
	private BaseDao dao;
	
	/**
	 * 获取视频数据
	 * @param deviceIP			设备IP
	 * @param searchDate	查询日期
	 * @param beginTime		查询开始时间
	 * @param endTime		查询结束时间
	 * @return Page<MediaVO>
	 */
	public Page<MediaVO> getData(String deviceIP, String searchDate, String beginTime, String endTime) {
		StringBuilder stringBuilder = getDataString(deviceIP, searchDate, beginTime, endTime);
		Page<MediaVO> page = new Page<MediaVO>(0);
		if(stringBuilder == null || stringBuilder.length() == 0) {
			return page;
		}
		List<MediaVO> dataList = new ArrayList<MediaVO>();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(stringBuilder.toString()); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			int counts = Integer.valueOf(rootElt.elementTextTrim("counts"));	//获取数据个数
			if(counts > 0) {	//有数据
				Element dataElt = null;
				MediaVO mediaVO = null;
				for (int i = 0; i < counts; i++) {
					mediaVO = new MediaVO();
					dataElt = rootElt.element("i" + i);
					mediaVO.setLabel(i + 1L);
					mediaVO.setMediaType(dataElt.elementTextTrim("mediaType"));
					mediaVO.setDestPath(dataElt.elementTextTrim("destPath"));
					mediaVO.setResolution(dataElt.elementTextTrim("resolution"));
					mediaVO.setTriggerTime(dataElt.elementTextTrim("triggerTime"));
					mediaVO.setBeginTime(dataElt.elementTextTrim("beginTime"));
					mediaVO.setEndTime(dataElt.elementTextTrim("endTime"));
					dataList.add(mediaVO);
				}
			}
			page.setTotalCount(counts);
			page.setPageSize(counts);
			page.setResult(dataList);
		} catch (Exception e) {
		}
		return page;
	}
	
	private StringBuilder getDataString(String deviceIP, String searchDate, String beginTime, String endTime) {
		String searchUrl = "http://" + deviceIP + "/cgi-bin/admin/lsctrl.cgi?cmd=search&triggerTime=%27"
										+ searchDate + "%20" + beginTime + "%27+TO+%27" + searchDate + "%20" + endTime + "%27";
		BufferedReader reader = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			URL url = new URL(searchUrl);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));	//获取url数据
			String lineStr;
			while ((lineStr = reader.readLine()) != null) {		//读url数据拼接字符串
				stringBuilder.append(lineStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		return stringBuilder;
	}
	
	/**
	 * 根据车牌号获取车载机IP
	 * @param vehicleName 车辆名称（车牌号）
	 * @return String 			车载机IP
	 */
	public String getIPByVehicle(String vehicleName) {
		if(Utils.isEmpty(vehicleName)) {
			return null;
		}
		String sql = "SELECT 1 AS KEY, CCTVIP AS VALUE "
				+ "FROM T_CORE_VEHICLE "
				+ "WHERE ISDELETE = 'F' AND VEHICLENAME = '" + vehicleName + "'";
		List<KeyAndValue> dataList = dao.getKeyAndValueBySQL(sql);
		if(dataList != null && dataList.size() > 0) {
			return dataList.get(0).getValue();
		} else {
			return null;
		}
	}
}