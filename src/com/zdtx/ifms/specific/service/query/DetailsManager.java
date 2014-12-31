package com.zdtx.ifms.specific.service.query;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.service.BaseManager;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.ExportExcel;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.KeyAndValues;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.base.Dict;
import com.zdtx.ifms.specific.model.query.Details;
import com.zdtx.ifms.specific.model.system.Score;
import com.zdtx.ifms.specific.vo.query.DetailsVo;

/**
 * @ClassName: DetailsManager
 * @Description: 不规范驾驶报告-不规范驾驶明细-业务层
 * @author JHQ
 * @date 2012年11月28日 17:06:53
 * @version V1.0
 */

@Service
@Transactional
public class DetailsManager {
	@Autowired
	public BaseDao baseDao;
	@Autowired
	public BaseManager baseMgr;

	@SuppressWarnings("unchecked")
	public List<KeyAndValues> getDridet(Long driverid, String typestr,
			DetailsVo dvo) {
		List<KeyAndValues> list = new ArrayList<KeyAndValues>();
		String ge = "";
		String le = "";
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
		}
		if ("1".equals(dvo.getBehCont())) {
			String hql = "From Details where driver_id = " + driverid
					+ " and upper(beh_name) = upper('" + typestr + "') ";
			hql += " and riqi>='" + ge + "' and riqi<='" + le + "' order by timeBegin";
			List<Details> detlist = baseDao.execute(hql);
			for (int i = 0; i < detlist.size(); i++) {
				Details d = detlist.get(i);
				KeyAndValues ky = new KeyAndValues();
				ky.setKey(d.getTimeBegin());
				ky.setValue(d.getTimeEnd());
				ky.setValue1(d.getTimeCont());
				ky.setValue2(d.getVehSpeed() + "");
				list.add(ky);
			}
		}
		if ("2".equals(dvo.getBehCont()) || "3".equals(dvo.getBehCont())) {
			String sql = "select riqi,beh_count,score,VEH_TYPEID from T_BEH_NOT_DAY where ";
			sql += " driver_id = " + driverid;
			sql += " and riqi>='" + ge + "' and riqi<='" + le + "'";
			sql += " and upper(beh_name) =upper('" + typestr
					+ "') order by riqi";
			SQLQuery query = baseDao.getSession().createSQLQuery(sql);
			List<List<String>> list1 = query.list();
			String scoreall = "no set";
			if (list1.size() > 0) {
				Object[] os = (Object[]) (Object) list1.get(0);
				if (os[3] != null) {
					List<Score> ls = getScores(Long.parseLong(os[3] + ""),
							typestr);
					if (ls.size() > 0) {
						Score s = ls.get(0);
						scoreall = s.getWeight() + "";
					}
				}
			}
			for (int i = 0; i < list1.size(); i++) {
				Object[] os = (Object[]) (Object) list1.get(i);
				KeyAndValues ky = new KeyAndValues();
				ky.setKey(os[0] + "");
				ky.setValue(os[1] + "");
				Double d = 0d;
				if (os[2] != null) {
					d = Double.parseDouble(os[2] + "");
					if (scoreall.length() < 6 && !"".equals(scoreall)) {
						DecimalFormat df = new DecimalFormat("###.##");
						if (Double.parseDouble(scoreall) > d) {
							ky.setValue1(df.format(Double.parseDouble(scoreall)
									- d));
						} else {
							ky.setValue1("0");
						}
					}
				} else {
					ky.setValue1(scoreall);
				}
				ky.setValue2(scoreall);
				list.add(ky);
			}
		}
		if ("4".equals(dvo.getBehCont())) {
			String sql = "select substr(riqi,1,7),sum(beh_count),sum(score),max(VEH_TYPEID) from T_BEH_NOT_DAY where ";
			sql += " driver_id = " + driverid;
			sql += " and riqi>='" + ge + "' and riqi<='" + le + "'";
			sql += " and upper(beh_name) =upper('" + typestr
					+ "') group by substr(riqi,1,7) order by substr(riqi,1,7)";
			SQLQuery query = baseDao.getSession().createSQLQuery(sql);
			List<List<String>> list1 = query.list();
			String scoreall = "no set";
			if (list1.size() > 0) {
				Object[] os = (Object[]) (Object) list1.get(0);
				if (os[3] != null) {
					List<Score> ls = getScores(Long.parseLong(os[3] + ""),
							typestr);
					if (ls.size() > 0) {
						Score s = ls.get(0);
						scoreall = s.getWeight() + "";
					}
				}
			}
			for (int i = 0; i < list1.size(); i++) {
				Object[] os = (Object[]) (Object) list1.get(i);
				KeyAndValues ky = new KeyAndValues();
				ky.setKey(os[0] + "");
				ky.setValue(os[1] + "");
				Double d = 0d;
				if (os[2] != null) {
					d = Double.parseDouble(os[2] + "") / 30;
					if (scoreall.length() < 6 && !"".equals(scoreall)) {
						DecimalFormat df = new DecimalFormat("###.##");
						if (Double.parseDouble(scoreall) > d) {
							ky.setValue1(df.format(Double.parseDouble(scoreall)
									- d));
						} else {
							ky.setValue1("0");
						}
					}
				} else {
					ky.setValue1(scoreall);
				}
				ky.setValue2(scoreall);
				list.add(ky);
			}
		}
		return list;
	}

	public Page<List<String>> getAll(Page<List<String>> page, DetailsVo dvo) {
		List<Dict> list = this.getDict();
		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		String sql = "";
		sql = "select * from ( select rownum rownum_,row_.*  from (select max(dept_name),max(riqi),sum(beh_count),sum(score),count(distinct driver_id),";
		String sql1 = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sql1 += "nvl(sum(decode(beh_type, '" + list.get(i).getDictID()
						+ "', beh_count)),0) a" + list.get(i).getDictID()
						+ " ,";
			}
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		sql = sql
				+ sql1
				+ " from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";
		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and dept_id = " + dvo.getOrgId();
		}

		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}
		sql = sql + " group by dept_id ";
		sql += " order by dept_id ASC) row_ where rownum <= "
				+ (page.getPageSize() + page.getFirst())
				+ " ) where rownum_ > " + page.getFirst();
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<List<String>> list1 = query.list();
		page.setResult(list1);
		page.setTotalCount(getCount(dvo));
		Utils.getSession().setAttribute("page", page);
		Utils.getSession().setAttribute("ge", ge);
		Utils.getSession().setAttribute("le", le);
		return page;
	}

	public Integer getCount(DetailsVo dvo) {
		String sql = "select count(1) from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";

		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and dept_id = " + dvo.getOrgId();
		}

		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}
		sql = sql + " group by dept_id";

		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		return query.list().size();
	}

	public Page<List<String>> getAllDri_(Page<List<String>> page, DetailsVo dvo) {
		Long[] dictids = { 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L };
		List<Dict> list = this.getDict(dictids);
		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = DateUtil.dateToWeek(dvo.getDate2())[0];
			le = DateUtil.dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = DateUtil.getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		if ("5".equals(dvo.getBehCont())) {
			ge = dvo.getDate1();
			le = dvo.getDate2();
			flag = true;
		}
		String sql = "";
		sql = "select * from ( select rownum rownum_,row_.*  from (select max(dept_name),max(driver_name),max(riqi),sum(beh_count),sum(score),";
		String sql1 = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sql1 += "nvl(sum(decode(beh_type, '" + list.get(i).getDictID()
						+ "', beh_count)),0) a" + list.get(i).getDictID()
						+ " ,";
			}
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		sql = sql
				+ sql1
				+ " from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";
		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and dept_id = " + dvo.getOrgId();
		}
		if (null != dvo.getDriverId() && -1L != dvo.getDriverId()) {
			sql = sql + " and driver_id = " + dvo.getDriverId();
		}

		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}
		sql = sql + " group by driver_id ";
		sql += " order by nvl(sum(score),0) DESC,max(driver_name)) row_ where rownum <= "
				+ (page.getPageSize() + page.getFirst())
				+ " ) where rownum_ > " + page.getFirst();

		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<List<String>> list1 = query.list();
		page.setResult(list1);
		page.setTotalCount(getCountDri(dvo));
		Utils.getSession().setAttribute("page", page);
		Utils.getSession().setAttribute("ge", ge);
		Utils.getSession().setAttribute("le", le);
		return page;
	}

	public List<List<String>> getAllDriShow(Long driverid, DetailsVo dvo) {
		List<Dict> list = this.getDict();
		String ge = "";
		String le = "";
		if ("1".equals(dvo.getBehCont())) {
			ge = dvo.getRiqi();
			le = dvo.getRiqi();
		}
		if ("2".equals(dvo.getBehCont())) {
			ge = DateUtil.dateToWeek(dvo.getRiqi())[0];
			le = DateUtil.dateToWeek(dvo.getRiqi())[1];
		}
		if ("3".equals(dvo.getBehCont())) {
			String mont = dvo.getRiqi().substring(0, 7);
			ge = mont + "-01";
			le = DateUtil.getLastDayOfMonth(mont);
		}
		if ("4".equals(dvo.getBehCont())) {
			String mont = dvo.getRiqi().substring(0, 4);
			ge = mont + "-01-01";
			le = mont + "-12-31";
		}
		String sql = "";
		sql = "select max(driver_name),max(riqi),nvl(sum(beh_count),0),nvl(sum(score),0),driver_id,max(VEH_TYPEID),";
		String sql1 = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sql1 += "nvl(sum(decode(beh_type, '" + list.get(i).getDictID()
						+ "', beh_count)),0) a" + list.get(i).getDictID()
						+ " ,";
				sql1 += "nvl(sum(decode(beh_type, '" + list.get(i).getDictID()
						+ "', SCORE)),0) b" + list.get(i).getDictID() + " ,";
			}
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		sql = sql
				+ sql1
				+ " from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";
		sql = sql + " and driver_id = " + driverid;
		sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		sql = sql + " group by driver_id ";
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<List<String>> list1 = query.list();
		return list1;
	}

	public Page<List<String>> getAllDri(Page<List<String>> page, DetailsVo dvo) {
		List<Dict> list = this.getDict();
		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		if ("5".equals(dvo.getBehCont())) {
			ge = dvo.getDate1();
			le = dvo.getDate2();
			flag = true;
		}
		String sql = "";
		sql = "select * from ( select rownum rownum_,row_.*  from (select max(dept_name),max(driver_name),max(vehcode),max(riqi),sum(beh_count),sum(score),driver_id,";
		String sql1 = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sql1 += "nvl(sum(decode(beh_type, '" + list.get(i).getDictID()
						+ "', beh_count)),0) a" + list.get(i).getDictID()
						+ " ,";
			}
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		sql = sql
				+ sql1
				+ " from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";
		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and dept_id = " + dvo.getOrgId();
		}
		if (null != dvo.getDriverId() && -1L != dvo.getDriverId()) {
			sql = sql + " and driver_id = " + dvo.getDriverId();
		}

		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}
		sql = sql + " group by driver_id ";
		sql += " order by nvl(sum(score),0) DESC,max(driver_name)) row_ where rownum <= "
				+ (page.getPageSize() + page.getFirst())
				+ " ) where rownum_ > " + page.getFirst();
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<List<String>> list1 = query.list();
		page.setResult(list1);
		page.setTotalCount(getCountDri(dvo));
		Utils.getSession().setAttribute("page", page);
		Utils.getSession().setAttribute("ge", ge);
		Utils.getSession().setAttribute("le", le);
		return page;
	}

	public Integer getCountDri(DetailsVo dvo) {
		String sql = "select count(1) from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";

		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and dept_id = " + dvo.getOrgId();
		}
		if (null != dvo.getDriverId() && -1L != dvo.getDriverId()) {
			sql = sql + " and driver_id = " + dvo.getDriverId();
		}
		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}
		sql = sql + " group by driver_id";
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		return query.list().size();
	}

	public Page<List<String>> getDriAll(Page<List<String>> page, DetailsVo dvo) {
		List<Dict> list = this.getDict();
		String sql = "";
		sql = "select * from ( select rownum rownum_,row_.*  from (select max(driver_name),max(DEPT_NAME),max(riqi),sum(beh_count),sum(score),";
		String sql1 = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sql1 += "nvl(sum(decode(beh_type, '" + list.get(i).getDictID()
						+ "', beh_count)),0) a" + list.get(i).getDictID()
						+ " ,";
			}
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		sql = sql
				+ sql1
				+ " from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";
		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and dept_id = " + dvo.getOrgId();
		}
		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}
		sql = sql + " group by driver_id ";
		sql += " order by nvl(sum(score),0) DESC,max(driver_name)) row_ where rownum <= "
				+ (page.getPageSize() + page.getFirst())
				+ " ) where rownum_ > " + page.getFirst();

		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<List<String>> list1 = query.list();
		page.setResult(list1);
		page.setTotalCount(getDriCount(dvo));
		Utils.getSession().setAttribute("page", page);
		Utils.getSession().setAttribute("ge", ge);
		Utils.getSession().setAttribute("le", le);
		return page;
	}

	public List<List<String>> getAll(DetailsVo dvo, boolean b) {
		Long[] dictids = { 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L };
		List<Dict> list = new ArrayList<Dict>();
		if (b) {
			list = this.getDict(dictids);
		} else {
			list = this.getDict();
		}
		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		if ("5".equals(dvo.getBehCont())) {
			ge = dvo.getDate1();
			le = dvo.getDate2();
			flag = true;
		}
		String sql = "";
		sql = "select nvl(sum(beh_count),0),nvl(sum(score),0),";
		String sql1 = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sql1 += "nvl(sum(decode(beh_type, '" + list.get(i).getDictID()
						+ "', beh_count)),0) a" + list.get(i).getDictID()
						+ " ,";
			}
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		sql = sql
				+ sql1
				+ " from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";
		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and dept_id = " + dvo.getOrgId();
		}
		if (null != dvo.getDriverId() && -1L != dvo.getDriverId()) {
			sql = sql + " and driver_id = " + dvo.getDriverId();
		}
		if (null != dvo.getVehcode() && !"".equals(dvo.getVehcode())) {
			sql = sql + " and driver_name = '" + dvo.getVehcode() + "'";
		}

		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<List<String>> list1 = query.list();
		return list1;
	}

	public Integer getDriCount(DetailsVo dvo) {
		String sql = "select count(1) from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";
		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and dept_id = " + dvo.getOrgId();
		}
		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}

		sql = sql + " group by driver_id";

		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		return query.list().size();
	}

	public Page<List<String>> getRouAll(Page<List<String>> page, DetailsVo dvo) {
		List<Dict> list = this.getDict();
		String sql = "";
		sql = "select * from ( select rownum rownum_,row_.*  from (select max(GEO_CAPTION),max(riqi),sum(beh_count),sum(score),";
		String sql1 = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sql1 += "nvl(sum(decode(beh_type, '" + list.get(i).getDictID()
						+ "', beh_count)),0) a" + list.get(i).getDictID()
						+ " ,";
			}
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		sql = sql
				+ sql1
				+ " from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") and GEO_ID is not null";
		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and GEO_ID = " + dvo.getOrgId();
		}

		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}
		sql = sql + " group by GEO_ID,GEO_CAPTION ";
		sql += " order by nvl(sum(score),0) DESC,max(GEO_CAPTION)) row_ where rownum <= "
				+ (page.getPageSize() + page.getFirst())
				+ " ) where rownum_ > " + page.getFirst();

		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<List<String>> list1 = query.list();
		page.setResult(list1);
		page.setTotalCount(getRouCount(dvo));
		Utils.getSession().setAttribute("page", page);
		Utils.getSession().setAttribute("ge", ge);
		Utils.getSession().setAttribute("le", le);
		return page;
	}

	public Integer getRouCount(DetailsVo dvo) {
		String sql = "select count(1) from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";
		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and GEO_ID = " + dvo.getOrgId();
		}

		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}

		sql = sql + " group by GEO_ID";

		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		return query.list().size();
	}

	public Page<List<String>> getDrirouAll(Page<List<String>> page,
			DetailsVo dvo) {
		List<Dict> list = this.getDict();
		String sql = "";
		sql = "select * from ( select rownum rownum_,row_.*  from (select max(driver_name),max(GEO_CAPTION),max(riqi),sum(beh_count),sum(score),";
		String sql1 = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				sql1 += "nvl(sum(decode(beh_type, '" + list.get(i).getDictID()
						+ "', beh_count)),0) a" + list.get(i).getDictID()
						+ " ,";
			}
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		sql = sql
				+ sql1
				+ " from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";
		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and GEO_ID = " + dvo.getOrgId();
		}

		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}
		sql = sql + " group by driver_id,driver_name ";
		sql += " order by nvl(sum(score),0) DESC,max(driver_name)) row_ where rownum <= "
				+ (page.getPageSize() + page.getFirst())
				+ " ) where rownum_ > " + page.getFirst();

		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<List<String>> list1 = query.list();
		page.setResult(list1);
		page.setTotalCount(getDrirouCount(dvo));
		Utils.getSession().setAttribute("page", page);
		Utils.getSession().setAttribute("ge", ge);
		Utils.getSession().setAttribute("le", le);
		return page;
	}

	public Integer getDrirouCount(DetailsVo dvo) {
		String sql = "select count(1) from T_BEH_NOT_DAY where dept_id in ("
				+ (String) Struts2Util.getSession().getAttribute(
						"userDepartmentStr") + ") ";

		if (null != dvo.getOrgId() && -1L != dvo.getOrgId()) {
			sql = sql + " and GEO_ID = " + dvo.getOrgId();
		}

		String ge = "";
		String le = "";
		boolean flag = false;
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null
				&& !"".equals(dvo.getDate1())) {
			ge = dvo.getDate1();
			le = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null
				&& !"".equals(dvo.getDate2())) {
			ge = dateToWeek(dvo.getDate2())[0];
			le = dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null
				&& !"".equals(dvo.getDate3())) {
			ge = dvo.getDate3() + "-01";
			le = getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null
				&& !"".equals(dvo.getDate4())) {
			ge = dvo.getDate4().split("-")[0] + "-01-01";
			le = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		if (flag) {
			sql = sql + " and riqi>='" + ge + "' and riqi<='" + le + "'";
		}

		sql = sql + " group by driver_id";

		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		return query.list().size();
	}

	public List<Dict> getDict(Long[] dictids) {
		Criteria criteria = baseDao.getSession().createCriteria(Dict.class);
		criteria.add(Restrictions.eq("categoryID", 201000L));
		criteria.add(Restrictions.eq("isDelete", "F"));
		criteria.add(Restrictions.in("dictID", dictids));
		criteria.addOrder(Order.asc("dictID"));
		return baseDao.getAll(criteria);
	}

	public List<Dict> getDict() {
		Criteria criteria = baseDao.getSession().createCriteria(Dict.class);
		criteria.add(Restrictions.eq("categoryID", 201000L));
		criteria.add(Restrictions.eq("isDelete", "F"));
		criteria.addOrder(Order.asc("dictID"));
		return baseDao.getAll(criteria);
	}

	public Page<Details> getBetch(Page<Details> page, DetailsVo dvo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Details.class);
		List<Order> orderList = new ArrayList<Order>();
		try {

			criteria.add(Restrictions.in("dept_id", (Long[]) Struts2Util
					.getSession().getAttribute("userDepartment")));
			if (!Utils.isEmpty(dvo.getVehcode())) { // 车牌号
				criteria.add(Restrictions.like("vehcode",
						"%" + dvo.getVehcode() + "%"));
			}
			if (!Utils.isEmpty(dvo.getRiqi())) { // 日期
				criteria.add(Restrictions.eq("riqi", dvo.getRiqi()));
			}
			if (!Utils.isEmpty(dvo.getTimeBegin())) { // 日期
				criteria.add(Restrictions.ge("timeBegin", dvo.getTimeBegin().split(" ")[1]));
			}
			if (!Utils.isEmpty(dvo.getTimeEnd())) { // 日期
				criteria.add(Restrictions.le("timeEnd", dvo.getTimeEnd().split(" ")[1]));
			}
			if (!Utils.isEmpty(dvo.getBehType()) && -1 != dvo.getBehType()) { // 事件类型
				criteria.add(Restrictions.eq("behType", dvo.getBehType()));
			}
			if (!Utils.isEmpty(dvo.getOrgId()) && -1 != dvo.getOrgId()) { // 车队
				criteria.add(Restrictions.eq("orgId", dvo.getOrgId()));
			}
			if (!Utils.isEmpty(dvo.getDeptId()) && -1 != dvo.getDeptId()) { // 车队
				criteria.add(Restrictions.eq("dept_id", dvo.getDeptId()));
			}
			if (!Utils.isEmpty(dvo.getVehId()) && -1 != dvo.getVehId()) { // 车队
				criteria.add(Restrictions.eq("vehId", dvo.getVehId()));
			}
			if (!Utils.isEmpty(dvo.getDriverId()) && -1 != dvo.getDriverId()) { // 车队
				criteria.add(Restrictions.eq("driver_id", dvo.getDriverId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		orderList.add(Order.desc("riqi"));
		orderList.add(Order.desc("timeBegin"));
		orderList.add(Order.asc("orgId"));
		orderList.add(Order.asc("vehcode"));
		Page<Details> pageResult = baseDao
				.getBatch(page,
						criteria.getExecutableCriteria(baseDao.getSession()),
						orderList);
		if (1 == pageResult.getCurrentPage()) {
			Utils.getSession().setAttribute("criteria_export", criteria);
			Utils.getSession().setAttribute("page_export", page);
			Utils.getSession().setAttribute("orderList_export", orderList);
		}
		return pageResult;
	}

	/**
	 * 生成-不规范驾驶明细
	 * 
	 * @param faultVo
	 * @param lines
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public InputStream getData(List<Details> list) {

		List<Object[]> data = new ArrayList<Object[]>();

		Page<Object[]> page_export = (Page<Object[]>) Utils.getSession()
				.getAttribute("page_export");
		DetachedCriteria criteria_export = (DetachedCriteria) Utils
				.getSession().getAttribute("criteria_export");
		List<Order> orderList_export = (List<Order>) Utils.getSession()
				.getAttribute("orderList_export");

		page_export.setPageSize(page_export.getTotalCount());
		Page<Object[]> pageResult = baseDao.getBatch(page_export,
				criteria_export.getExecutableCriteria(baseDao.getSession()),
				orderList_export);

		if (null != pageResult) {
			if (0 != pageResult.getResult().size()) {
				data = pageResult.getResult();

			}
		}

		ExportExcel ee = new ExportExcel() {

			@Override
			protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] total,
					List<?> data) throws IOException {
				HSSFSheet sheet = wb.getSheetAt(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
				HSSFRow rowss = sheet.createRow(0);
				rowss.setHeightInPoints(20);
				HSSFCell hssfCell = rowss.createCell(0);
				hssfCell = this.createCell(wb, hssfCell, "Event List");
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
				HSSFCell cellrow09 = row2.createCell(8);
				cellrow09.setCellStyle(style);
				HSSFCell cellrow10 = row2.createCell(9);
				cellrow10.setCellStyle(style);
				HSSFCell cellrow11 = row2.createCell(10);
				cellrow11.setCellStyle(style);
				HSSFCell cellrow12 = row2.createCell(11);
				cellrow12.setCellStyle(style);
				HSSFCell cellrow13 = row2.createCell(12);
				cellrow13.setCellStyle(style);
				HSSFCell cellrow14 = row2.createCell(13);
				cellrow14.setCellStyle(style);
				HSSFCell cellrow15 = row2.createCell(14);
				cellrow15.setCellStyle(style);

				cellrow01.setCellValue("No.");
				cellrow02.setCellValue("Driver");
				cellrow03.setCellValue("Vehicle Name");
				cellrow04.setCellValue("Plate Number");
				cellrow05.setCellValue("Department");
				cellrow06.setCellValue("Fleet");
				cellrow07.setCellValue("Geo");
				cellrow08.setCellValue("Date");
				cellrow09.setCellValue("Start Time");
				cellrow10.setCellValue("End Time");
				cellrow11.setCellValue("Continuance");
				cellrow12.setCellValue("Type");
				cellrow13.setCellValue("Speed");
				cellrow14.setCellValue("Lng");
				cellrow15.setCellValue("Lat");

				if (null != data && 0 != data.size()) {
					for (int i = 0; i < data.size(); i++) {
						HSSFRow row = sheet.createRow(i + 2);
						Details o = (Details) data.get(i);
						HSSFCell cell001 = row.createCell(0);
						cell001.setCellStyle(style);
						cell001.setCellValue(i + 1);
						HSSFCell cell002 = row.createCell(1);
						cell002.setCellStyle(style);
						cell002.setCellValue(o.getDriver_name().toString());
						HSSFCell cell003 = row.createCell(2);
						cell003.setCellStyle(style);
						cell003.setCellValue(o.getVehno().toString());
						HSSFCell cell004 = row.createCell(3);
						cell004.setCellStyle(style);
						cell004.setCellValue(o.getVehcode().toString());
						HSSFCell cell005 = row.createCell(4);
						cell005.setCellStyle(style);
						cell005.setCellValue(o.getDept_name().toString());
						HSSFCell cell006 = row.createCell(5);
						cell006.setCellStyle(style);
						cell006.setCellValue(o.getOrgName().toString());
						HSSFCell cell007 = row.createCell(6);
						cell007.setCellStyle(style);
						cell007.setCellValue(o.getGeo_caption().toString());
						HSSFCell cell008 = row.createCell(7);
						cell008.setCellStyle(style);
						cell008.setCellValue(o.getRiqi().toString());
						HSSFCell cell009 = row.createCell(8);
						cell009.setCellStyle(style);
						cell009.setCellValue(o.getTimeBegin().toString());
						HSSFCell cell010 = row.createCell(9);
						cell010.setCellStyle(style);
						cell010.setCellValue(o.getTimeEnd().toString());
						HSSFCell cell011 = row.createCell(10);
						cell011.setCellStyle(style);
						cell011.setCellValue(getValue(o.getTimeCont()));
						HSSFCell cell012 = row.createCell(11);
						cell012.setCellStyle(style);
						cell012.setCellValue(o.getBeh_name().toString());
						HSSFCell cell013 = row.createCell(12);
						cell013.setCellStyle(style);
						cell013.setCellValue(o.getVehSpeed().toString());
						HSSFCell cell014 = row.createCell(13);
						cell014.setCellStyle(style);
						cell014.setCellValue(o.getLongitude().toString());
						HSSFCell cell015 = row.createCell(14);
						cell015.setCellStyle(style);
						cell015.setCellValue(o.getLatitude().toString());
					}
				}
				return wb;
			}

		};

		Object[] total = new Object[1];
		total[0] = "";
		String str = "Event List";
		return ee.export(total, data, str);
	}

	public String getValue(String value) {
		String idsStr = "";
		if (value == null || value.equals("")) {
			idsStr = "00:00:00";
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

	public List<KeyAndValue> getCoeList(Long categoryID) {
		String sql = "SELECT DICT_ID AS KEY, DICTNAME AS VALUE FROM T_CORE_DICT WHERE ISDELETE = 'F' AND SORT_ID = "
				+ categoryID + " ORDER BY DICT_ID ASC";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	public String getTypeValue(String id) {
		String value = "";
		String sql = "SELECT DICT_ID AS KEY, DICTNAME AS VALUE FROM T_CORE_DICT WHERE ISDELETE = 'F' AND SORT_ID = 201000";
		List<KeyAndValue> list = baseDao.getKeyAndValueBySQL(sql);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getKey().equals(id))
				value = list.get(i).getValue();
		}
		return value;
	}

	@SuppressWarnings("deprecation")
	private String[] dateToWeek(String date) {
		String[] date_week = new String[2];
		Date date_all = DateUtil.parse(date);
		int date_length = date_all.getDay();
		Long fTime = date_all.getTime() - date_length * 24 * 3600000;
		for (int a = 1; a <= 7; a++) {
			date_all = new Date();
			date_all.setTime(fTime + (a * 24 * 3600000));
			if (a == 1) {
				date_week[0] = DateUtil.formatDate(date_all);
			}
			if (a == 7) {
				date_week[1] = DateUtil.formatDate(date_all);
			}
		}
		return date_week;
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

	public List<Score> getScores(String vehtype) {
		String hql = "From Score where vehicletypeid = "
				+ Long.parseLong(vehtype) + " order by typeid";
		return baseDao.execute(hql);
	}

	public List<Score> getScores(Long vehtype, String type) {
		String hql = "From Score where upper(type) = upper('" + type
				+ "') and vehicletypeid = " + vehtype + " order by typeid";
		return baseDao.execute(hql);
	}

	@SuppressWarnings("unchecked")
	public InputStream getTrip(DetailsVo dvo) {
		List<Details> data = new ArrayList<Details>();
		Page<Details> page_export = (Page<Details>) Utils.getSession()
				.getAttribute("page_export");
		DetachedCriteria criteria_export = (DetachedCriteria) Utils
				.getSession().getAttribute("criteria_export");
		List<Order> orderList_export = (List<Order>) Utils.getSession()
				.getAttribute("orderList_export");
		page_export.setPageSize(page_export.getTotalCount());
		Page<Details> pageResult = baseDao.getBatch(page_export,
				criteria_export.getExecutableCriteria(baseDao.getSession()),
				orderList_export);
		if (null != pageResult) {
			data = pageResult.getResult();
		}
		ExportExcel ee = new ExportExcel() {
			@Override
			protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] obj,
					List<?> data) throws IOException {
				HSSFSheet sheet = wb.getSheetAt(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 13));
				HSSFRow rowss = sheet.createRow(0);
				rowss.setHeightInPoints(20);
				HSSFCell hssfCell = rowss.createCell(0);
				hssfCell = this.createCell(wb, hssfCell, "Report Trip");
				HSSFCellStyle style = this.createStyle(wb);
				List<Details> list = (List<Details>) data;
				for (int i = 0; i < list.size(); i++) {
					Details r = list.get(i);
					HSSFRow row = sheet.createRow(i + 2);
					HSSFCell cell = row.createCell(0);
					cell.setCellStyle(style);
					cell.setCellValue(i + 1);
					cell = row.createCell(1);
					cell.setCellStyle(style);
					cell.setCellValue(r.getDriver_name());
					cell = row.createCell(2);// vehicle
					cell.setCellStyle(style);
					cell.setCellValue(r.getVehcode());
					cell = row.createCell(3);
					cell.setCellStyle(style);
					cell.setCellValue(r.getDept_name());
					cell = row.createCell(4);// date
					cell.setCellStyle(style);
					cell.setCellValue(r.getOrgName());
					cell = row.createCell(5);// time start
					cell.setCellStyle(style);
					cell.setCellValue(r.getGeo_caption());
					cell = row.createCell(6);// time end
					cell.setCellStyle(style);
					cell.setCellValue(r.getRiqi());
					cell = row.createCell(7);// Period_run
					cell.setCellStyle(style);
					cell.setCellValue(r.getTimeBegin());
					cell = row.createCell(8);// Period_drive
					cell.setCellStyle(style);
					cell.setCellValue(r.getTimeEnd());
					cell = row.createCell(9);// Period_idle
					cell.setCellStyle(style);
					cell.setCellValue(DateUtil.formatHmsDate(r.getTimeCont()));
					cell = row.createCell(10);// Period_air
					cell.setCellStyle(style);
					cell.setCellValue(r.getBeh_name());
					cell = row.createCell(11);// Period_hot
					cell.setCellStyle(style);
					cell.setCellValue(r.getVehSpeed());
					cell = row.createCell(12);// Km_run
					cell.setCellStyle(style);
					cell.setCellValue(r.getLongitude());
					cell = row.createCell(13);// Km_run
					cell.setCellStyle(style);
					cell.setCellValue(r.getLatitude());
				}
				return wb;
			}
		};
		Object[] total = new Object[1];
		total[0] = "Report Trip";
		return ee.export(total, data, "", "No.", "Driver", "Plate Number",
				"Department", "Fleet", "Geo", "Date", "Start Time", "End Time",
				"Continuance", "Type", "Speed", "Lng", "Lat");
	}

	/**
	 * redep导出
	 * 
	 * @param faultVo
	 * @param lines
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public InputStream getRepDeptExcel() {
		List<KeyAndValue> violationList = (List<KeyAndValue>)this.getCoeList(201000L);
		int violationCount = violationList.size();
		Page<Object[]> page_export = (Page<Object[]>) Utils.getSession().getAttribute("page");
		final String startDate = Utils.getSession().getAttribute("ge") + "";
		final String endDate = Utils.getSession().getAttribute("le") + "";
		List<Object[]> chartData = page_export.getResult();
		DetailsVo vo = (DetailsVo) Utils.getSession().getAttribute("dvo");
		final String dateType = vo.getBehCont();
		DecimalFormat format = new DecimalFormat("####.##");
		
		OPCPackage opcPackage = null;
		XSSFWorkbook workbook = null;
		InputStream fis = this.getClass().getResourceAsStream("/../../excel-temp/temp_reportDepartment.xlsx");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			opcPackage = OPCPackage.open(fis);		//read temp stream can prevent the temp file from be modified
			workbook = new XSSFWorkbook(opcPackage);
			XSSFSheet sheet = workbook.getSheetAt(0);
			//title row
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5 + violationCount));
			//navigation row
			XSSFRow row = sheet.getRow(1);
			row.getCell(2).setCellValue(vo.getOrgName());	//set department
			if ("1".equals(dateType)) {
				row.getCell(4).setCellValue("Daily");	//set Stat Type
				row.getCell(7).setCellValue(vo.getDate1());	//set date
			}
			if ("2".equals(dateType)) {
				row.getCell(4).setCellValue("Weekly");	//set Stat Type
				row.getCell(7).setCellValue(vo.getDate2());	//set date
			}
			if ("3".equals(dateType)) {
				row.getCell(4).setCellValue("Monthly");	//set Stat Type
				row.getCell(7).setCellValue(vo.getDate3());	//set date
			}
			if ("4".equals(dateType)) {
				row.getCell(4).setCellValue("Yearly");	//set Stat Type
				row.getCell(7).setCellValue(vo.getDate4());	//set date
			}
			//grid's header row
			row = sheet.getRow(2);	
			XSSFCell cell = null;
			CellStyle cellStyle = row.getCell(0).getCellStyle();	//load header row's cell style
			cellStyle.setWrapText(true);	//set wrap
			for (int i = 0; i < violationCount; i++) {	//add violations columns
				cell = row.createCell(i + 6);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(violationList.get(i).getValue());
			}
			//grid's body row
			//first row
			row = sheet.getRow(3);	
			cellStyle = ExportExcel.createXNormalFont(workbook);	//load body row's normal cell style
			CellStyle redCellStyle = ExportExcel.createXRedNormalFont(workbook);		//load body row's red font cell style
			Object[] obj = chartData.get(0);	//first data
			row.getCell(1).setCellValue(obj[1] + "");
			row.getCell(2).setCellValue(startDate + " " + "00:00:00");
			row.getCell(3).setCellValue(endDate + " " + "23:59:59");
			row.getCell(4).setCellValue(Utils.isEmpty(obj[3]) ? 0 : Integer.valueOf(obj[3] + ""));
			double totalScore = (obj[4] != null ? Double.parseDouble(obj[4] + "") : 0d);
			int count = Integer.parseInt(obj[5] + "");
			double score = 0d;
			if ("1".equals(dateType)) {
				score = 100 - totalScore / count;
			}
			if ("2".equals(dateType)) {
				score = 100 - totalScore / count / 7;
			}
			if ("3".equals(dateType)) {
				score = 100 - totalScore / count / 30;
			}
			if ("4".equals(dateType)) {
				score = 100 - totalScore / count / 365;
			}
			if (score < 0) {
				score = 0d;
			}
			score = Double.valueOf(format.format(score));
			row.getCell(5).setCellValue(score);
			for (int i = 0; i < violationCount; i++) {	//add violations data
				row.createCell(i + 6).setCellValue(Utils.isEmpty(obj[i + 6]) ? 0 : Integer.valueOf(obj[i + 6] + ""));
			}
			if(score < 50) {	//all row's cell  red font when score <= 50
				for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
					row.getCell(j).setCellStyle(redCellStyle);
				}
			} else {
				for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
					row.getCell(j).setCellStyle(cellStyle);
				}
			}
			//other row
			for (int i = 1; i < chartData.size(); i++) {	
				obj = chartData.get(i);	//current data
				row = sheet.createRow(i + 3);	//create new row
				row.createCell(0).setCellValue(i+1);	//No. column
				row.createCell(1).setCellValue(obj[1] + "");	//Department column
				row.createCell(2).setCellValue(startDate + " " + "00:00:00");	//Start Time column
				row.createCell(3).setCellValue(endDate + " " + "23:59:59");	//End Time column
				row.createCell(4).setCellValue(Utils.isEmpty(obj[3]) ? 0 : Integer.valueOf(obj[3] + ""));	//Total column
				totalScore = (obj[4] != null ? Double.parseDouble(obj[4] + "") : 0d);
				count = Integer.parseInt(obj[5] + "");
				score = 0d;
				if ("1".equals(dateType)) {
					score = 100 - totalScore / count;
				}
				if ("2".equals(dateType)) {
					score = 100 - totalScore / count / 7;
				}
				if ("3".equals(dateType)) {
					score = 100 - totalScore / count / 30;
				}
				if ("4".equals(dateType)) {
					score = 100 - totalScore / count / 365;
				}
				if (score < 0) {
					score = 0d;
				}
				score = Double.valueOf(format.format(score));
				row.createCell(5).setCellValue(score);	//Score column
				for (int j = 0; j < violationCount; j++) {	//add violations data
					row.createCell(j + 6).setCellValue(Utils.isEmpty(obj[j + 6]) ? 0 : Integer.valueOf(obj[j + 6] + ""));
				}
				if(score < 50) {	//all row's cell  red font when score < 50
					for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
						row.getCell(j).setCellStyle(redCellStyle);
					}
				} else {
					for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
						row.getCell(j).setCellStyle(cellStyle);
					}
				}
			}
			//change chart's data area
			XSSFChart chart = sheet.createDrawingPatriarch().getCharts().get(0);	//get chart object
			CTBarSer ser = chart.getCTChart().getPlotArea().getBarChartList().get(0).getSerArray(0);
			ser.getCat().getNumRef().setF("'Report Department'!$B$4:$B$" + (chartData.size() + 3));
			ser.getVal().getNumRef().setF("'Report Department'!$F$4:$F$" + (chartData.size() + 3));
			workbook.write(baos);
			byte[] b = baos.toByteArray();
			return new ByteArrayInputStream(b);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fis.close();
				baos.close();
				opcPackage.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Report Route
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public InputStream getRerouteData(Object[] obj, Page<List<String>> page,
			boolean flag, String ges, String les, String behCont) {

		List<Object[]> data = new ArrayList<Object[]>();

		Page<Object[]> page_export = (Page<Object[]>) Utils.getSession()
				.getAttribute("page");

		data = page_export.getResult();

		ExportExcel ee = new ExportExcel() {

			@Override
			protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] total,
					List<?> data) throws IOException {
				HSSFSheet sheet = wb.getSheetAt(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
				HSSFRow rowss = sheet.createRow(0);
				rowss.setHeightInPoints(20);
				HSSFCell hssfCell = rowss.createCell(0);
				hssfCell = this.createCell(wb, hssfCell, "Report Route");
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
				HSSFCell cellrow09 = row2.createCell(8);
				cellrow09.setCellStyle(style);
				HSSFCell cellrow10 = row2.createCell(9);
				cellrow10.setCellStyle(style);
				HSSFCell cellrow11 = row2.createCell(10);
				cellrow11.setCellStyle(style);
				HSSFCell cellrow12 = row2.createCell(11);
				cellrow12.setCellStyle(style);
				HSSFCell cellrow13 = row2.createCell(12);
				cellrow13.setCellStyle(style);
				HSSFCell cellrow14 = row2.createCell(13);
				cellrow14.setCellStyle(style);
				HSSFCell cellrow15 = row2.createCell(14);
				cellrow15.setCellStyle(style);

				cellrow01.setCellValue("No.");
				cellrow02.setCellValue("Route");
				cellrow03.setCellValue("Start Time");
				cellrow04.setCellValue("End Time");
				cellrow05.setCellValue("Total");
				cellrow06.setCellValue("Score");
				cellrow07.setCellValue("Sudden Acceleration");
				cellrow08.setCellValue("Sudden Braking");
				cellrow09.setCellValue("Sudden Left");
				cellrow10.setCellValue("Sudden Right");
				cellrow11.setCellValue("Speeding");
				cellrow12.setCellValue("Neutral Slide");
				cellrow13.setCellValue("Idle");
				cellrow14.setCellValue("Idle Air Conditioning");
				cellrow15.setCellValue("Engine Overspeed ");

				if (null != data && 0 != data.size()) {
					for (int i = 0; i < data.size(); i++) {
						HSSFRow row = sheet.createRow(i + 2);
						Object[] o = (Object[]) data.get(i);
						HSSFCell cell001 = row.createCell(0);
						cell001.setCellStyle(style);
						cell001.setCellValue(o[0] == null ? "" : o[0]
								.toString());
						HSSFCell cell002 = row.createCell(1);
						cell002.setCellStyle(style);
						cell002.setCellValue(o[1] == null ? "" : o[1]
								.toString());
						HSSFCell cell003 = row.createCell(2);
						cell003.setCellStyle(style);
						cell003.setCellValue(total[0].equals("false") ? o[2] == null ? ""
								: o[2].toString() + " 00:00:00"
								: total[1].toString() + " 00:00:00");
						HSSFCell cell004 = row.createCell(3);
						cell004.setCellStyle(style);
						cell004.setCellValue(total[0].equals("false") ? o[2] == null ? ""
								: o[2].toString() + " 23:59:59"
								: total[2].toString() + " 23:59:59");
						HSSFCell cell005 = row.createCell(4);
						cell005.setCellStyle(style);
						cell005.setCellValue(o[3] == null ? "0" : o[3]
								.toString());
						HSSFCell cell006 = row.createCell(5);
						cell006.setCellStyle(style);
						cell006.setCellValue(setScore(total[3].toString(),
								o[4] == null ? "" : o[4].toString()));
						HSSFCell cell007 = row.createCell(6);
						cell007.setCellStyle(style);
						cell007.setCellValue(o[5] == null ? "" : o[5]
								.toString());
						HSSFCell cell008 = row.createCell(7);
						cell008.setCellStyle(style);
						cell008.setCellValue(o[6] == null ? "" : o[6]
								.toString());
						HSSFCell cell009 = row.createCell(8);
						cell009.setCellStyle(style);
						cell009.setCellValue(o[7] == null ? "" : o[7]
								.toString());
						HSSFCell cell010 = row.createCell(9);
						cell010.setCellStyle(style);
						cell010.setCellValue(o[8] == null ? "" : o[8]
								.toString());
						HSSFCell cell011 = row.createCell(10);
						cell011.setCellStyle(style);
						cell011.setCellValue(o[9] == null ? "" : o[9]
								.toString());
						HSSFCell cell012 = row.createCell(11);
						cell012.setCellStyle(style);
						cell012.setCellValue(o[10] == null ? "" : o[10]
								.toString());
						HSSFCell cell013 = row.createCell(12);
						cell013.setCellStyle(style);
						cell013.setCellValue(o[11] == null ? "" : o[11]
								.toString());
						HSSFCell cell014 = row.createCell(13);
						cell014.setCellStyle(style);
						cell014.setCellValue(o[12] == null ? "" : o[12]
								.toString());
						HSSFCell cell015 = row.createCell(14);
						cell015.setCellStyle(style);
						cell015.setCellValue(o[13] == null ? "" : o[13]
								.toString());
					}
				}
				return wb;
			}

		};

		Object[] total = new Object[4];
		total[0] = String.valueOf(flag);
		total[1] = ges;
		total[2] = les;
		total[3] = behCont;
		String str = "Report Route";
		return ee.export(total, data, str);
	}

	private static DecimalFormat format = new DecimalFormat(
			"#################.00");

	public String setScore(String beh, String score) {
		if (score == null || "".equals(score)) {
			score = "0";
		}
		String temp = "";
		if (beh.equals("1")) {
			if (100 - Double.valueOf(score) > 0) {
				temp = format.format(100 - Double.valueOf(score));
			} else {
				temp = "0";
			}
		}
		if (beh.equals("2")) {
			if (100 - Double.valueOf(score) / 7 > 0) {
				temp = format.format(100 - Double.valueOf(score) / 7);
			} else {
				temp = "0";
			}
		}
		if (beh.equals("3")) {
			if (100 - Double.valueOf(score) / 30 > 0) {
				temp = format.format(100 - Double.valueOf(score) / 30);
			} else {
				temp = "0";
			}
		}
		if (beh.equals("4")) {
			if (100 - Double.valueOf(score) / 365 > 0) {
				temp = format.format(100 - Double.valueOf(score) / 365);
			} else {
				temp = "0";
			}
		}
		return temp;
	}

	/**
	 * Report Driver By Route
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public InputStream getRedrirouData(Object[] obj, Page<List<String>> page,
			boolean flag, String ges, String les, String behCont) {

		List<Object[]> data = new ArrayList<Object[]>();

		Page<Object[]> page_export = (Page<Object[]>) Utils.getSession()
				.getAttribute("page");

		data = page_export.getResult();

		ExportExcel ee = new ExportExcel() {

			@Override
			protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] total,
					List<?> data) throws IOException {
				HSSFSheet sheet = wb.getSheetAt(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
				HSSFRow rowss = sheet.createRow(0);
				rowss.setHeightInPoints(20);
				HSSFCell hssfCell = rowss.createCell(0);
				hssfCell = this.createCell(wb, hssfCell,
						"Report Driver By Route");
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
				HSSFCell cellrow09 = row2.createCell(8);
				cellrow09.setCellStyle(style);
				HSSFCell cellrow10 = row2.createCell(9);
				cellrow10.setCellStyle(style);
				HSSFCell cellrow11 = row2.createCell(10);
				cellrow11.setCellStyle(style);
				HSSFCell cellrow12 = row2.createCell(11);
				cellrow12.setCellStyle(style);
				HSSFCell cellrow13 = row2.createCell(12);
				cellrow13.setCellStyle(style);
				HSSFCell cellrow14 = row2.createCell(13);
				cellrow14.setCellStyle(style);
				HSSFCell cellrow15 = row2.createCell(14);
				cellrow15.setCellStyle(style);
				HSSFCell cellrow16 = row2.createCell(15);
				cellrow16.setCellStyle(style);

				cellrow01.setCellValue("No.");
				cellrow02.setCellValue("Driver");
				cellrow03.setCellValue("Route");
				cellrow04.setCellValue("Start Time");
				cellrow05.setCellValue("End Time");
				cellrow06.setCellValue("Total");
				cellrow07.setCellValue("Score");
				cellrow08.setCellValue("Sudden Acceleration");
				cellrow09.setCellValue("Sudden Braking");
				cellrow10.setCellValue("Sudden Left");
				cellrow11.setCellValue("Sudden Right");
				cellrow12.setCellValue("Speeding");
				cellrow13.setCellValue("Neutral Slide");
				cellrow14.setCellValue("Idle");
				cellrow15.setCellValue("Idle Air Conditioning");
				cellrow16.setCellValue("Engine Overspeed ");

				if (null != data && 0 != data.size()) {
					for (int i = 0; i < data.size(); i++) {
						HSSFRow row = sheet.createRow(i + 2);
						Object[] o = (Object[]) data.get(i);
						HSSFCell cell001 = row.createCell(0);
						cell001.setCellStyle(style);
						cell001.setCellValue(o[0] == null ? "" : o[0]
								.toString());
						HSSFCell cell002 = row.createCell(1);
						cell002.setCellStyle(style);
						cell002.setCellValue(o[1] == null ? "" : o[1]
								.toString());
						HSSFCell cell003 = row.createCell(2);
						cell003.setCellStyle(style);
						cell003.setCellValue(o[2] == null ? "" : o[2]
								.toString());
						HSSFCell cell004 = row.createCell(3);
						cell004.setCellStyle(style);
						cell004.setCellValue(total[0].equals("false") ? o[3]
								.toString() + " 00:00:00" : total[1].toString()
								+ " 00:00:00");
						HSSFCell cell005 = row.createCell(4);
						cell005.setCellStyle(style);
						cell005.setCellValue(total[0].equals("false") ? o[3]
								.toString() + " 23:59:59" : total[2].toString()
								+ " 23:59:59");
						HSSFCell cell006 = row.createCell(5);
						cell006.setCellStyle(style);
						cell006.setCellValue(o[4] == null ? "0" : o[4]
								.toString());
						HSSFCell cell007 = row.createCell(6);
						cell007.setCellStyle(style);
						cell007.setCellValue(setScore(total[3].toString(),
								o[5] == null ? "" : o[5].toString()));
						HSSFCell cell008 = row.createCell(7);
						cell008.setCellStyle(style);
						cell008.setCellValue(o[6] == null ? "" : o[6]
								.toString());
						HSSFCell cell009 = row.createCell(8);
						cell009.setCellStyle(style);
						cell009.setCellValue(o[7] == null ? "" : o[7]
								.toString());
						HSSFCell cell010 = row.createCell(9);
						cell010.setCellStyle(style);
						cell010.setCellValue(o[8] == null ? "" : o[8]
								.toString());
						HSSFCell cell011 = row.createCell(10);
						cell011.setCellStyle(style);
						cell011.setCellValue(o[9] == null ? "" : o[9]
								.toString());
						HSSFCell cell012 = row.createCell(11);
						cell012.setCellStyle(style);
						cell012.setCellValue(o[10] == null ? "" : o[10]
								.toString());
						HSSFCell cell013 = row.createCell(12);
						cell013.setCellStyle(style);
						cell013.setCellValue(o[11] == null ? "" : o[11]
								.toString());
						HSSFCell cell014 = row.createCell(13);
						cell014.setCellStyle(style);
						cell014.setCellValue(o[12] == null ? "" : o[12]
								.toString());
						HSSFCell cell015 = row.createCell(14);
						cell015.setCellStyle(style);
						cell015.setCellValue(o[13] == null ? "" : o[13]
								.toString());
						HSSFCell cell016 = row.createCell(15);
						cell016.setCellStyle(style);
						cell016.setCellValue(o[14] == null ? "" : o[14]
								.toString());
					}
				}
				return wb;
			}

		};

		Object[] total = new Object[4];
		total[0] = String.valueOf(flag);
		total[1] = ges;
		total[2] = les;
		total[3] = behCont;
		String str = "Report Driver By Route";
		return ee.export(total, data, str);
	}

	/**
	 * redep导出
	 * 
	 * @param faultVo
	 * @param lines
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public InputStream getRedriData(Object[] obj, Page<List<String>> page) {

		List<Dict> listd = (List<Dict>) obj[0];

		List<Object[]> data = new ArrayList<Object[]>();

		Page<Object[]> page_export = (Page<Object[]>) Utils.getSession()
				.getAttribute("page");
		final Object[] count = (Object[]) Utils.getSession().getAttribute(
				"redricount");
		final DetailsVo dvo = (DetailsVo) Utils.getSession().getAttribute(
				"redridvo");
		final String ge = Utils.getSession().getAttribute("ge") + "";
		final String le = Utils.getSession().getAttribute("le") + "";
		page_export.setPageSize(page_export.getTotalCount());
		data = page_export.getResult();
		// DetachedCriteria criteria_export = (DetachedCriteria) Utils
		// .getSession().getAttribute("criteria_export");
		// List<Order> orderList_export = (List<Order>) Utils.getSession()
		// .getAttribute("orderList_export");
		//
		// Page<Object[]> pageResult = baseDao.getBatch(page_export,
		// criteria_export.getExecutableCriteria(baseDao.getSession()),
		// orderList_export);
		//
		// if (null != pageResult) {
		// if (0 != pageResult.getResult().size()) {
		// data = pageResult.getResult();
		//
		// }
		// }

		ExportExcel ee = new ExportExcel() {

			@Override
			protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] total,
					List<?> data) throws IOException {
				HSSFSheet sheet = wb.getSheetAt(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
				HSSFRow rowss = sheet.createRow(0);
				rowss.setHeightInPoints(20);
				HSSFCell hssfCell = rowss.createCell(0);
				hssfCell = this.createCell(wb, hssfCell, "Report Driver");
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
				cellrow06.setCellStyle(style);

				cellrow01.setCellValue("No.");
				cellrow02.setCellValue("Department");
				cellrow03.setCellValue("Driver");
				cellrow04.setCellValue("Start Time");
				cellrow05.setCellValue("End Time");
				cellrow06.setCellValue("Total");
				cellrow07.setCellValue("Score");

				List<Dict> dictlist = (List<Dict>) total[0];
				for (int i = 0; i < dictlist.size(); i++) {
					HSSFCell cellrow0i = row2.createCell(i + 7);
					cellrow0i.setCellStyle(style);
					cellrow0i.setCellValue(dictlist.get(i).getDictName());
				}

				HSSFRow row4 = sheet.createRow(2);

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
				HSSFCell cellrow405 = row4.createCell(4);
				cellrow405.setCellStyle(styleFont);

				sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 4));

				for (int i = 0; i < count.length; i++) {
					HSSFCell cellrow406 = row4.createCell(i + 5);
					cellrow406.setCellStyle(styleFont);
					DecimalFormat df = new DecimalFormat("####.##");
					if (i == 1) {
						cellrow406.setCellValue(df.format(Double
								.parseDouble(count[i] + "")));
					} else {
						cellrow406.setCellValue(count[i] + "");
					}
				}

				if (null != data && 0 != data.size()) {
					for (int i = 0; i < data.size(); i++) {
						HSSFRow row = sheet.createRow(i + 3);
						Object[] o = (Object[]) data.get(i);
						Double d = 0d;
						if (o[5] != null) {
							d = Double.parseDouble(o[5] + "");
						}
						Double dou = 0d;
						if ("1".equals(dvo.getBehCont())) {
							dou = 100 - d;
						}
						if ("2".equals(dvo.getBehCont())) {
							dou = 100 - d / 7;
						}
						if ("3".equals(dvo.getBehCont())) {
							dou = 100 - d / 30;
						}
						if ("4".equals(dvo.getBehCont())) {
							dou = 100 - d / 365;
						}
						if (dou < 0) {
							dou = 0d;
						}
						DecimalFormat df = new DecimalFormat("####.##");
						if (dou <= 50D) {
							style = this.createFont(wb);
						} else {
							style = this.createStyle(wb);
						}
						HSSFCell cell001 = row.createCell(0);
						cell001.setCellStyle(style);
						cell001.setCellValue(i + 1);
						HSSFCell cell002 = row.createCell(1);
						cell002.setCellStyle(style);
						cell002.setCellValue(o[1] + "");
						HSSFCell cell003 = row.createCell(2);
						cell003.setCellStyle(style);
						cell003.setCellValue(o[2] + "");
						HSSFCell cell004 = row.createCell(3);
						cell004.setCellStyle(style);
						cell004.setCellValue(ge + " " + "00:00:00");
						HSSFCell cell005 = row.createCell(4);
						cell005.setCellStyle(style);
						cell005.setCellValue(le + " 23:59:59");
						HSSFCell cell006 = row.createCell(5);
						cell006.setCellStyle(style);
						cell006.setCellValue(o[4] == null ? "0" : o[4] + "");
						HSSFCell cell007 = row.createCell(6);
						cell007.setCellStyle(style);
						cell007.setCellValue(df.format(dou));
						for (int j = 7; j < o.length; j++) {
							HSSFCell cell008 = row.createCell(j);
							cell008.setCellStyle(style);
							cell008.setCellValue(o[j] + "");
						}

					}
				}
				return wb;
			}

		};

		Object[] total = new Object[1];
		total[0] = listd;
		String str = "Report Driver";
		return ee.export(total, data, str);
	}

	@SuppressWarnings("unchecked")
	public InputStream getRepDriverContrastExcel() {
		List<KeyAndValue> violationList = (List<KeyAndValue>)this.getCoeList(201000L);
		int violationCount = violationList.size();
		Page<Object[]> page_export = (Page<Object[]>) Utils.getSession().getAttribute("page");
		final String startDate = Utils.getSession().getAttribute("ge") + "";
		final String endDate = Utils.getSession().getAttribute("le") + "";
		List<Object[]> chartData = page_export.getResult();
		DetailsVo vo = (DetailsVo) Utils.getSession().getAttribute("redridvo");
		final Object[] total = (Object[]) Utils.getSession().getAttribute("redricount");
		final String dateType = vo.getBehCont();
		DecimalFormat format = new DecimalFormat("####.##");
		
		OPCPackage opcPackage = null;
		XSSFWorkbook workbook = null;
		InputStream fis = this.getClass().getResourceAsStream("/../../excel-temp/temp_reportDriverContrast.xlsx");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			opcPackage = OPCPackage.open(fis);		//read temp stream can prevent the temp file from be modified
			workbook = new XSSFWorkbook(opcPackage);
			XSSFSheet sheet = workbook.getSheetAt(0);
			//title row
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6 + violationCount));
			//navigation row
			XSSFRow row = sheet.getRow(1);
			row.getCell(2).setCellValue(vo.getOrgName());	//set department
			row.getCell(4).setCellValue(vo.getDriverName());	//set Stat Type
			if ("1".equals(dateType)) {
				row.getCell(7).setCellValue("Daily");	//set Stat Type
				row.getCell(10).setCellValue(vo.getDate1());	//set date
			}
			if ("2".equals(dateType)) {
				row.getCell(7).setCellValue("Weekly");	//set Stat Type
				row.getCell(10).setCellValue(vo.getDate2());	//set date
			}
			if ("3".equals(dateType)) {
				row.getCell(7).setCellValue("Monthly");	//set Stat Type
				row.getCell(10).setCellValue(vo.getDate3());	//set date
			}
			if ("4".equals(dateType)) {
				row.getCell(7).setCellValue("Yearly");	//set Stat Type
				row.getCell(10).setCellValue(vo.getDate4());	//set date
			}
			//grid's header row
			row = sheet.getRow(2);	
			XSSFCell cell = null;
			CellStyle cellStyle = ExportExcel.createXNormalFont(workbook);	//load header row's cell style
			cellStyle.setWrapText(true);	//set wrap
			for (int i = 0; i < violationCount; i++) {	//add violations columns
				cell = row.createCell(i + 7);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(violationList.get(i).getValue());
			}
			//grid's body row
			//total row
			row = sheet.getRow(3);	
			double score = 0d;
			CellStyle redCellStyle = ExportExcel.createXDarkRedBolderFont(workbook);	//load total row's cell style
			row.getCell(0).setCellStyle(redCellStyle);
			for (int i = 0; i < total.length; i++) {
				score = Double.valueOf(Utils.isEmpty(total[i]) ? "0" : format.format(total[i]));
				cell = row.createCell(i + 5);
				cell.setCellStyle(redCellStyle);
				cell.setCellValue(score);
			}
			//first row
			row = sheet.getRow(4);
			redCellStyle = ExportExcel.createXRedNormalFont(workbook);
			cellStyle = row.getCell(0).getCellStyle();	//load body row's normal cell style
			Object[] obj = chartData.get(0);	//first data
			row.getCell(1).setCellValue(obj[1] + "");
			row.getCell(2).setCellValue(obj[2] + "");
			row.getCell(3).setCellValue(startDate + " " + "00:00:00");
			row.getCell(4).setCellValue(endDate + " " + "23:59:59");
			row.getCell(5).setCellValue(Utils.isEmpty(obj[4]) ? 0 : Double.valueOf(obj[4] + ""));
			Double count = Utils.isEmpty(obj[5]) ? 0D : Double.parseDouble(obj[5] + "");
			score = 0d;
			if ("1".equals(dateType)) {
				score = 100 - count;
			}
			if ("2".equals(dateType)) {
				score = 100 - count / 7;
			}
			if ("3".equals(dateType)) {
				score = 100 - count / 30;
			}
			if ("4".equals(dateType)) {
				score = 100 - count / 365;
			}
			if (score < 0) {
				score = 0d;
			}
			score = Double.valueOf(format.format(score));
			row.getCell(6).setCellValue(score);
			row.getCell(7).setCellValue(Utils.isEmpty(obj[6]) ? 0D : Double.parseDouble(obj[6] + ""));
			for (int i = 0; i < violationCount - 1; i++) {	//add violations data
				row.createCell(i + 8).setCellValue(Utils.isEmpty(obj[i + 7]) ? 0 : Double.valueOf(obj[i + 7] + ""));
			}
			if(score < 50) {	//all row's cell  red font when score <= 50
				for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
					row.getCell(j).setCellStyle(redCellStyle);
				}
			} else {
				for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
					row.getCell(j).setCellStyle(cellStyle);
				}
			}
			//other row
			for (int i = 1; i < chartData.size(); i++) {	
				obj = chartData.get(i);	//current data
				row = sheet.createRow(i + 4);	//create new row
				row.createCell(0).setCellValue(i+1);	//No. column
				row.createCell(1).setCellValue(obj[1] + "");	//Department column
				row.createCell(2).setCellValue(obj[2] + "");	//Driver column
				row.createCell(3).setCellValue(startDate + " " + "00:00:00");	//Start Time column
				row.createCell(4).setCellValue(endDate + " " + "23:59:59");	//End Time column
				row.createCell(5).setCellValue(Utils.isEmpty(obj[4]) ? 0 : Double.valueOf(obj[4] + ""));	//Total column
				count = Utils.isEmpty(obj[5]) ? 0D : Double.parseDouble(obj[5] + "");
				score = 0d;
				if ("1".equals(dateType)) {
					score = 100 - count;
				}
				if ("2".equals(dateType)) {
					score = 100 - count / 7;
				}
				if ("3".equals(dateType)) {
					score = 100 - count / 30;
				}
				if ("4".equals(dateType)) {
					score = 100 - count / 365;
				}
				if (score < 0) {
					score = 0d;
				}
				score = Double.valueOf(format.format(score));
				row.createCell(6).setCellValue(score);	//Score column
				for (int j = 0; j < violationCount; j++) {	//add violations data
					row.createCell(j + 7).setCellValue(Utils.isEmpty(obj[j + 6]) ? 0 : Double.valueOf(obj[j + 6] + ""));
				}
				if(score < 50) {	//all row's cell  red font when score <= 50
					for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
						row.getCell(j).setCellStyle(redCellStyle);
					}
				} else {
					for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
						row.getCell(j).setCellStyle(cellStyle);
					}
				}
			}
			//change chart's data area
			XSSFChart chart = sheet.createDrawingPatriarch().getCharts().get(0);	//get chart object
			CTBarChart barChart = chart.getCTChart().getPlotArea().getBarChartList().get(0);	//getCTChart
			CTBarSer saSer = barChart.getSerArray(0);
			saSer.getCat().getStrRef().setF("'Report Driver Contrast'!$C$5:$C$" + (chartData.size() + 4));
			saSer.getVal().getNumRef().setF("'Report Driver Contrast'!$H$5:$H$" + (chartData.size() + 4));
			CTBarSer newSer = null;
			String charStr = "IJKLMNOPQRST";	//excel column's sequence
			for (int i = 0; i < violationCount - 1; i++) {
				newSer = barChart.addNewSer();
				newSer.addNewIdx().setVal(i + 1);
				newSer.addNewOrder().setVal(i + 1);
				newSer.addNewInvertIfNegative().setVal(false);
				CTStrRef txStrRef = newSer.addNewTx().addNewStrRef();
				txStrRef.setF("'Report Driver Contrast'!$" + charStr.charAt(i) + "$3");
				CTStrRef strRef = newSer.addNewCat().addNewStrRef();
				strRef.setF("'Report Driver Contrast'!$C$5:$C$5" + (chartData.size() + 4));
				CTNumRef numRef = newSer.addNewVal().addNewNumRef();
				numRef.setF("'Report Driver Contrast'!$" + charStr.charAt(i) + "$5:$" + charStr.charAt(i) + "$" + (chartData.size() + 4));
			}
			workbook.write(baos);
			byte[] b = baos.toByteArray();
			return new ByteArrayInputStream(b);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fis.close();
				baos.close();
				opcPackage.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}