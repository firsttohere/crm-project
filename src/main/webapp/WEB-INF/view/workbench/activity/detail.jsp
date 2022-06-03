<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta charset="UTF-8">

<link href="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	var basePath = "${pageContext.request.contextPath}";
	
	var nowActivityId = "${requestScope.selectedActivity.activityId }";
	var nowActivityName = "${requestScope.selectedActivity.activityName }";
	
	bind = function(){
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});

		//给删除超链接绑定事件
		$(".deleteHref").click(function(){
			//查看当前超连接的value值
			var remarkId = $(this).prop("id").substring(6);
			//向后端发删除请求
			$.ajax({
				url:basePath+'/workbench/activityRemark/delete?id='+remarkId,
				type:'get',
				success:function(result){
					if(result["code"] == "0"){
						alert(result["message"]);
					}else{
						//成功后，刷新备注列表,删除对应的div,调用jquery对象的remove()
						$("#div"+remarkId).remove();
					}
				}
			});
		});
		
		//给修改超链接绑定事件
		$(".editHref").click(function(){
			//显示模态窗口
			$("#editRemarkModal").modal("show");
			//获取用户想要编辑的备注id
			var remarkId = $(this).prop("id").substring(4);
			//给模态窗口的更新按钮绑定事件
			$("#updateRemarkBtn").click(function(){
				//确保修改后的备注内容不为空
				//	*备注内容不能为空
				var content = $("#noteContent").val();
				if(content == ""){
					alert("备注不能为空");
					return;
				}
				//发送ajax请求
				$.ajax({
					url:basePath+'/workbench/activityRemark/update',
					type:'post',
					data:{
						id:remarkId,
						content:content
					},
					dataType:'json',
					//  *修改成功之后,关闭模态窗口,刷新备注列表
					//  *修改失败,提示信息,模态窗口不关闭,列表也不刷新
					success:function(result){
						if(result["code"] == "0"){
							alert(result["message"]);
						}else{
							var remark = result["otherData"];//修改后的remark
							var s = "<img title=\""+remark.arCreateBy+"\" src=\""+basePath+"/image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">"+
									"<div style=\"position: relative; top: -40px; left: 40px;\" >"+
										"<h5>"+remark.arNoteContent+"</h5>"+
										"<font color=\"gray\">市场活动</font> <font color=\"gray\">-</font> <b>"+nowActivityName+"</b> <small style=\"color: gray;\"> "+remark.arEditTime+" 由"+remark.arEditBy+"修改</small>"+
										"<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">"+
											"<a class=\"myHref editHref\" id=\"edit"+remark.activityRemarkId+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>"+
											"&nbsp;&nbsp;&nbsp;&nbsp;"+
											"<a class=\"myHref deleteHref\" id=\"delete"+remark.activityRemarkId+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>"+
										"</div>"+
									"</div>";
							$("#div"+remarkId).html(s);
							bind();
							$("#editRemarkModal").modal("hide");
						}
					}
				});
				
			});
		});
	}
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		bind();
		
		
		//给保存按钮添加事件
		$("#saveRemarkBtn").click(function(){
			//保证文本域中的数据不为空
			var content = $("#remark").val();
			if(content == ""){
				alert("备注不能为空！");
				return;
			}
			//获取当前是哪个activity
			
			//发送ajax请求
			$.ajax({
				url:'${pageContext.request.contextPath}/workbench/activityRemark/save',
				type:'post',
				data:{
					arNoteContent:content,
					activityId:nowActivityId
				},
				dataType:'json',
				success:function(result){
					//前端需要知道成功还是失败
					if(result["code"] == "0"){
						//失败就提示错误信息
						alert(result["message"]);
					}else{
						
						//成功，在otherData中存放刚刚添加进去的这个activityRemark对象
						var remark = result["otherData"];
						var remarkDiv = $("#remarkDiv");
						var s = 
						"<div class=\"remarkDiv\" style=\"height: 60px;\" id=\"div"+remark.activityRemarkId+"\">"+
							"<img title=\""+remark.arCreateBy+"\" src=\""+basePath+"/image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">"+
							"<div style=\"position: relative; top: -40px; left: 40px;\" >"+
								"<h5>"+remark.arNoteContent+"</h5>"+
								"<font color=\"gray\">市场活动</font> <font color=\"gray\">-</font> <b>"+nowActivityName+"</b> <small style=\"color: gray;\"> "+remark.arCreateTime+" 由"+remark.arCreateBy+"创建</small>"+
								"<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">"+
									"<a class=\"myHref editHref\" id=\"edit"+remark.activityRemarkId+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>"+
									"&nbsp;&nbsp;&nbsp;&nbsp;"+
									"<a class=\"myHref deleteHref\" id=\"delete"+remark.activityRemarkId+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>"+
								"</div>"+
							"</div>"+
						"</div>";
						remarkDiv.before(s);
						bind();
						//清空文本域
						$("#remark").val("");
						$("#cancelBtn").click();
					}
				}
			});
		});
	});
	
</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
	
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

    

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${requestScope.selectedActivity.activityName} <small>${requestScope.selectedActivity.activityStartDate} ~ ${requestScope.selectedActivity.activityEndDate}</small></h3>
		</div>
		
	</div>
	
	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.selectedActivity.activityOwner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestScope.selectedActivity.activityName}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.selectedActivity.activityStartDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestScope.selectedActivity.activityEndDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.selectedActivity.activityCost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestScope.selectedActivity.activityCreateBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${requestScope.selectedActivity.activityCreateTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestScope.selectedActivity.activityEditBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${requestScope.selectedActivity.activityEditTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${requestScope.selectedActivity.activityDescription}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 30px; left: 40px;" id="allremarks">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<c:forEach var="remark" items="${requestScope.remarks}" >
			<div class="remarkDiv" style="height: 60px;" id="div${remark.activityRemarkId }">
				<img title="${remark.arCreateBy }" src="${pageContext.request.contextPath}/image/user-thumbnail.png" style="width: 30px; height:30px;">
				<div style="position: relative; top: -40px; left: 40px;" >
					<h5>${remark.arNoteContent }</h5>
					<font color="gray">市场活动</font> <font color="gray">-</font> <b>${requestScope.selectedActivity.activityName}</b> <small style="color: gray;"> ${remark.arCreateTime } 由${remark.arCreateBy }</small>
					<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
						<a class="myHref editHref" id="edit${remark.activityRemarkId }" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a class="myHref deleteHref" id="delete${remark.activityRemarkId }" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
					</div>
				</div>
			</div>
		</c:forEach>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>