package com.zdtx.ifms.specific.service.query;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.service.BaseManager;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.ExportExcel;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.base.Dict;
import com.zdtx.ifms.specific.vo.query.RepDailyVo;

/**
 * @ClassName: RepDailyManager
 * @Description: 不规范驾驶报告-不规范驾驶日报-业务层
 * @author JHQ
 * @date 2012年11月29日 11:25:01
 * @version V1.0
 */

@Service
@Transactional
public class RepDailyManager {
	@Autowired
	public BaseDao baseDao;
	
	@Autowired
	public BaseManager baseMgr;

	public List<List<String>> getAllCount( RepDailyVo dvo){
		String ge = "";
		String le = "";
		boolean flag = false;
		if("1".equals(dvo.getBehCont())&&dvo.getDate1()!=null&&!"".equals(dvo.getDate1())){
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag=true;
		}
		if("2".equals(dvo.getBehCont())&&dvo.getDate2()!=null&&!"".equals(dvo.getDate2())){
			ge = DateUtil.dateToWeek(dvo.getDate2())[0];
			le = DateUtil.dateToWeek(dvo.getDate2())[1];
			flag=true;
		}
		if("3".equals(dvo.getBehCont())&&dvo.getDate3()!=null&&!"".equals(dvo.getDate3())){
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag=true;
		}
		if("4".equals(dvo.getBehCont())&&dvo.getDate4()!=null&&!"".equals(dvo.getDate4())){
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag=true;
		}
		
		List<Dict> list = this.getDict();
		String sql = "";

		sql = "select sum(beh_count),sum(score),";

		String sql1 = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sql1 = sql1 + "nvl(sum(decode(beh_type, '"
						+ list.get(i).getDictID() + "', beh_count)),0) a"
						+ list.get(i).getDictID() + ",";
						if(!list.get(i).getDictName().startsWith("Sudden")){
							sql1+= "'t'||sum(decode(beh_type, '" + list.get(i).getDictID();
							sql1+= "', beh_cont)) b" + list.get(i).getDictID() + ",";
						}
			}
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		sql = sql + sql1 + " from T_BEH_NOT_DAY where dept_id in (" + (String)Struts2Util.getSession().getAttribute("userDepartmentStr") + ") ";

		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and driver_id = " + dvo.getOrgId();
		}

		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='"+le+"'";
		}
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<List<String>> list1 = query.list();
		return list1;
	}
	
	
	public Page<List<String>> getAll(Page<List<String>> page, RepDailyVo dvo) {
		String sql = this.getSql(dvo);
		sql += " row_ where rownum <= "
				+ (page.getPageSize() + page.getFirst())
				+ " ) where rownum_ > " + page.getFirst();

		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<List<String>> list1 = query.list();
		page.setResult(list1);
		page.setTotalCount(getCount_(dvo));
		Utils.getSession().setAttribute("page", page);
		return page;
	}

	public String getSql(RepDailyVo dvo) {
		String ge = "";
		String le = "";
		boolean flag = false;
		if("1".equals(dvo.getBehCont())&&dvo.getDate1()!=null&&!"".equals(dvo.getDate1())){
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag=true;
		}
		if("2".equals(dvo.getBehCont())&&dvo.getDate2()!=null&&!"".equals(dvo.getDate2())){
			ge = DateUtil.dateToWeek(dvo.getDate2())[0];
			le = DateUtil.dateToWeek(dvo.getDate2())[1];
			flag=true;
		}
		if("3".equals(dvo.getBehCont())&&dvo.getDate3()!=null&&!"".equals(dvo.getDate3())){
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag=true;
		}
		if("4".equals(dvo.getBehCont())&&dvo.getDate4()!=null&&!"".equals(dvo.getDate4())){
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag=true;
		}
		
		
		List<Dict> list = this.getDict();
		String sql = "";

		sql = "select * from ( select rownum rownum_,row_.*  from (select max(driver_name),max(riqi),sum(beh_count),sum(score),";

		String sql1 = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sql1 = sql1 + "'d'||sum(decode(beh_type, '"
						+ list.get(i).getDictID() + "', beh_count)) a"
						+ list.get(i).getDictID() + ",";
						if(!list.get(i).getDictName().startsWith("Sudden")){
							sql1+= "'t'||sum(decode(beh_type, '" + list.get(i).getDictID();
							sql1+= "', beh_cont)) b" + list.get(i).getDictID() + ",";
						}
			}
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		sql = sql + sql1 + " from T_BEH_NOT_DAY where dept_id in (" + (String)Struts2Util.getSession().getAttribute("userDepartmentStr") + ") ";

		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and driver_id = " + dvo.getOrgId();
		}

		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='"+le+"'";
			sql = sql + " group by driver_id order by nvl(sum(score),0) DESC,max(driver_name)) ";
		}else{
			sql = sql + " group by driver_id ,riqi order by riqi DESC,sum(score) DESC)";
		}
		return sql;
	}

	public Integer getCount_(RepDailyVo dvo) {
		String ge = "";
		String le = "";
		boolean flag = false;
		if("1".equals(dvo.getBehCont())&&dvo.getDate1()!=null&&!"".equals(dvo.getDate1())){
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag=true;
		}
		if("2".equals(dvo.getBehCont())&&dvo.getDate2()!=null&&!"".equals(dvo.getDate2())){
			ge = DateUtil.dateToWeek(dvo.getDate2())[0];
			le = DateUtil.dateToWeek(dvo.getDate2())[1];
			flag=true;
		}
		if("3".equals(dvo.getBehCont())&&dvo.getDate3()!=null&&!"".equals(dvo.getDate3())){
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag=true;
		}
		if("4".equals(dvo.getBehCont())&&dvo.getDate4()!=null&&!"".equals(dvo.getDate4())){
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag=true;
		}
		String sql = "select count(1) from T_BEH_NOT_DAY where  dept_id in (" + (String)Struts2Util.getSession().getAttribute("userDepartmentStr") + ") ";
		
		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and driver_id = " + dvo.getOrgId();
		}
		
		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='"+le+"'";
		}
		
		sql = sql + " group by driver_id";
		
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		return query.list().size();
	}
	public Integer getCount(RepDailyVo dvo) {
		String ge = "";
		String le = "";
		boolean flag = false;
		if("1".equals(dvo.getBehCont())&&dvo.getDate1()!=null&&!"".equals(dvo.getDate1())){
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag=true;
		}
		if("2".equals(dvo.getBehCont())&&dvo.getDate2()!=null&&!"".equals(dvo.getDate2())){
			ge = DateUtil.dateToWeek(dvo.getDate2())[0];
			le = DateUtil.dateToWeek(dvo.getDate2())[1];
			flag=true;
		}
		if("3".equals(dvo.getBehCont())&&dvo.getDate3()!=null&&!"".equals(dvo.getDate3())){
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag=true;
		}
		if("4".equals(dvo.getBehCont())&&dvo.getDate4()!=null&&!"".equals(dvo.getDate4())){
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag=true;
		}
		String sql = "select count(1) from T_BEH_NOT_DAY where dept_id in (" + (String)Struts2Util.getSession().getAttribute("userDepartmentStr") + ") ";

		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and driver_id = " + dvo.getOrgId();
		}

		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='"+le+"'";
		}

		sql = sql + " group by driver_id,riqi";

		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		return query.list().size();
	}

	public List<Dict> getDict() {
		Criteria criteria = baseDao.getSession().createCriteria(Dict.class);
		criteria.add(Restrictions.eq("categoryID", 201000L));
		criteria.add(Restrictions.eq("isDelete", "F"));
		criteria.addOrder(Order.asc("dictID"));
		return baseDao.getAll(criteria);
	}
	public List<Dict> getDict(Long[] dictids) {
		Criteria criteria = baseDao.getSession().createCriteria(Dict.class);
		criteria.add(Restrictions.eq("categoryID", 201000L));
		criteria.add(Restrictions.eq("isDelete", "F"));
		criteria.add(Restrictions.in("dictID", dictids));
		criteria.addOrder(Order.asc("dictID"));
		return baseDao.getAll(criteria);
	}
	
	/**
	 * 生成-不规范驾驶日报
	 * @param dvo
	 * @return
	 */
	public InputStream getRep(RepDailyVo dvo,Object[] obj){
		
		@SuppressWarnings("unchecked")
		List<Dict> listd = (List<Dict>)obj[0];

		List<Object[]> data = new ArrayList<Object[]>();

		@SuppressWarnings("unchecked")
		Page<Object[]> page_export = (Page<Object[]>) Utils.getSession().getAttribute("page");
		
		page_export.setPageSize(page_export.getTotalCount());
		
		data = page_export.getResult();
		 
			
			ExportExcel ee = new ExportExcel(){
				
				@Override
				protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] total,
						List<?> data) throws IOException {
					
					@SuppressWarnings("unchecked")
					List<Dict> list= (List<Dict>)total[0]; 
					
					HSSFSheet sheet = wb.getSheetAt(0);
					
					HSSFRow rowss = sheet.createRow(0);
					rowss.setHeightInPoints(20);
					HSSFCell hssfCell = rowss.createCell(0);
				    hssfCell = this.createCell(wb, hssfCell, "Report Query");
					HSSFCellStyle style = this.createStyle(wb);
					HSSFRow row2 = sheet.createRow(1);
					HSSFRow row3 = sheet.createRow(2);
					HSSFRow row4 = sheet.createRow(3);
					
					HSSFCellStyle styleFont = this.createFont(wb);
					
					
					HSSFCell cellrow401 = row4.createCell(0);
					cellrow401.setCellStyle(styleFont);
					cellrow401.setCellValue("SUM");
					
					HSSFCell cellrow402 = row4.createCell(1);
					cellrow402.setCellStyle(styleFont);
					
					HSSFCell cellrow403 = row4.createCell(2);
					cellrow403.setCellStyle(styleFont);
					
					HSSFCell cellrow404 = row4.createCell(3);
					cellrow404.setCellStyle(styleFont);
					
					sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 3));
					
					Object[] objc = (Object[])total[1];
					int p=3;
					for (int i = 0; i < objc.length; i++) {
						HSSFCell cellrow405 = row4.createCell(i+4);
						cellrow405.setCellStyle(styleFont);
						if(String.valueOf(objc[i]).startsWith("t")){
							cellrow405.setCellValue(getValue(String.valueOf(objc[i]).replace("t", "")));
							p++;
						} else {
							 if(i == 1){
								 cellrow405.setCellValue(changfloat(Double.valueOf(objc[i].toString())));
							 } else {
								 cellrow405.setCellValue(objc[i].toString());
							 }
							p++;
						}
						 
					}
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,p));
					
					
					HSSFCell cellrow01 = row2.createCell(0);
					cellrow01.setCellStyle(style);
					
					HSSFCell cellrow301 = row3.createCell(0);
					cellrow301.setCellStyle(style);
					
					HSSFCell cellrow02 = row2.createCell(1);
					cellrow02.setCellStyle(style);
					
					HSSFCell cellrow302 = row3.createCell(1);
					cellrow302.setCellStyle(style);
					
					HSSFCell cellrow03 = row2.createCell(2);
					cellrow03.setCellStyle(style);
					
					HSSFCell cellrow303 = row3.createCell(2);
					cellrow303.setCellStyle(style);
					
					HSSFCell cellrow04 = row2.createCell(3);
					cellrow04.setCellStyle(style);
					
					HSSFCell cellrow304 = row3.createCell(3);
					cellrow304.setCellStyle(style);
					
					HSSFCell cellrow05 = row2.createCell(4);
					cellrow05.setCellStyle(style);
					
					HSSFCell cellrow305 = row3.createCell(4);
					cellrow305.setCellStyle(style);
					
					HSSFCell cellrow06 = row2.createCell(5);
					cellrow06.setCellStyle(style);
					
					HSSFCell cellrow306 = row3.createCell(5);
					cellrow306.setCellStyle(style);
					
					cellrow01.setCellValue("No."); 
					cellrow02.setCellValue("Driver"); 
					cellrow03.setCellValue("Start Time");
					cellrow04.setCellValue("End Time");
					cellrow05.setCellValue("Total");
					cellrow06.setCellValue("Score");
					
					sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
					sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
					sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
					sheet.addMergedRegion(new CellRangeAddress(1, 2, 5, 5));
				 	
					int k=6;
				    for (int i = 0; i < list.size(); i++) {
				    	
				    	if(list.get(i).getDictName().startsWith("Sudden")){
				    		HSSFCell cellrowi = row2.createCell(k);
					    	cellrowi.setCellStyle(style);
					    	HSSFCell cellrow3i = row3.createCell(k);
					    	cellrow3i.setCellStyle(style);
				    		cellrowi.setCellValue(list.get(i).getDictName());
				    		sheet.addMergedRegion(new CellRangeAddress(1, 2, k, k));
				    		k++;
				    	}  
					}
				    
                   for (int i = 0; i < list.size(); i++) {
				    	
				    	if(!list.get(i).getDictName().startsWith("Sudden")){
				    		HSSFCell cellrowi = row2.createCell(k);
					    	cellrowi.setCellStyle(style);
					    	
					    	HSSFCell cellrowi1 = row2.createCell(k+1);
					    	cellrowi1.setCellStyle(style);
					    	
					    	HSSFCell cellrow3i = row3.createCell(k);
					    	cellrow3i.setCellStyle(style);
					    	
					    	HSSFCell cellrow3ik = row3.createCell(k+1);
					    	cellrow3ik.setCellStyle(style);
					    	
				    		cellrowi.setCellValue(list.get(i).getDictName());
				    		
				    		cellrow3i.setCellValue("times");
				    		cellrow3ik.setCellValue("Cont");
				    		sheet.addMergedRegion(new CellRangeAddress(1, 1, k, k+1));
				    		k = k+2;
				    	}  
					}
                   RepDailyVo dvo = (RepDailyVo)total[4];
                   for (int i = 0; i < data.size(); i++) {
                	     Object[] obj = (Object[])data.get(i);
                	     HSSFRow row5 = sheet.createRow(i+4);
                	     int y=0;
					     for (int j = 0; j < obj.length; j++) {
					    	
					    	 if(j == 2){
					    		 HSSFCell cellrow501 = row5.createCell(y);
						    	 cellrow501.setCellStyle(style);
						    	 cellrow501.setCellValue(total[2].toString()+" 00:00:00");
						    	 HSSFCell cellrow50y1 = row5.createCell(y+1);
						    	 cellrow50y1.setCellStyle(style);
						    	 cellrow50y1.setCellValue(total[3].toString()+" 23:59:59");
					    		 y= y+2;
					    	 } else {
					    		 HSSFCell cellrow501 = row5.createCell(y);
						    	 cellrow501.setCellStyle(style);
						    	
						    	 // 判断分数
						    	 if(j == 4){
						    		 if(!"null".equals(obj[j]+"")){
						    			  
						    			 if(!"d".equals(obj[j]+"")){
						    				 
						    				 Double dou = 0d;
						    				 Double d=Double.valueOf(obj[j].toString().replace("d",""));
						    				 if ("1".equals(dvo.getBehCont())){
													dou = 100-d;
											 }
						    				 if ("2".equals(dvo.getBehCont())){
													dou = 100-d/7;
											 }
											  if ("3".equals(dvo.getBehCont())){
													dou = 100-d/30;
											 }
											  if ("4".equals(dvo.getBehCont())){
													dou = 100-d/365;
											 }
											  if(dou<0){
													dou=0d;
											  }
												DecimalFormat df = new DecimalFormat("####.##");
						    				 cellrow501.setCellValue(df.format(dou));
						    			 } else {
						    				 cellrow501.setCellValue(100L);
						    			 }
						    			 
						    			 
						    		 } else {
						    			 cellrow501.setCellValue(100L);
						    		 }
						    		 
						    	 } else {
						    		 if(!"null".equals(obj[j]+"")){
							    		 
							    		 if(String.valueOf(obj[j]).startsWith("t")){
							    			 if(j>2){
							    				 cellrow501.setCellValue(getValue(String.valueOf(obj[j]).replace("t","")));
							    			 } else {
							    				 cellrow501.setCellValue(obj[j].toString());
							    			 }
								    		 
								    	 } else {
								    		 if("d".equals(obj[j]+"")){
								    			 if(j>2){
								    				 cellrow501.setCellValue("0"); 
								    			 } else {
								    				 cellrow501.setCellValue("");
								    			 }
								    			
								    		 } else {
								    			 if(j>2){
								    				 cellrow501.setCellValue(String.valueOf(obj[j]).replace("d", ""));
								    			 } else {
								    				 cellrow501.setCellValue(String.valueOf(obj[j]));
								    			 }
								    			 
								    		 }
								    	 }
							    		
							    	 } else {
							    		 if(j>2){
							    			 cellrow501.setCellValue("0");
							    		 } else {
							    			 cellrow501.setCellValue("");
							    		 }
							    		
							    	 }
						    	 }
						    	
					    	   y++;
					    	 }
					    	 
						}
				   }
					 
					return wb;
				}
				
			};
		
			Object[] total = new Object[5];
			total[0] = listd;
			total[1] = Utils.getSession().getAttribute("count");
			total[2] = obj[1];
			total[3] = obj[2];
			total[4] = obj[3];
			String str="Report Query";
			return ee.export(total, data, str);
	}
	
