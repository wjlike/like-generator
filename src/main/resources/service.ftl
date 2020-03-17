package ${interfacetargetPackage};

import com.baomidou.mybatisplus.extension.service.IService;
import ${targetPackage}.${domainObjectName};
import com.lzl.utils.PageUtils;
/**
 * ${classDesc}
 * Author ${author}  ${curDate}
 * version 1.0.0
 */
public interface ${samllDomainObjectName}Service extends IService<${domainObjectName}> {
    /**
     * 主键ID  ${curDate}
     * @Author ${author}
     * @param ${samllDomainObjectName}Id
     * @return
     */
    ${domainObjectName} get${domainObjectName}ById(String ${samllDomainObjectName}Id);

    /**
     * 分页查询
     * @Author ${author}
     * ${curDate}
     * @param dto  查询条件
     * @return
     */
    PageUtils queryListByPage(${domainObjectName} ${samllDomainObjectName},int page, int limit);
}
