layui.config({
    base: '../../script/common/' //静态资源所在路径
}).extend({
    dropdown: '{/}../../static/layuiadmin/lib/extend/dropdown/dropdown'
}).use(['form', 'layer', 'laydate', 'table','base', 'element','dropdown'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery, laydate = layui.laydate,  base=layui.base,
        table = layui.table,element = layui.element,dropdown=layui.dropdown;

        //表格列定义
    var  cols = [[
        <#list fieldMaps?keys as key>
			<#if key!='serialVersionUID'>
				<#if key?contains('Time')||key?contains('time')>
					{field: '${key}', title: '${fieldMaps[key]}', align: 'center',width:160,templet:function (d) {return base.format2Time(d.createTime)}},
				<#else>
					{field: '${key}', title: '${fieldMaps[key]}', align: 'center'},
				</#if>
			</#if>
        </#list>
		{field: 'id', title: '操作', align: 'center',toolbar: '#${shortName}Tool',fixed: 'right'}
    ]];
    //表格渲染
    table.render({
        elem: '#${shortName}Table',
        id:'${shortName}TableId',
        page:true,
        height:'full-155',
        cols:cols,
        url: basePath + '${shortName}/${shortName}List.do',
        toolbar:'#${shortName}ToolBar',
        defaultToolbar:['filter']
    });
	
	/**
	 *日期框初始化
	 */
	base.universalDate("startTime");
	base.universalDate("endTime");


    //搜索
    $(".search_btn").on("click", function () {
        //执行重载
        tableReload();
    });

    function exportExcel() {
        var param = base.formToObject("${shortName}QueryForm")
        var chkTime = base.compareDate(param.startTime,param.endTime);
        if(!chkTime){
            return false;
        }
        var param = "?startTime="+param.startTime
            +"&endTime="+param.endTime;
        base.popConfirm("你确定要导出数据吗?",function () {
            document.getElementById("YOU EXPORT URL IFREAM ID").src="YOU EXPOPRT URL"+param;
            base.successConfig("文件导出成功");
        });
    }

    function tableReload(){
        table.reload('${shortName}TableId', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where:base.formToObject("${shortName}QueryForm")
        });
    }
    
    window.refresh${name}Table = function () {
        tableReload();
    };

    table.on('tool(${shortName}Table)', function (obj) {
        if(obj.event === '${shortName}Edit'){
            ${shortName}Edit(obj.data);
        }

    });

    table.on('toolbar(${shortName}Table)', function (obj) {
        if (obj.event === '${shortName}Export') {
            exportExcel();
        }else if (obj.event === '${shortName}Add') {
            ${shortName}Add();
        }
    });

    $("body").keydown(function(){
        if(event.keyCode=="13"){
            tableReload();
        }
    });

    function ${shortName}Add() {
        base.sendByHtml("${shortName}/${shortName}Add.do",'',function(res){
            base.openModal(res,{title:"新增",area:['700px', '400px']});
        });
    }

    function ${shortName}Edit(id) {
        base.sendByHtml("${shortName}/${shortName}Edit.do?id="+id,'',function(res){
            base.openModal(res,{title:"修改",area:['700px', '400px']});
        });
    }
});