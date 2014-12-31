<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<body height="100%">
		<custom:navigation father="Monitor" model="Police Playback" />
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0" height="100%">
				<tr>
					<td class="text_bg" width="13%" >Police:</td>
					<td width="37%" style="text-align: left;">
						<s:select id="select_police" list="policeList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" />
					</td>
					<td class="text_bg" width="13%">Date:</td>
					<td width="37%" style="text-align: left;">
						<input type="text" id="date" style="width: 127px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', readOnly:true});this.blur();" />
					</td>
				</tr>
				<tr>
					<td class="text_bg">StartTime:</td>
					<td style="text-align: left;">
						<label class="input_date">
							<input type="text" id="startTime" style="width: 127px;" onfocus="WdatePicker({dateFmt:'HH:mm:ss', maxDate:&quot;#F{$dp.$D('endTime')}&quot;,readOnly:true});this.blur();" />
						</label>
					</td>
					<td class="text_bg">EndTime:</td>
					<td style="text-align: left;">
						<ul>
							<li style="display:inline;">
								<label class="input_date">
									<input type="text" id="endTime" style="width: 127px;" onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:&quot;#F{$dp.$D('startTime')}&quot;,readOnly:true});this.blur();" />
								</label>
							</li>
							<li style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</li>
							<li style="display:inline;">
								<button class="button_input" onclick="doPlay();" style="width: 60px;">Play</button>
								<button class="button_input" onclick="doStop();" style="width: 60px;">Stop</button>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td colspan="4" style="text-align: center;">
						<div>
							<object id="zdcomaudio" name="zdcomaudio" 
   								classid="clsid:7E6CC141-11E8-41AE-A1BE-CF6606B1DB48"
   								codebase="<%=basePath%>cabs/ZdcomAudio.cab#version=1,0,9,0"
   								width="480" height="360">
   							</object>			
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
	<script type="text/javascript">
		var select_police = dhtmlXComboFromSelect_cascade('select_police',120);
		select_police.enableFilteringMode(true);
	
		function doPlay() {
			var deviceName = select_police.getSelectedValue();
			var date = $("#date").val();
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			if(deviceName == null || deviceName == "" || deviceName == '-1') {
				alert("Please select police");
				return;
			}
			if(date == null || date == ""){
				alert("Please select date");
				return;
			}
			if(startTime == null || startTime == ""){
				alert("Please select start time");
				return;
			}
			
			if(endTime == null || endTime == ""){
				alert("Please select end time");
				return;
			}
			var year = date.split('-')[0];
			var month = date.split('-')[1];
			var day = date.split('-')[2];
			var hour = startTime.split(':')[0];
			var minute = startTime.split(':')[1];
			var second = startTime.split(':')[2];
			var secondStr = new Date(year, month-1, day, hour, minute, second).getTime().toString().substring(0,10);	//取秒数
			var r = zdcomaudio.PlayNet_history("172.100.100.35", 18026, "${mainUser.loginName}", 2, deviceName, 1, 480, 360, secondStr);
			if(r == 0) {
				alert('Video Connected');
			}
		}

		function doStop(){
			zdcomaudio.CloseOCX();
		}
	</script>	
</html>