<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.opensymphony.xwork2.ognl.OgnlValueStack"%>
<%
	((OgnlValueStack) request.getAttribute("struts.valueStack")).set(
	"fileProperty", request.getParameter("fileProperty"));
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
	String CDN = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			 + "/erp/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%--
	title：文件上传封装对象
	Useage (JSP):
	<s:include value="/common/fileUpload.jsp">
		<s:param name="label" value="'label'" /> ：标题(暂时无用)
		<s:param name="fileType" value="'image'" />	：'image'\'excel' 暂时限制只能上传此两类文件,默认为excel
		<s:param name="showID" value="'invoicePhoto_img'" /> ：显示上传照片的img标签的id，fileType选image则必填
		<s:param name="fileProperty" value="'invoicePhoto'" /> ：保存实体类对应的属性名，必填
		<s:param name="isEdit" value="'true'" /> ：true-编辑模式有上传按钮; false-查看模式无上传按钮，默认false
	</s:include>
--%>
<s:hidden id="%{fileProperty}" name="%{fileProperty}" theme="simple" />
<head>
	<title>文件上传</title>
	<meta name="author" content="Leon liu" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="<%=CDN%>css/fileuploader.css" rel="stylesheet" type="text/css">
</head>
	<div id='div_${param.showID}' style="width: 68px; height: 22px; background-image: url('<%=CDN%>images/upload_09.jpg'); background-position: center; background-repeat: no-repeat;">
	</div>
    <script src="<%=CDN%>scripts/fileuploader.js" type="text/javascript"></script>
    <script>
	    function addLoadEvent(func) {
			var oldonload = window.onload;
			if (typeof window.onload != 'function') {
				window.onload = func;
			} else {
				window.onload = function() {
					oldonload();
					func();
				};
			}
		}

		function prepareImage() {
			if(document.getElementById('${param.fileProperty}').value != "") {
				document.getElementById('${param.showID}').src = basePath + "common/file-upload!showImg?ul_path=" + document.getElementById('${param.fileProperty}').value + "&time=" + new Date().getTime();
			}
		}

	    function createUploader(){
		    var uploader = new qq.FileUploaderBasic({
		    	multiple: false,
		    	button: document.getElementById('div_${param.showID}'),
		    	//name: '${param.property}',
		        params: {
					"fileType" : '${param.fileType}',
					"property" : '${param.fileProperty}'
					//"oldFile" : document.getElementById('${param.fileProperty}').value
		        },
		        action: basePath + "common/file-upload!upload",
				allowedExtensions: '${param.fileType}' == 'image' ? ['jpg', 'jpeg', 'png', 'gif'] : ['xls'],
	     		sizeLimit: 5000000, // max size 5M
		        minSizeLimit: 0, // min size 0B
		        onSubmit: function(id, fileName){
			        if(!confirm("确定上传文件:" + fileName + "？")) {
				        return false;
				    }
			    },
		        onComplete: function(id, fileName, responseJSON){
					if(responseJSON.success) {
						alert("上传文件：成功！");
						if('${param.fileType}' == 'image') {	//上传图片则立即显示
							document.getElementById('${param.showID}').src = basePath + "common/file-upload!showImg?time=" + new Date().getTime();
							document.getElementById('${param.fileProperty}').value = responseJSON.success;
						}
						return true;
					} else if(responseJSON.error == "dirError") {
						alert("创建路径错误！");
						return false;
					} else if(responseJSON.error == "notFound") {
						alert("未找到文件！");
						return false;
					}
				},
		        messages: {
		            typeError: "文件：{file} 类型不合法. 只能上传 {extensions} 类型的文件！",
		            sizeError: "文件：{file} 过大, 最大不能超过 {sizeLimit} Byte！",
		            emptyError: "文件：{file} 是空的，请选择其他文件！",
		            onLeave: "文件正在上传，请等待..."
		        },
		        showMessage: function(message){
		            alert(message);
		        },
		        debug: true
		    });
	    }
        // in your app create uploader as soon as the DOM is ready
        // don't wait for the window to load
		if('${param.fileType}' == 'image') {
        	addLoadEvent(prepareImage);
		}
        if('${param.isEdit}' == 'true') {
        	addLoadEvent(createUploader);
        } else {
        	document.getElementById('div_${param.showID}').style.display="none";
		}
    </script>