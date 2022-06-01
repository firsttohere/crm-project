<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta charset="UTF-8">

<link href="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/bs_pagination-master/localization/en.min.js"></script>
<!-- jquery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.11.1-min.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/bs_pagination-master/js/jquery.bs_pagination.js"></script>



<script type="text/javascript">

	isSelectAll = function(){
		var is = document.getElementById("all");
		$("input[type='checkbox']").prop("checked",is.checked);
	}

	showPageDivF = function(recordsCount,pageSize,pageNo){
		var totalPages = Math.ceil(recordsCount / pageSize);
		var visible = Math.min(totalPages,5);
		$("#showPageDiv").bs_pagination({
			currentPage:pageNo,//当前显示第几页
			totalPages:totalPages,//总页数
			rowsPerPage:pageSize,//每页显示条数
			visiblePageLinks:visible,
			showGoToPage:true,
			showRowsPerPage:true,
			showRowsInfo:true,
			
			onChangePage:function(event,pageObj){
				//每次切换页面就会回调这个函数
				//可以通过pageObj.currentPage获取切换后的页数，pageObj.rowsPerPage获取当前每页显示条数
				requestIpage(pageObj.currentPage,pageObj.rowsPerPage);

			}
		});
		//把全选复选框重置
		$("#all").prop("checked",false);
	}
	
	//定义一个函数，向后端发送第i页的请求,并且处理数据，一页默认显示10条记录
	requestIpage = function(pageNo,pageSize){
	
		var name = $("#query-name").val();
		var owner = $("#query-owner").val();
		var startDate = $("#query-startDate").val();
		var endDate = $("#query-endDate").val();
		$.ajax({
			url:'${pageContext.request.contextPath}/workbench/activity/query',
			data:{
				pageNo:pageNo,
				pageSize:pageSize,
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate
			},
			type:'post',
			dataType:'json',
			success:function (result){
				//后端返回一个list，和总记录条数
				//获取数据
				var count = result["count"];
				var activities = result["activities"];
				//展示总页数
				//$("#totalCounts").html(count);
				//展示所有记录
				if(activities != null){
					var s = "";
					$.each(activities,function(index,obj){
						s += "<tr class=\"active\"><td><input type=\"checkbox\" value=\""+obj.activityId+"\"/></td><td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='detail.html';\">"+obj.activityName+"</a></td><td>"+obj.activityOwner+"</td><td>"+obj.activityStartDate+"</td><td>"+obj.activityEndDate+"</td></tr>"
					});
					$("#myshowbody").html(s);
				}else{
					$("#myshowbody").html("");
				}
				//给每一条记录加单击事件，必须在动态html生成后才可以，必须在这里加
				$("#myshowbody input[type='checkbox']").click(function(){
					//如果列表中的所有checkbox是不是都处于选中状态，则全选按钮也选中
					//获得所有的checkbox对象
					var allcheckbox = $("#myshowbody input[type='checkbox']").size();
					//获得所有被选中的checkbox
					var checkedcount = $("#myshowbody input[type='checkbox']:checked").size();
					document.getElementById("all").checked = (allcheckbox == checkedcount);
				});
				showPageDivF(count,pageSize,pageNo);
			}
			
		});
	}
	

	$(function(){
		
		//页面加载完毕后，向后端发送ajax请求，希望返回活动表中的记录条数，以及第1页的数据
		requestIpage(1,10);
		
		//给查询按钮添加单击事件
		$("#query-activitybtn").click(function(){
			requestIpage(1,$("#showPageDiv").bs_pagination('getOption','rowsPerPage'));
		});
		
		//给修改按钮绑定单击事件
		$("#modifybtn").click(function(){
			//看看用户选中了哪个活动
			var selectCount = $("#myshowbody input[type='checkbox']:checked").size();
			if(selectCount != 1){
				alert("一次只能修改一条活动，确保你只选择了一条");
				return;
			}
			//获取用户选择的活动id
			var id = $("#myshowbody input[type='checkbox']:checked").val();
			$.ajax({
				url:'${pageContext.request.contextPath}/workbench/activity/queryById?id='+id,
				type:'get',
				success:function(activity){
					//获得activity的id，以便后面的修改
					var id = activity.activityId;
					//后台返回一个activity对象
					//展示在模态窗口上
					$("#edit-marketActivityOwner").val(activity.activityOwner);
					$("#edit-marketActivityName").val(activity.activityName);
					$("#edit-startTime").val(activity.activityStartDate);
					$("#edit-endTime").val(activity.activityEndDate);
					$("#edit-cost").val(activity.activityCost);
					$("#edit-describe").val(activity.activityDescription);
					
					$("#editActivityModal").modal("show");
					//给修改模态窗口的更新按钮，和关闭按钮绑定事件
					$("#updateActivityBtn").click(function(){
						//检验更新后的数据是否合法
						//确保名称不为空
						var name = $("#edit-marketActivityName").val();
						if(name == ""){
							alert("名称不能为空");
							return;
						}
						//startDate < endDate,其格式已经将input做了只读处理，和日期插件
						var startDate = $("#edit-startTime").val();
						var endDate = $("#edit-endTime").val();
						if(startDate != "" && endDate != "" && startDate > endDate){
							alert("确保开始日期在结束日期之前");
							return;
						}
						var owner = $("#edit-marketActivityOwner").val();
						var cost = $("#edit-cost").val();
						var description = $("#edit-describe").val();
						
						if(owner == activity.activityOwner &&
						   name == activity.activityName &&
						   startDate == activity.activityStartDate &&
						   endDate == activity.activityEndDate &&
						   cost == activity.activityCost &&
						   description == activity.activityDescription){
							//不发请求,关闭模态窗口
							$("#editActivityModal").modal("hide");
							return;
						}
						//向后端发送修改请求
						$.ajax({
							url:'${pageContext.request.contextPath}/workbench/activity/update',
							data:{
								activityId:id,
								activityOwner:owner,
								activityName:name,
								activityStartDate:startDate,
								activityEndDate:endDate,
								activityCost:cost,
								activityDescription:description
							},
							type:'post',
							dataType:'json',
							success:function(result){
								//更新完毕，
								if(result["code"] == "1"){
									//更新数据
									var curPage = $("#showPageDiv").bs_pagination('getOption','currentPage');
									requestIpage(curPage,$("#showPageDiv").bs_pagination('getOption','rowsPerPage'));
									//如果成功，就关闭模态窗口
									$("#editActivityModal").modal("hide");
								}else{
									//失败提示一下
									alert(result["message"]);
								}
							}
						});
					});
					
				}
				
			});
		});
		
		//给删除按钮绑定单级事件
		$("#deletebtn").click(function(){
			//提示是否删除
			if(window.confirm("亲，删除后不可恢复哦！")){
				//真的要删除，先查出用户选择了哪些
				var arr = [];
				$("#myshowbody input[type='checkbox']:checked").each(function(i){
					arr[i] = $(this).val();
				});
				//拿到数组，发送请求
				if(arr.length == 0){
					alert("arr.length = 0");
					return;
				}
				$.ajax({
					url:'${pageContext.request.contextPath}/workbench/activity/delete',
					data:{ids:arr},
					traditional:true,
					type:'post',
					dataType:'json',
					success:function(result){
						//处理相应的结果
						//result={code:"0|1",message:" |删除失败"}
						//如果删除成功，发送查询请求，从新加载tbody
						if(result["code"] == "1"){
							//如果当前是最后一页，并且这一页的全选复选框是选中状态，就跳掉前一页
							var curPage = $("#showPageDiv").bs_pagination('getOption','currentPage');
							var curTotalPage = $("#showPageDiv").bs_pagination('getOption','totalPages');
							var isAll = $("#all").prop("checked");
							if(curPage != 1 && curPage == curTotalPage && isAll){
								requestIpage(curPage - 1,$("#showPageDiv").bs_pagination('getOption','rowsPerPage'));
							}else{
								requestIpage(curPage,$("#showPageDiv").bs_pagination('getOption','rowsPerPage'));
							}
						}else{
							//删除失败
							alert(result["message"]);
						}
					}
				});
			}
		});
		
		//绑定日历插件
		$(".mydate").datetimepicker({
			language:'zh-CN',//语言
			format:'yyyy-mm-dd',
			minView:'month',
			initialDate:new Date(),
			autoclose:true,
			todayBtn:true,
			clearBtn:true
		});
		
		//cost输入框只允许，输入数字
		checkInput = function(){
			var keyCode = event.keyCode;
			var nowVal = event.srcElement.value;
			if(keyCode == 8){
				return;
			}
			if(nowVal == ""){
				if(keyCode < 49 || keyCode > 57){
					event.returnValue = false;
				}
			}else{
				if(keyCode < 48 || keyCode > 57){
					event.returnValue = false;
				}
			}
		}
		
		
		//给按钮btn btn-primary绑定单击保存事件
		
		
		savebut = function(){
			//获取参数
			var owner = $("#create-marketActivityOwner").val();
			var name = $.trim($("#create-marketActivityName").val());
			var startTime = $("#create-startTime").val();
			var endTime = $("#create-endTime").val();
			var cost = $.trim($("#create-cost").val());
			var description = $.trim($("#create-describe").val());
			//确保数据合法
			//所有者owner和名称name不能为""
			if(owner == "" || name == ""){
				alert("拥有者和活动名称都不能为空！");
				return;
			}
			var regExp = /^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[13579][26])00))-02-29)$/g;
			var startOk = regExp.test(startTime);
			var regExp2 = /^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[13579][26])00))-02-29)$/g;			
			var endOk = regExp2.test(endTime);
			if(startTime != "" && !startOk){
				alert("开始时间格式不对！");
				return;
			}
			if(endTime != "" && !endOk){
				alert("结束时间格式不对！");
				return;
			}
			if(startTime != "" && endTime != "" && startTime > endTime){
				alert("开始时间不能比结束时间大");
				return;
			}
			//提交请求
			$.ajax({
				url:'${pageContext.request.contextPath}/workbench/activity/create/save',
				data:{
					activityOwner:owner,
					activityName:name,
					activityStartDate:startTime,
					activityEndDate:endTime,
					activityCost:cost,
					activityDescription:description
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if(data["code"] == "1"){
						//活动添加成功后，关闭当前的模态窗口，刷新当前页面的列表
						//删除表单中的数据，触发关闭按钮
						document.getElementById("createActivityForm").reset();
						$("#closebut").click();
						//后端把添加后的Activity对象放在data的otherDate中
						requestIpage(1,$("#showPageDiv").bs_pagination('getOption','rowsPerPage'));
					}else{
						//显示错误提示信息
						alert(data["message"]);
					}
				}
			});
		}
		
		//文件下载（导出）
		$("#exportActivityAllBtn").click(function(){
			//向后台发送导出的请求
			//后台查询所有的activity
			//创建一个excel文件，把市场活动写到excel文件
			//把excel文件通过网络传输，到浏览器
			window.location.href = "${pageContext.request.contextPath}/workbench/activity/filedownload";
		});
	});
	
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" id="createActivityForm" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <c:forEach items="${requestScope.users}" var="user">
								  	<option value="${user.userId}">${user.divName}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost" onkeydown="checkInput()">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" id="closebut" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" onclick="savebut()">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <c:forEach items="${requestScope.users}" var="user">
								  	<option value="${user.userId}">${user.divName}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="edit-startTime" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="edit-endTime" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control mydate" type="text" id="query-startDate" readonly/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control mydate" type="text" id="query-endDate" readonly/>
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="query-activitybtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createActivityModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="modifybtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deletebtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="all" onclick="isSelectAll()"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="myshowbody">
						
					</tbody>
				</table>
				<div id="showPageDiv">
				
				</div>
			</div>
			
			
			
		</div>
		
	</div>
</body>
</html>