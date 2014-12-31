<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	    <style type="text/css">
			html { height: 100% }
			body { height: 100%; margin: 0; padding: 0 }
			#map_canvas { height: 100% }
	    </style>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/system/google-poi-conf.js"></script>
		<script charset="UTF-8" type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=drawing&key=AIzaSyAs2HwT6PEJzn2P8WYiSScJpq0oigo_pGA&sensor=false"></script>
		<script type="text/javascript">
			var iconx;
			$(function() {
				$("#comid").val("${poi.comid}");
				//载入地图
				initMap();
				//添加已有标记点,状态:非选中
				<s:iterator value="poilist" var="b" >
					addPoiMarkers('${b.longitude}', '${b.latitude}', '${b.caption}', '${b.icon}', 'oldMarkers');
				</s:iterator>
				
				iconx = $("#icon").val();
				if(iconx == null||iconx==""){
					//新增时默认选中第一个图片
					iconx="tu_03";
					$("#tu_03").attr("src",basePath+"images/tu_03_.gif");
				} else {
					//编辑时初始化标记点
					selectIcon(iconx);
				}
				
				//编辑时初始化经纬度
				if($("#poiid")!=null){
					if($("#isvisible").val()=='T'){
						$("#cbox").attr('checked',true);
					}
				}
			});
		
			function longla(){
				if(iconx == null||iconx==""){
					//新增时默认选中第一个图片
					iconx="tu_03";
					$("#tu_03").attr("src",basePath+"images/tu_03_.gif");
				} else {
					//编辑时初始化标记点
					selectIcon(iconx);
				}
				if(firstload){
					deleteOverlays();
				}
				firstload=true;
				addPoiMarkers($("#longitude").val(), $("#latitude").val(), "123", iconx, 'newMarker');
			}
		</script>
	</head>
	<body>
		<c:if test="${poiid == null}">
			<custom:navigation father="System Settings" model="POI" operate="Add" saveMethod="subx();" homePath="system/google-poi-conf" />
		</c:if>
		<c:if test="${poiid != null}">
			<custom:navigation father="System Settings" model="POI" operate="Edit" saveMethod="subx();" homePath="system/google-poi-conf" />
		</c:if>
		<form name="poiForm" method="post" action="<%=basePath%>system/google-poi-conf!save">
			<input id="poiid" type="hidden" name="poiid" value="${poiid}"/>
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
					    <td rowspan="12" width="60%" height="487px">
					    	<div id="basicMap" style="width:100%; height:100%;"></div>
					    </td>
						<td class="text_bg" width="10%"><font color="red">*&nbsp;</font>POI:</td>
						<td width="30%" class="valid">
							<input type="text" id="caption" name="caption" value="${caption}" maxlength="30" class="5,1,30" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Company:</td>
						<td>
							<s:select id="comid" name="comid" list="coms" listKey="key" listValue="value" theme="simple" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Longitude:</td>
						<td width="35%" style="text-align:left;">
							<input type="text" id="longitude" name="longitude" value="${longitude}" onblur="longla();" onkeyup="if(this.value==this.value2)return;if(this.value.search(/^\d*(?:\.\d{0,6})?$/)==-1)this.value=(this.value2)?this.value2:'';else this.value2=this.value;if(this.value2.length>10){this.value=''};" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Latitude:</td>
						<td>
							<input type="text" id="latitude" name="latitude" value="${latitude}" onblur="longla();" onkeyup="if(this.value==this.value2)return;if(this.value.search(/^\d*(?:\.\d{0,6})?$/)==-1)this.value=(this.value2)?this.value2:'';else this.value2=this.value;if(this.value2.length>10){this.value=''};"  />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Visibility:</td>
						<td>
							<label>&nbsp;&nbsp;<input type="checkbox" id="cbox" onclick="setvis()" style="width:16px"/></label>
							<input type="hidden" id="isvisible" name="isvisible" value="${isvisible}"/>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Choose Icon:</td>
						<td style="height: 37px;">&nbsp;
						    <a href="#" onclick="selectIcon('tu_03')"><img id="tu_03" src="<%=basePath%>images/tu_03.png" /></a>&nbsp;
						    <a href="#" onclick="selectIcon('tu_05')"><img id="tu_05" src="<%=basePath%>images/tu_05.png" /></a>&nbsp;
						    <a href="#" onclick="selectIcon('tu_07')"><img id="tu_07" src="<%=basePath%>images/tu_07.png" /></a>&nbsp;
							<input type="hidden" id="icon" name="icon" value="${icon}" />
						</td>
					</tr>
					<tr>
						<td class="text_bg" height="295px">Description:<br />(300 characters)</td>
						<td height="295px">
							<s:textarea id="description" name="description" theme="simple" cssStyle="width:100%; height:100%;" />
						</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>