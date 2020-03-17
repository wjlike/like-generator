<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${shortName}List</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="../../static/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="../../static/layuiadmin/lib/extend/dropdown/dropdown.css" media="all">
    <link rel="stylesheet" href="../../static/hska1che/css/hscommon.css" media="all">
</head>
<body>
<blockquote class="layui-elem-quote quoteBox hs-search">
    <form class="layui-form" id="${shortName}QueryForm">
    	<#list fieldMaps?keys as key>
    		<#if (key_index)%4== 0>
			<div class="layui-form-item">
			</#if>
	    		<div class="layui-inline">
	                <label class="layui-form-label">${fieldMaps[key]}:</label>
	                <div class="layui-input-inline">
	                    <input class="layui-input" name="${key}" autocomplete="off">
	                </div>
	            </div>
            <#if (key_index)%4==3||!key_has_next>
        	</div>
            </#if>
        </#list> 
         <a class="layui-btn search_btn layui-btn-sm" data-type="reload">搜索</a>
    </form>
</blockquote>

<table class="layui-hide" id="${shortName}Table" lay-filter="${shortName}Table"></table>
<script src="../../static/layuiadmin/layui/layui.js"></script>
<script type="text/html" id="${shortName}Tool">
	<a class="layui-btn layui-btn-warm  layui-btn-xs" lay-event="${shortName}Edit">编辑</a>
</script>
<script type="text/html" id="${shortName}ToolBar">
	<div class="layui-inline">
		<a class="layui-btn layui-btn-normal add_btn" lay-event="${shortName}Add">添加</a>
	</div>
</script>
<script src="../../script/${shortName}/${shortName}.js"></script>
<script type="text/javascript">
    var basePath = '<%=basePath%>';
</script>
</body>
</html>