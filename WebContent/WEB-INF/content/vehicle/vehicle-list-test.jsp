<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
<body>
<form name="form" action="<%=basePath%>vehicle/vehicle-list!test" method="post">
<br/>
<br/>
<h4>Jquery Autocomplate/Combobox Demo</h4>
<br/>
&nbsp;Test:	<s:include value="/common/jquery_combobox.jsp">
		<s:param name="name" value="'vehvo.deviceid'" />
		<s:param name="value" value="%{vehvo.deviceid}" />
		<s:param name="url" value="'vehicle/vehicle-list!getData'" />
	</s:include>
	<input onclick="document.form.submit()" value="submit"  type="button"/>
	<input onclick="alert($('#vehvo\\.deviceid').val())" value="getSelectedValue"  type="button"/>
	<br>
	<br/>
	<br/>
	<br/>
	&nbsp;<b>用法：</b><br/>
	&nbsp;&nbsp;<span>&lt;s:include value="/common/jquery_combobox.jsp"&gt;</span><br/>
	&nbsp;&nbsp;&nbsp;<span>&lt;s:param name="name" value="'vehvo.deviceid'" /&gt;</span><br/>
	&nbsp;&nbsp;&nbsp;<span>&lt;s:param name="value" value="%{vehvo.deviceid}" /&gt;</span><br/>
	&nbsp;&nbsp;&nbsp;<span>&lt;s:param name="url" value="'vehicle/vehicle-list!getData'" /&gt;</span><br/>
	&nbsp;&nbsp;<span>&lt;/s:include&gt;</span>
	<br/>
	<br/>
	&nbsp;<b>Controller</b><br/>
	&nbsp;&nbsp;&nbsp;<span>renderJson(manager.getDatas(Utils.getParameter("search_text"), Utils.getParameter("search_value")))</span>
	<br/>
	<br/>
	&nbsp;<b>Manager</b><br/>
	&nbsp;&nbsp;<span>if (null == value || "".equals(value)) {</span><br/>
	&nbsp;&nbsp;&nbsp;<span>sql.append("SELECT * FROM (SELECT T.VEHICLEID KEY , T.VEHICLENAME VALUE FROM T_CORE_VEHICLE T WHERE  T.VEHICLENAME LIKE '%" + text + "%' OR T.VEHICLENAME LIKE '%" + text + "%') WHERE ROWNUM <11");</span><br/>
	&nbsp;&nbsp;<span>}else {</span><br/>
	&nbsp;&nbsp;&nbsp;<span>sql.append("SELECT T.VEHICLEID KEY , T.VEHICLENAME VALUE FROM T_CORE_VEHICLE T WHERE  T.VEHICLEID =" + value ");</span><br/>
	&nbsp;&nbsp;<span>}</span><br/>
	&nbsp;<span>return baseDao.getKeyAndValueBySQL(sql.toString());</span><br/>
</form>
</body>
</html>