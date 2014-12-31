package com.zdtx.ifms.specific.service.query;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.ExportExcel;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.specific.model.query.Track;
import com.zdtx.ifms.specific.vo.query.TrackVO;

@Service
@Transactional
public class TrackManager {
	@Autowired
	private BaseDao baseDao;
	
	@SuppressWarnings("unchecked")
	public List<Track> getPage(Page<Track> page,TrackVO trvo){
		String date1=trvo.getDatemin().substring(0, 10).replaceAll("-", "");
		String date2=trvo.getDatemax().substring(0, 10).replaceAll("-", "");
		List<Track> tracks=new ArrayList<Track>();
		String sql1="";
		if(date1.equals(date2)){
			sql1="select count(1) as key,count(1) as value from t_jk_fullgpsdata"+date1+" t where t.o_terminalno="+trvo.getBusid()+"  and t.o_gpsdatetime <=to_date('"+trvo.getDatemax()+"','yyyy-mm-dd hh24:mi:ss') and  t.o_gpsdatetime >=to_date('"+trvo.getDatemin()+"','yyyy-mm-dd hh24:mi:ss')   order by  t.o_Gpsdatetime";
		}else{
			sql1="select count(1) as key,count(1) as value from (select * from t_jk_fullgpsdata"+date1+" where o_terminalno="+trvo.getBusid()+" and o_gpsdatetime >=to_date('"+trvo.getDatemin()+"','yyyy-mm-dd hh24:mi:ss')     UNION ALL select * from t_jk_fullgpsdata"+date2+" where o_terminalno="+trvo.getBusid()+" and  o_gpsdatetime <=to_date('"+trvo.getDatemax()+"','yyyy-mm-dd hh24:mi:ss') )";
		}
		List<KeyAndValue> kv= baseDao.getKeyAndValueBySQL(sql1);
		if(kv!=null && kv.size()!=0){
			page.setTotalCount(Integer.valueOf(kv.get(0).getKey()));
			String sql="";
			String excsql="";
			if(date1.equals(date2)){
				sql="select * from (  select row_.*, rownum rownum_ from (select o_busname,o_date,o_time,round(o_longitude/60,2) as o_longitude ,round(o_latitude/60,2) as o_latitude,o_speed,o_direction*2 as o_direction from t_jk_fullgpsdata"+date1+" t where t.o_terminalno="+trvo.getBusid()+" and t.o_gpsdatetime <=to_date('"+trvo.getDatemax()+"','yyyy-mm-dd hh24:mi:ss') and  t.o_gpsdatetime >=to_date('"+trvo.getDatemin()+"','yyyy-mm-dd hh24:mi:ss')  order by  t.o_Gpsdatetime) row_ where rownum <= "+(page.getFirst()+page.getPageSize())+" )  where   rownum_ > "+page.getFirst();
				excsql="select o_busname,o_date,o_time,round(o_longitude/60,2) as o_longitude ,round(o_latitude/60,2) as o_latitude,o_speed,o_direction*2 as o_direction from t_jk_fullgpsdata"+date1+" t where t.o_terminalno="+trvo.getBusid()+" and t.o_gpsdatetime <=to_date('"+trvo.getDatemax()+"','yyyy-mm-dd hh24:mi:ss') and  t.o_gpsdatetime >=to_date('"+trvo.getDatemin()+"','yyyy-mm-dd hh24:mi:ss')  order by  t.o_Gpsdatetime";
			}else{
				sql="select * from (  select row_.*, rownum rownum_ from (select o_busname,o_date,o_time,o_longitude ,o_latitude,o_speed,o_direction*2 as o_direction from ( select o_busname,o_date,o_time,round(o_longitude/60,2) as o_longitude ,round(o_latitude/60,2) as o_latitude,o_speed,o_direction,o_Gpsdatetime from t_jk_fullgpsdata"+date1+" where o_terminalno="+trvo.getBusid()+" and o_gpsdatetime >=to_date('"+trvo.getDatemin()+"','yyyy-mm-dd hh24:mi:ss')     UNION ALL select o_busname,o_date,o_time,round(o_longitude/60,2) as o_longitude ,round(o_latitude/60,2) as o_latitude,o_speed,o_direction,o_Gpsdatetime from t_jk_fullgpsdata"+date2+" where o_terminalno="+trvo.getBusid()+" and  o_gpsdatetime <=to_date('"+trvo.getDatemax()+"','yyyy-mm-dd hh24:mi:ss')) order by  o_Gpsdatetime) row_ where rownum <= "+(page.getFirst()+page.getPageSize())+" )  where   rownum_ > "+page.getFirst();
				excsql="select o_busname,o_date,o_time,o_longitude ,o_latitude,o_speed,o_direction*2 as o_direction from ( select o_busname,o_date,o_time,round(o_longitude/60,2) as o_longitude ,round(o_latitude/60,2) as o_latitude,o_speed,o_direction,o_Gpsdatetime from t_jk_fullgpsdata"+date1+" where o_terminalno="+trvo.getBusid()+" and o_gpsdatetime >=to_date('"+trvo.getDatemin()+"','yyyy-mm-dd hh24:mi:ss')     UNION ALL select o_busname,o_date,o_time,round(o_longitude/60,2) as o_longitude ,round(o_latitude/60,2) as o_latitude,o_speed,o_direction,o_Gpsdatetime from t_jk_fullgpsdata"+date2+" where o_terminalno="+trvo.getBusid()+" and  o_gpsdatetime <=to_date('"+trvo.getDatemax()+"','yyyy-mm-dd hh24:mi:ss')) order by  o_Gpsdatetime";
			}
			 Struts2Util.getSession().setAttribute("trackexcel", excsql);
			SQLQuery query = baseDao.getSession().createSQLQuery(sql);
			query.addScalar("o_busname", StringType.INSTANCE);
			query.addScalar("o_date", StringType.INSTANCE);
			query.addScalar("o_time", StringType.INSTANCE);
			query.addScalar("o_longitude", StringType.INSTANCE);
			query.addScalar("o_latitude", StringType.INSTANCE);
			query.addScalar("o_speed", LongType.INSTANCE);
			query.addScalar("o_direction", LongType.INSTANCE);
			query.setResultTransformer(Transformers.aliasToBean(Track.class));
			tracks= (List<Track>) query.list();
		}
		page.setResult(tracks);
		return tracks;
	}

