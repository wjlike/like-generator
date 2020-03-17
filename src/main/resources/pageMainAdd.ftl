<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<form class="layui-form" autocomplete="off" id="add${name}FormId" style="margin-top: 15px;">
    	<#list fieldMaps?keys as key>
    		<#if (key_index)%2== 0>
			<div class="layui-form-item">
			</#if>
	    		<div class="layui-inline">
	                <label class="layui-form-label"><font color="red">*</font>${fieldMaps[key]}:</label>
	                <div class="layui-input-inline">
	                    <input type="text" class="layui-input" id="${key}" name="${key}" lay-verify="required" placeholder="请输入${fieldMaps[key]}">
	                </div>
	            </div>
            <#if (key_index)%2==1||!key_has_next>
        	</div>
            </#if>
        </#list>
        <div class="layui-form-item">
	        <div class="layui-input-block">
	            <a class="layui-btn" lay-filter="do${name}Add" lay-submit id="do${name}Add"  style="margin-left: 90px;margin-top: 30px;">提交</a>
	        </div>
    	</div>
</form>
<script type="text/javascript">
    layui.config({
        base: '../../script/common/' //静态资源所在路径
    }).use(['form','layer', 'laydate','base'],function(){
        var form = layui.form,
            layer = parent.layer === undefined ? layui.layer : top.layer, base=layui.base,jQuery = layui.jquery;
        	jQuery('#do${name}Add').click(function () {
            var params = base.getFromByJson("add${name}FormId");
            base.sendByJson('${shortName}/do${name}Add',JSON.stringify(params), function (data) {
                if(data.flag == '0'){
                    base.successConfig(data.msg);
                    base.closeAllPageDialog();
                    if (typeof  window.refresh${name}Table == 'function') {
                        window.refresh${name}Table.call(this);
                    }
                }else{
                    base.errorConfig(data.msg);
                }
            });
        });
    });
</script>
</html>