//	public String getDitName(Long id, Long userID){
//		List<KeyAndValue> list = baseMgr.getFleetByAuthority(userID);
//		String str = "";
//		for (int i = 0; i < list.size(); i++) {
//			if(id == Long.valueOf(list.get(i).getKey())){
//				str = list.get(i).getValue();
//				break;
//			}
//		}
//		return str;
//	}

	public String getValue(String value)  {
        String idsStr = "";
		if (value == null || value.equals("")) {
			idsStr = "00:00:00" ;
		} else {
			Long sec = Long.valueOf(value); // 秒数
			Long h = sec / 3600L; // 时
			Long m = (sec % 3600L) / 60L; // 分
			Long s = sec % 60L; // 秒
			String hh = h < 10 ? ("0" + h) : String.valueOf(h); // 补位
			String mm = m < 10 ? ("0" + m) : String.valueOf(m);
			String ss = s < 10 ? ("0" + s) : String.valueOf(s);
			 
			idsStr = hh + ":" + mm + ":" + ss;
		}
      return idsStr;
	}
	 
	public Double changfloat(Double f){
 
		BigDecimal   b   =   new   BigDecimal(f);  
		return b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 根据月份获得这一月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getLastDayOfMonth(String date) {
		Date date_begin = DateUtil.parse(date + "-01");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date_begin);
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date lastDate = calendar.getTime();
		lastDate.setDate(lastDay);
		return DateUtil.formatDate(lastDate);
	}
}