	@SuppressWarnings("unchecked")
	public InputStream getData() {
		String sql=(String)Struts2Util.getSession().getAttribute("trackexcel");
		
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		query.addScalar("o_busname", StringType.INSTANCE);
		query.addScalar("o_date", StringType.INSTANCE);
		query.addScalar("o_time", StringType.INSTANCE);
		query.addScalar("o_longitude", StringType.INSTANCE);
		query.addScalar("o_latitude", StringType.INSTANCE);
		query.addScalar("o_speed", LongType.INSTANCE);
		query.addScalar("o_direction", LongType.INSTANCE);
		query.setResultTransformer(Transformers.aliasToBean(Track.class));
		List<Track> data = (List<Track>) query.list();
	
		ExportExcel ee = new ExportExcel() {

			@Override
			protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] total,List<?> data) throws IOException {
				HSSFSheet sheet = wb.getSheetAt(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
				HSSFRow rowss = sheet.createRow(0);
				rowss.setHeightInPoints(20);
				HSSFCell hssfCell = rowss.createCell(0);
				hssfCell = this.createCell(wb, hssfCell, "Tracking Report");
				HSSFCellStyle style = this.createStyle(wb);
				HSSFRow row2 = sheet.createRow(1);
				HSSFCell cellrow01 = row2.createCell(0);
				cellrow01.setCellStyle(style);
				HSSFCell cellrow02 = row2.createCell(1);
				cellrow02.setCellStyle(style);
				HSSFCell cellrow03 = row2.createCell(2);
				cellrow03.setCellStyle(style);
				HSSFCell cellrow04 = row2.createCell(3);
				cellrow04.setCellStyle(style);
				HSSFCell cellrow05 = row2.createCell(4);
				cellrow05.setCellStyle(style);
				HSSFCell cellrow06 = row2.createCell(5);
				cellrow06.setCellStyle(style);
				HSSFCell cellrow07 = row2.createCell(6);
				cellrow07.setCellStyle(style);
				HSSFCell cellrow08 = row2.createCell(7);
				cellrow08.setCellStyle(style);
				
				cellrow01.setCellValue("No.");
				cellrow02.setCellValue("Busname");
				cellrow03.setCellValue("Date");
				cellrow04.setCellValue("Time");
				cellrow05.setCellValue("Longitude");
				cellrow06.setCellValue("Latitude");
				//Speed Direction
				cellrow07.setCellValue("Speed(km/h)");
				cellrow08.setCellValue("Direction");
				if (null != data && 0 != data.size()) {
					for (int i = 0; i < data.size(); i++) {
						HSSFRow row = sheet.createRow(i + 2);
						Track o = (Track) data.get(i);
						HSSFCell cell001 = row.createCell(0);
						cell001.setCellStyle(style);
						cell001.setCellValue(i + 1);
						HSSFCell cell002 = row.createCell(1);
						cell002.setCellStyle(style);
						cell002.setCellValue(o.getO_busname());
						HSSFCell cell003 = row.createCell(2);
						cell003.setCellStyle(style);
						cell003.setCellValue(o.getO_date());
						HSSFCell cell004 = row.createCell(3);
						cell004.setCellStyle(style);
						cell004.setCellValue(o.getO_time());
						HSSFCell cell005 = row.createCell(4);
						cell005.setCellStyle(style);
						cell005.setCellValue(o.getO_longitude());
						HSSFCell cell006 = row.createCell(5);
						cell006.setCellStyle(style);
						cell006.setCellValue(o.getO_latitude());
						
						HSSFCell cell007 = row.createCell(6);
						cell007.setCellStyle(style);
						cell007.setCellValue(o.getO_speed());
						
						HSSFCell cell008 = row.createCell(7);
						cell008.setCellStyle(style);
						cell008.setCellValue(TrackManager.getfangwei( o.getO_direction()));
						
						
					}
				}
			
				return wb;
			}

		};

		Object[] total = new Object[1];
		total[0] = "";
		String str = "Tracking Report";
		return ee.export(total, data, str);
	}
	/***
	 * 中文名	英文名	字段取值范围
	北	North	[0, 22]∪[338,359]
	东北	Northeast	[23,67]
东	East	[68,112]
东南	Southeast	[113,157]
南	South	[158,202]
西南	Southwest	[203,247]
西	West	[248,292]
西北	Northwest	[293,337]

	 * @param j
	 * @return
	 */
	private static String getfangwei(Long j){
		if(j!=null){
			if(j<=22){
				return "North";
			}else if(j<=67) {
				return "Northeast";
			}else if(j<=112) {
				return "East";
			}else if(j<=157) {
				return "Southeast";
			}else if(j<=202) {
				return "South";
			}else if(j<=247) {
				return "Southwest";
			}else if(j<=292) {
				return "West";
			}else if(j<=337) {
				return "Northwest";
			}else if(j<=360) {
				return "North";
			}
		}
		return "";
	}
}
