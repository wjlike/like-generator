package ${impltargetPackage};

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${targetPackage}.${domainObjectName};
import ${targetPackageService}.${domainObjectName}Service;
import ${targetPackageService}.${domainObjectName}Dao;
import com.lzl.base.BaseServiceImpl;
import com.lzl.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * ${classDesc}
 * Author ${author}  ${curDate}
 * version 1.0.0
 */
@Slf4j
@Service
public class ${domainObjectName}ServiceImpl extends BaseServiceImpl<${domainObjectName}Dao ,${domainObjectName}>  implements ${domainObjectName}Service {


    @Autowired
    private ${domainObjectName}Dao ${samllDomainObjectName}Dao;


    /**
     * 主键ID
     * @Author ${author}
     * ${curDate}
     * @param ${samllDomainObjectName}Id
     * @return
     */
    @Override
    public ${domainObjectName} getInfoById(String id) {
        return  ${samllDomainObjectName}Dao.selectById(id);
    }

    /**
     * 分页查询
     * @Author ${author}
     * ${curDate}
     * @param dto  查询条件
     * @return
     */
    @Override
    public PageUtils queryListByPage(${domainObjectName} ${samllDomainObjectName},int page, int limit) {
    Page<${domainObjectName}> p = new Page<${domainObjectName}>(page,limit);
        QueryWrapper<${domainObjectName}> qw = new QueryWrapper<${domainObjectName}>();
        List<${domainObjectName}> list = new ArrayList<${domainObjectName}>();
        int  count = userDao.selectCount(qw);
        if(count>0){
            list = (List<${domainObjectName}>)userDao.selectMapsPage(p,qw);
         }
         p.setTotal(count);
         p.setRecords(list);
         return new PageUtils(p);
    }
}