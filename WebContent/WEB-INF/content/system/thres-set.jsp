<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<style type="text/css">
			.ui-autocomplete-input {
				width: 200px;
			}
		</style>
		<script type="text/JavaScript">
			$(function() {//解决ie浏览器点击文本框却没光标的bug
				$(".Reprot_td input").css("padding-right", 1);
			});
	       
			function doSubmit() {
				var sa_speed_variation = $("#sa_speed_variation").val();
				if(sa_speed_variation == '') {
					alert('Sudden Acceleration - Speed Variation should not be empty!');
					$("#sa_speed_variation").select();
					return;
				} else {
					if(Number(sa_speed_variation) == 0 || Number(sa_speed_variation) > 100) {
						alert('Sudden Acceleration - Speed Variation is out of range!');
						$("#sa_speed_variation").select();
						return;
					}
				}
				var sa_time = $("#sa_time").val();
				if($("#sa_time").val() == '') {
					alert('Sudden Acceleration - Time should not be empty!');
					$("#sa_time").select();
					return;
				} else {
					if(Number(sa_time) == 0 || Number(sa_time) > 10) {
						alert('Sudden Acceleration - Time is out of range!');
						$("#sa_time").select();
						return;
					}
				}
				var sb_speed_variation = $("#sb_speed_variation").val();
				if(sb_speed_variation == '') {
					alert('Sudden Braking - Speed Variation should not be empty!');
					$("#sb_speed_variation").select();
					return;
				} else {
					if(Number(sb_speed_variation) == 0 || Number(sb_speed_variation) > 100) {
						alert('Sudden Braking - Speed Variation is out of range!');
						$("#sb_speed_variation").select();
						return;
					}
				}
				var sb_time = $("#sb_time").val();
				if(sb_time == '') {
					alert('Sudden Braking - Time should not be empty!');
					$("#sb_time").select();
					return;
				} else {
					if(Number(sb_time) == 0 || Number(sb_time) > 10) {
						alert('Sudden Braking - Time is out of range!');
						$("#sb_time").select();
						return;
					}
				}
 				var sl_angle_variation = $("#sl_angle_variation").val();
				if(sl_angle_variation == '') {
					alert('Sudden Left - Angle Variation should not be empty!');
					$("#sl_angle_variation").select();
					return;
				} else {
					if(Number(sl_angle_variation) > 359) {
						alert('Sudden Left - Angle Variation is out of range!');
						$("#sl_angle_variation").select();
						return;
					}
				}
				var sl_time = $("#sl_time").val();
				if(sl_time == '') {
					alert('Sudden Left - Time should not be empty!');
					$("#sl_time").select();
					return;
				} else {
					if(Number(sl_time) > 255) {
						alert('Sudden Left - Time is out of range!');
						$("#sl_time").select();
						return;
					}
				}
				var sl_min_speed = $("#sl_min_speed").val();
				if(sl_min_speed == '') {
					alert('Sudden Left - Min Speed should not be empty!');
					$("#sl_min_speed").select();
					return;
				} else {
					if(Number(sl_min_speed) < 0) {
						alert('Sudden Left - Min Speed is out of range!');
						$("#sl_min_speed").select();
						return;
					}
				}
				var sr_angle_variation = $("#sr_angle_variation").val();
				if(sr_angle_variation == '') {
					alert('Sudden Right - Angle Variation should not be empty!');
					$("#sr_angle_variation").select();
					return;
				} else {
					if(Number(sr_angle_variation) > 359) {
						alert('Sudden Right - Angle Variation is out of range!');
						$("#sr_angle_variation").select();
						return;
					}
				}
				var sr_time = $("#sr_time").val();
				if(sr_time == '') {
					alert('Sudden Right - Time should not be empty!');
					$("#sr_time").select();
					return;
				} else {
					if(Number(sr_time) > 255) {
						alert('Sudden Right - Time is out of range!');
						$("#sr_time").select();
						return;
					}
				}
				var sr_min_speed = $("#sr_min_speed").val();
				if(sr_min_speed == '') {
					alert('Sudden Right - Min Speed should not be empty!');
					$("#sr_min_speed").select();
					return;
				} else {
					if(Number(sr_min_speed) < 0) {
						alert('Sudden Right - Min Speed is out of range!');
						$("#sr_min_speed").select();
						return;
					}
				}
				var sp_max_speed = $("#sp_max_speed").val();
				if(sp_max_speed == '') {
					alert('Speeding - Max Speed should not be empty!');
					$("#sp_max_speed").select();
					return;
				} else {
					if(Number(sp_max_speed) <= 0) {
						alert('Speeding - Max Speed is out of range!');
						$("#sp_max_speed").select();
						return;
					}
				}
				var sp_duration = $("#sp_duration").val();
				if(sp_duration == '') {
					alert('Speeding - Duration should not be empty!');
					$("#sp_duration").select();
					return;
				} else {
					if(Number(sp_duration) <= 0) {
						alert('Speeding - Duration is out of range!');
						$("#sp_duration").select();
						return;
					}
				}
				var ns_min_speed = $("#ns_min_speed").val();
				if(ns_min_speed == '') {
					alert('Neutral slide - Min Speed should not be empty!');
					$("#ns_min_speed").select();
					return;
				} else {
					if(Number(ns_min_speed) > 255) {
						alert('Neutral slide - Min Speed is out of range!');
						$("#ns_min_speed").select();
						return;
					}
				}
				var ns_max_speed = $("#ns_max_speed").val();
				if(ns_max_speed == '') {
					alert('Neutral slide - Max Engine Speed should not be empty!');
					$("#ns_max_speed").select();
					return;
				} else {
					if(Number(ns_max_speed) > 65535) {
						alert('Neutral slide - Max Engine Speed is out of range!');
						$("#ns_max_speed").select();
						return;
					}
				}
				var ns_duration = $("#ns_duration").val();
				if(ns_duration == '') {
					alert('Neutral slide - Duration should not be empty!');
					$("#ns_duration").select();
					return;
				} else {
					if(Number(ns_duration) < 2) {
						alert('Neutral slide - Duration is out of range!');
						$("#ns_duration").select();
						return;
					}
				}
				var id_duration = $("#id_duration").val();
				if(id_duration == '') {
					alert('Idle - Duration should not be empty!');
					$("#id_duration").select();
					return;
				} else {
					if(Number(id_duration) > 65535) {
						alert('Idle - Duration is out of range!');
						$("#id_duration").select();
						return;
					}
				}
				var eo_max_speed = $("#eo_max_speed").val();
				if(eo_max_speed == '') {
					alert('Engine overspeed - Max Engine Speed should not be empty!');
					$("#eo_max_speed").select();
					return;
				} else {
					if(Number(eo_max_speed) > 65535) {
						alert('Engine overspeed - Max Engine Speed is out of range!');
						$("#eo_max_speed").select();
						return;
					}
				}
				var eo_duration = $("#eo_duration").val();
				if(eo_duration == '') {
					alert('Engine overspeed - Duration should not be empty!');
					$("#eo_duration").select();
					return;
				} else {
					if(Number(eo_duration) < 2) {
						alert('Engine overspeed - Duration is out of range!');
						$("#eo_duration").select();
						return;
					}
				}
				document.forms['queryResult'].action = basePath + 'system/thres-set!create';
				document.forms['queryResult'].submit();
			}
       </script>
    </head>
	<body>
		<custom:navigation father="System Settings" model="Threshold Setting">
			<li>
				<a style="width: 60px; margin-left: -15px;" onclick="doSubmit();" href="#">
					<img border="0" align="absmiddle" title="Save" src="<%=basePath%>images/dtree/ico_save.gif"> Send
				</a>
			</li>
		</custom:navigation>
		<form name="queryResult" action="<%=basePath%>system/thres-set!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<input type="hidden" value="${tslist[0].thresholdid}" name="tslist[0].thresholdid" />
			<input type="hidden" value="${tslist[0].type}" name="tslist[0].type" />
			<input type="hidden" value="${tslist[0].typeid}" name="tslist[0].typeid" />
	       	<input type="hidden" value="${tslist[0].vehicletype}" name="tslist[0].vehicletype" />
	       	<input type="hidden" value="${tslist[0].vehicletypeid}" name="tslist[0].vehicletypeid" />
	       	<input type="hidden" value="${tslist[1].thresholdid}" name="tslist[1].thresholdid" />
	       	<input type="hidden" value="${tslist[1].type}" name="tslist[1].type" />
	       	<input type="hidden" value="${tslist[1].typeid}" name="tslist[1].typeid" />
	       	<input type="hidden" value="${tslist[1].vehicletype}" name="tslist[1].vehicletype" />
	       	<input type="hidden" value="${tslist[1].vehicletypeid}" name="tslist[1].vehicletypeid" />
	       	<input type="hidden" value="${tslist[2].thresholdid}" name="tslist[2].thresholdid" />
		    <input type="hidden" value="${tslist[2].type}" name="tslist[2].type" />
		    <input type="hidden" value="${tslist[2].typeid}" name="tslist[2].typeid" />
		    <input type="hidden" value="${tslist[2].vehicletype}" name="tslist[2].vehicletype" />
		    <input type="hidden" value="${tslist[2].vehicletypeid}" name="tslist[2].vehicletypeid" />
		    <input type="hidden" value="${tslist[3].thresholdid}" name="tslist[3].thresholdid" />
			<input type="hidden" value="${tslist[3].type}" name="tslist[3].type" />
			<input type="hidden" value="${tslist[3].typeid}" name="tslist[3].typeid" />
			<input type="hidden" value="${tslist[3].vehicletype}" name="tslist[3].vehicletype" />
			<input type="hidden" value="${tslist[3].vehicletypeid}" name="tslist[3].vehicletypeid" />
			<input type="hidden" value="${tslist[4].thresholdid}" name="tslist[4].thresholdid" />
			<input type="hidden" value="${tslist[4].type}" name="tslist[4].type" />
			<input type="hidden" value="${tslist[4].typeid}" name="tslist[4].typeid" />
			<input type="hidden" value="${tslist[4].vehicletype}" name="tslist[4].vehicletype" />
			<input type="hidden" value="${tslist[4].vehicletypeid}" name="tslist[4].vehicletypeid" />
			<input type="hidden" value="${tslist[5].thresholdid}" name="tslist[5].thresholdid" />
			<input type="hidden" value="${tslist[5].type}" name="tslist[5].type" />
			<input type="hidden" value="${tslist[5].typeid}" name="tslist[5].typeid" />
			<input type="hidden" value="${tslist[5].vehicletype}" name="tslist[5].vehicletype" />
			<input type="hidden" value="${tslist[5].vehicletypeid}" name="tslist[5].vehicletypeid" />
			<input type="hidden" value="${tslist[6].thresholdid}" name="tslist[6].thresholdid" />
			<input type="hidden" value="${tslist[6].type}" name="tslist[6].type" />
			<input type="hidden" value="${tslist[6].typeid}" name="tslist[6].typeid" />
			<input type="hidden" value="${tslist[6].vehicletype}" name="tslist[6].vehicletype" />
			<input type="hidden" value="${tslist[6].vehicletypeid}" name="tslist[6].vehicletypeid" />
			<input type="hidden" value="${tslist[7].thresholdid}" name="tslist[7].thresholdid" />
			<input type="hidden" value="${tslist[7].type}" name="tslist[7].type" />
			<input type="hidden" value="${tslist[7].typeid}" name="tslist[7].typeid" />
			<input type="hidden" value="${tslist[7].vehicletype}" name="tslist[7].vehicletype" />
			<input type="hidden" value="${tslist[7].vehicletypeid}" name="tslist[7].vehicletypeid" />
			<input type="hidden" value="${tslist[8].thresholdid}" name="tslist[8].thresholdid" />
			<input type="hidden" value="${tslist[8].type}" name="tslist[8].type" />
			<input type="hidden" value="${tslist[8].typeid}" name="tslist[8].typeid" />
			<input type="hidden" value="${tslist[8].vehicletype}" name="tslist[8].vehicletype" />
			<input type="hidden" value="${tslist[8].vehicletypeid}" name="tslist[8].vehicletypeid" />
			<custom:searchBody>
				<custom:searchItem title="Vehicle Type" side="first">
				  	<%-- <custom:select name="tsvo.vehicletypeid" list="${typelist}" value="${tsvo.vehicletypeid}" width="200px" /> --%>
				  	<s:select list="typelist" name="tsvo.vehicletypeid" id="vehId" theme="simple" listKey="key" listValue="value" cssStyle="width:200px;" />
				</custom:searchItem>
				<custom:searchItem width="40%">
					<custom:search onclick="sub();" />
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="false">
					<td width="15%">Vehicle Type</td>
					<td width="15%">Alerting Type</td>
					<td width="20%">Threshold Settings</td>
					<td width="20%">Threshold Values</td>
					<td width="20%">Range</td>
				</custom:tableHeader>
				<!-- Sudden Acceleration -->
				<tr class="Reprot_td" id="tr${tslist[0].thresholdid}">
					<td rowspan="2"><label>1</label></td>
					<td rowspan="2"><label>${tslist[0].vehicletype}</label></td>
					<td rowspan="2"><label>Sudden Acceleration</label></td>
					<td><label>Speed variation(km/h)</label></td>
					<td>
						<input type="text" id="sa_speed_variation" name="tslist[0].startvalue" value="<fmt:formatNumber value='${tslist[0].startvalue}' pattern='#.######'/>" maxlength="5" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>1-100</label></td>
				</tr>
				<tr class="Reprot_td" id="tr${tslist[0].thresholdid}">
					<td><label>Time(s)</label></td>
					<td>
						<input type="text" id="sa_time" name="tslist[0].usetime" value="${tslist[0].usetime}" maxlength="2" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>1-10</label></td>
				</tr>
				<!-- Sudden Braking --> 
				<tr class="Reprot_td" id="tr${tslist[1].thresholdid}">
					<td rowspan="2"><label>2</label></td>
					<td rowspan="2"><label>${tslist[1].vehicletype}</label></td>
					<td rowspan="2"><label>Sudden Braking</label></td>
					<td><label>Speed variation(km/h)</label></td>
					<td>
						<input type="text" id="sb_speed_variation" name="tslist[1].startvalue" value="<fmt:formatNumber value='${tslist[1].startvalue}' pattern='#.######'/>" maxlength="3" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>1-100</label></td>
				</tr>
				<tr class="Reprot_td" id="tr${tslist[1].thresholdid}">
					<td><label>Time(s)</label></td>
					<td>
						<input type="text" id="sb_time" name="tslist[1].usetime" value="${tslist[1].usetime}" maxlength="2" style="max-width: 30%; background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>1-10</label></td>
				</tr>
		       	<!-- Sudden Left -->
			    <tr class="Reprot_td" id="tr${tslist[2].thresholdid}">
					<td rowspan="3"><label>3</label></td>
					<td rowspan="3"><label>${tslist[2].vehicletype}</label></td>
					<td rowspan="3"><label>Sudden Left</label></td>
					<td><label>Angle Variation(°)</label></td>
					<td>
						<input type="text" id="sl_angle_variation" name="tslist[2].startvalue" value="<fmt:formatNumber value='${tslist[2].startvalue}' pattern='#.######'/>" maxlength="3" style="max-width:30%; background-color: #FFF5EE; text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
					</td>
					<td><label>0-359</label></td>
				</tr>
				<tr class="Reprot_td" id="tr${tslist[2].thresholdid}">
					<td><label>Time(s)</label></td>
					<td>
						<input type="text" id="sl_time" name="tslist[2].usetime" value="${tslist[2].usetime}" maxlength="3" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>0-255</label></td>
				</tr>
				<tr class="Reprot_td" id="tr${tslist[2].thresholdid}">
					<td><label>Min Speed(km/h)</label></td>
					<td>
						<input type="text" id="sl_min_speed" name="tslist[2].endvalue" value="<fmt:formatNumber value='${tslist[2].endvalue}' pattern='#.######'/>" maxlength="5" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>>=0</label></td>
				</tr>
		       	<!-- Sudden Right -->
			    <tr class="Reprot_td" id="tr${tslist[3].thresholdid}">
					<td rowspan="3"><label>4</label></td>
					<td rowspan="3"><label>${tslist[3].vehicletype}</label></td>
					<td rowspan="3"><label>Sudden Right</label></td>
					<td><label>Angle Variation(°)</label></td>
					<td>
						<input type="text" id="sr_angle_variation" name="tslist[3].startvalue" value="<fmt:formatNumber value='${tslist[3].startvalue}' pattern='#.######'/>" maxlength="3" style="max-width:30%; background-color: #FFF5EE; text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
					</td>
					<td><label>0-359</label></td>
				</tr>
				<tr class="Reprot_td" id="tr${tslist[3].thresholdid}">
					<td><label>Time(s)</label></td>
					<td>
						<input type="text" id="sr_time" name="tslist[3].usetime" value="${tslist[3].usetime}" maxlength="3" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>0-255</label></td>
				</tr>
				<tr class="Reprot_td" id="tr${tslist[3].thresholdid}">
					<td><label>Min Speed(km/h)</label></td>
					<td>
						<input type="text" id="sr_min_speed" name="tslist[3].endvalue" value="<fmt:formatNumber value='${tslist[3].endvalue}' pattern='#.######'/>" maxlength="5" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>>=0</label></td>
				</tr>
				<!-- Speeding -->
				<tr class="Reprot_td" id="tr${tslist[4].thresholdid}">
					<td rowspan="2"><label>5</label></td>
					<td rowspan="2"><label>${tslist[4].vehicletype}</label></td>
					<td rowspan="2"><label>Speeding</label></td>
					<td><label>Max Speed(km/h)</label></td>
					<td>
						<input type="text" id="sp_max_speed" name="tslist[4].startvalue" value="<fmt:formatNumber value='${tslist[4].startvalue}' pattern='#.######'/>" maxlength="5" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
					</td>
					<td><label>>0</label></td>
				</tr>
				<tr class="Reprot_td" id="tr${tslist[4].thresholdid}">
					<td><label>Duration(s)</label></td>
					<td>
						<input type="text" id="sp_duration" name="tslist[4].usetime" value="${tslist[4].usetime}" maxlength="5" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>>0</label></td>
				</tr>
				<!-- Neutral Slide -->
				<tr class="Reprot_td" id="tr${tslist[5].thresholdid}">
					<td rowspan="3"><label>6</label></td>
					<td rowspan="3"><label>${tslist[5].vehicletype}</label></td>
					<td rowspan="3"><label>Neutral Slide</label></td>
					<td><label>Min Speed(km/h)</label></td>
					<td>
						<input type="text" id="ns_min_speed" name="tslist[5].startvalue" value="<fmt:formatNumber value='${tslist[5].startvalue}' pattern='#.######'/>" maxlength="3" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
					</td>
					<td><label>0-255</label></td>
				</tr>
				<tr class="Reprot_td" id="tr${tslist[5].thresholdid}">
					<td><label>Max Engine Speed(RPM)</label></td>
					<td>
						<input type="text" id="ns_max_speed" name="tslist[5].endvalue" value="<fmt:formatNumber value='${tslist[5].endvalue}' pattern='#.######'/>" maxlength="5" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>0-65535</label></td>
				</tr>
				<tr class="Reprot_td" id="tr${tslist[5].thresholdid}">
					<td><label>Duration(s)</label></td>
					<td>
						<input type="text" id="ns_duration" name="tslist[5].usetime" value="${tslist[5].usetime}" maxlength="5" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>>=2</label></td>
				</tr>
				<!-- Idle -->
				<tr class="Reprot_td" id="tr${tslist[6].thresholdid}">
					<td><label>7</label></td>
					<td><label>${tslist[6].vehicletype}</label></td>
					<td><label>Idle</label></td>
					<td><label>Duration(s)</label></td>
					<td>
						<input type="text" id="id_duration" name="tslist[6].usetime" value="${tslist[6].usetime}" maxlength="5" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>0-65535</label></td>
				</tr>
				<!-- Engine overspeed -->
				<tr class="Reprot_td" id="tr${tslist[8].thresholdid}">
					<td rowspan="2"><label>8</label></td>
					<td rowspan="2"><label>${tslist[8].vehicletype}</label></td>
					<td rowspan="2"><label>Engine Overspeed</label></td>
					<td><label>Max Engine Speed(RPM)</label></td>
					<td>
						<input type="text" id="eo_max_speed" name="tslist[8].startvalue" value="<fmt:formatNumber value='${tslist[8].startvalue}' pattern='#.######'/>" maxlength="5" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>0-65535</label></td>
				</tr>
				<tr class="Reprot_td" id="tr${tslist[8].thresholdid}">
					<td><label>Duration(s)</label></td>
					<td>
						<input type="text" id="eo_duration" name="tslist[8].usetime" value="${tslist[8].usetime}" maxlength="5" style="max-width:30%;background-color: #FFF5EE;text-align: right;" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
					</td>
					<td><label>>=2</label></td>
				</tr>
			</custom:table>
		</form>
		<script type="text/javascript">
			var vehId = dhtmlXComboFromSelect("vehId");
			vehId.enableFilteringMode(true);
		</script>
	</body>
</html>