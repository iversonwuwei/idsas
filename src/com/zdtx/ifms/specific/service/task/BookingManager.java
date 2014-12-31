package com.zdtx.ifms.specific.service.task;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.specific.model.task.Booking;
import com.zdtx.ifms.specific.model.task.BookingDetail;
import com.zdtx.ifms.specific.vo.task.BookingVO;

@Service
@Transactional
public class BookingManager {

	@Autowired
	private BaseDao baseDao;
	
	public Page<Booking> getPage(Page<Booking> page, BookingVO bVo) {
		Criteria criteria = baseDao.getSession().createCriteria(Booking.class);
		if(null != bVo.getBookingdate() && !"".equals(bVo.getBookingdate())){
			criteria.add(Restrictions.eq("bookingdate", bVo.getBookingdate()));
		}
		if(null != bVo.getVehicleid() &&  -1 != bVo.getVehicleid()){
			criteria.add(Restrictions.eq("vehicletypeid",bVo.getVehicleid()));
		}
		if(null != bVo.getDestination() && !"".equals(bVo.getDestination())){
			criteria.add(Restrictions.ilike("destination","%" +bVo.getDestination().trim()+"%"));
		}
		if(null != bVo.getDuty() && !"".equals(bVo.getDuty())){
			criteria.add(Restrictions.eq("duty",bVo.getDuty()));
		}
		if(null != bVo.getRoute() && !"-1".equals(bVo.getRoute().trim())){
			criteria.add(Restrictions.eq("route",bVo.getRoute()));
		}
		criteria.add(Restrictions.eq("isdelete", "F"));
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(Order.desc("bookingdate"));
		orderList.add(Order.asc("vehicletype"));
		return baseDao.getBatch(page, criteria, orderList);
	}
	
	public List<Booking> getBath() {
		Criteria criteria = baseDao.getSession().createCriteria(Booking.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		return baseDao.getAll(criteria);
	}
	
	public List<Booking> getBath(String date,Long id) {
		Criteria criteria = baseDao.getSession().createCriteria(Booking.class);
		if(null != id){
			criteria.add(Restrictions.ne("bookingid",id));
		}
		if(null != date && !"".equals(date)){
			criteria.add(Restrictions.eq("bookingdate", date));
		}
		criteria.add(Restrictions.eq("isdelete", "F"));
		return baseDao.getAll(criteria);
	}
	
	public List<BookingDetail> getBath1(Long bookingid) {
		Criteria criteria = baseDao.getSession().createCriteria(BookingDetail.class);
		if(null != bookingid){
			criteria.add(Restrictions.eq("bookingid", bookingid));
		}
		criteria.add(Restrictions.eq("isdelete", "F"));
		return baseDao.getAll(criteria);
	}


	 
	
}
