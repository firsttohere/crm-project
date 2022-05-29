<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta charset="UTF-8">
<link href="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function(){
		//给整个浏览器窗口绑定按下回车事件
		$(window).keydown(function(e){
			if(e.keyCode == 13){
				$("#loginBut").click();
			}
		});
		
		//给登录按钮绑定事件
		$("#loginBut").click(function(){
			//发请求和处理请求
			//先收集参数
			var username = $.trim($("#username").val());//登录用户名
			var password = $.trim($("#password").val());//密码啊
			var isRemPwd = $("#isRemPwd").prop("checked");//获取复选框的属性值，true/false
			//确保这些参数值合法(参数验证)
			if(username == "" || password == ""){
				$("#msg").html("用户名和密码都不能为空！");
				return;
			}
			//提示前端，正在努力验证
			$("#msg").html("正在验证，请稍后...");
			//发送请求
			$.ajax({
				url:'${pageContext.request.contextPath}/settings/qx/user/login',
				data:{
					loginAct:username,
					loginPwd:password,
					isRemPwd:isRemPwd
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if(data["code"] == "1"){
						//登录成功了，跳转到业务主页面
						window.location.href = "${pageContext.request.contextPath}/toMainPage";
					}else{
						//显示提示信息
						$("#msg").html(data["message"]);
					}
				}
				/*
				beforeSend:function(){//这个函数执行的时机在发送请求之前
									  //如果这个函数返回值为true，在会真正向后端发送请求
									  //如果是false，就放弃发送
					
				}
				*/
			})
		});
	});
</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="${pageContext.request.contextPath}/image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="${pageContext.request.contextPath}/workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" id="username" type="text" value="${cookie.loginAct.value}" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" id="password" type="password" value="${cookie.loginPwd.value}" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<c:if test="${not empty cookie.loginAct and not empty cookie.loginPwd}">
								<input type="checkbox" id="isRemPwd" checked/>
							</c:if>
							<c:if test="${empty cookie.loginAct or empty cookie.loginPwd}">
								<input type="checkbox" id="isRemPwd"/>
							</c:if>
							 十天内免登录
						</label>
						&nbsp;&nbsp;
						<span id="msg"></span>
					</div>
					<button type="button" id="loginBut" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>