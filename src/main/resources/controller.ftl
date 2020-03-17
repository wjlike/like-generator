package ${impltargetPackage};

import com.lzl.commom.R;
import ${interfacetargetPackage}.${domainObjectName}Server;
import ${mapperTargetPackage}.${domainObjectName};
import com.lxl.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.lzl.utils.PageUtils;
import com.alibaba.fastjson.JSONObject;
/**
 * ${classDesc}
 * Author like  ${curDate}
 * version 1.0.0
 */

@Controller
@Slf4j
@RequestMapping("/${samllDomainObjectName}")
public class ${domainObjectName}Controller {

    @Autowired
    private ${domainObjectName}Service ${samllDomainObjectName}Service;


    /**
     * @Author ${author}
     * @Description 查询${samllDomainObjectName}信息
     * @Date ${curDate}
     * @Param [${samllDomainObjectName}, page]
     * @return java.lang.String
     */
    @RequestMapping(value = "/listByPage")
    @ResponseBody()
    public R listByPage(@RequestBody${domainObjectName} ${samllDomainObjectName},int page, int limit){
        log.info("查询参数"+JSONObject.toJSONString(${samllDomainObjectName}));
        PageUtils pageutil= ${samllDomainObjectName}Service.queryListByPage(${samllDomainObjectName},page,limit);
        return R.ok().put("page",pageutil);
    }

    /**
     * @Author ${author}
     * @Description 新增保存${samllDomainObjectName}信息
     * @Date ${curDate}
     * @Param [${samllDomainObjectName}]
     * @return java.lang.String
     */
    @RequestMapping(value = "/add",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody()
    public R doAdd(@RequestBody() ${domainObjectName} ${samllDomainObjectName}){
        log.info("新增参数"+JSONObject.toJSONString(${samllDomainObjectName}));
·       try{
            ${samllDomainObjectName}Service.save(${samllDomainObjectName});
            return R.ok();
        }catch (Exception e) {
            e.printStackTrace();
            return R.error(1,"服务器异常，请联系管理员");
        }
    }

    /**
     * @Author ${author}
     * @Description 根据Id查询${samllDomainObjectName}信息
     * @Date ${curDate}
     * @Param id
     * @return R
     */
    @RequestMapping(value = "/getInfoById",method = {RequestMethod.POST,RequestMethod.GET})
    public R get${domainObjectName}ById(String id){
        if("".equals(id)){
            return R.error(2,"Id为空,无法获取信息");
        }
        ${domainObjectName} ${samllDomainObjectName} = ${samllDomainObjectName}Service.get${domainObjectName}ById(id);

        if(${samllDomainObjectName} == null){
            return R.error(2,"查询失败，无Id值为"+id+"信息");
        }
        return  R.ok().put("data",${samllDomainObjectName});
    }

    /**
     * @Author ${author}
     * @Description 修改保存${samllDomainObjectName}信息
     * @Date ${curDate}
     * @Param [${samllDomainObjectName}]
     * @return R
     */
    @RequestMapping(value = "/edit",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody()
    public R doEdit(@RequestBody() ${domainObjectName} ${samllDomainObjectName}){
        log.info("新增参数"+JSONObject.toJSONString(${samllDomainObjectName}));
        try{
            ${samllDomainObjectName}Service.updateById(${samllDomainObjectName});
            return R.ok();
        }catch (Exception e) {
            e.printStackTrace();
            return R.error(1,"服务器异常，请联系管理员");
        }
    }
}
