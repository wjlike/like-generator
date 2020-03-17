package com.like.generate;
import static org.mybatis.generator.internal.util.ClassloaderUtility.getCustomClassloader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import freemarker.cache.ClassTemplateLoader;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.NullProgressCallback;
import org.mybatis.generator.internal.ObjectFactory;

import freemarker.template.Template;
import freemarker.template.TemplateException;
public class Generate {

	private static String author = "like";

	private static List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<GeneratedJavaFile>();

	private static List<GeneratedXmlFile> generatedXmlFiles = new ArrayList<GeneratedXmlFile>();

	private static List<String> warnings = new ArrayList<String>();


	public void Create(Class clazz) throws URISyntaxException, SQLException, InterruptedException {
		Long startTime = System.currentTimeMillis();
		try {
			List<String> warnings = new ArrayList<String>();
			boolean overwrite = true;
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = classloader.getResourceAsStream("generatorConfig.xml");
			ConfigurationParser cp = new ConfigurationParser(warnings);
			Configuration config = cp.parseConfiguration(is);
			List<TableConfiguration> tableConfigurations = config.getContexts().get(0).getTableConfigurations();
			String targetPackage = config.getContexts().get(0).getJavaModelGeneratorConfiguration().getTargetPackage();
			String targetProject = config.getContexts().get(0).getJavaModelGeneratorConfiguration().getTargetProject();
			System.out.println(tableConfigurations);
			DefaultShellCallback callback = new DefaultShellCallback(overwrite);
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
			myBatisGenerator.generate(null);
			try {
				generJavaFileList(config,null,null,null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			generateServerClass(targetPackage,targetProject,tableConfigurations,clazz);
			System.out.println("生成结束");
        /*} catch (SQLException e) {
            e.printStackTrace();*/
		} catch (IOException e) {
			e.printStackTrace();
        /*} catch (InterruptedException e) {
            e.printStackTrace();*/
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (XMLParserException e) {
			e.printStackTrace();
		}
		System.out.println("耗时:"+(System.currentTimeMillis()-startTime));
	}

	private static void generatePageMainJs(freemarker.template.Configuration configuration, List<TableConfiguration> tableConfigurations, String targetPackage, String targetProject) {
		Template t=null;
		try {
			t = configuration.getTemplate("pageMainJs.ftl"); //获取模板文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(GeneratedJavaFile gJavaFile:generatedJavaFiles){
			TopLevelClass c = (TopLevelClass) gJavaFile.getCompilationUnit();
			String shortName = c.getType().getShortName();;
			String pathname = targetProject+"\\"+targetPackage.replace("entity", "pageInfo").replace(".","//")+"/"+toLowerCaseFirstOne(shortName);
			File destDir = new File(pathname);
			if(!destDir.exists()){
				destDir.mkdirs();
			}
			Writer out = null;
			List<Field> fields = c.getFields();
			File outFile = new File(pathname+"/"+toLowerCaseFirstOne(shortName)+".js");
			Map<String,String> fieldMaps = new HashMap<String, String>();
			for(Field field:fields){
				fieldMaps.put(field.getName(), field.getJavaDocLines().get(1).replace("*", "").replace(",", "").trim());
			}
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("shortName", toLowerCaseFirstOne(shortName));
			dataMap.put("name",shortName);
			dataMap.put("fieldMaps", fieldMaps);
			dataMap.put("author", author);
			dataMap.put("curDate", DateTimeUtil.getTime(new Date(), "yyyy-MM-dd hh:mm:ss"));
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void generatePageMain(freemarker.template.Configuration configuration, List<TableConfiguration> tableConfigurations, String targetPackage, String targetProject) {
		Template t=null;
		try {
			t = configuration.getTemplate("pageMain.ftl"); //获取模板文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(GeneratedJavaFile gJavaFile:generatedJavaFiles){
			TopLevelClass c = (TopLevelClass) gJavaFile.getCompilationUnit();
			String shortName = c.getType().getShortName();;
			String pathname = targetProject+"\\"+targetPackage.replace("entity", "pageInfo").replace(".","//")+"/"+toLowerCaseFirstOne(shortName);
			File destDir = new File(pathname);
			if(!destDir.exists()){
				destDir.mkdirs();
			}
			Writer out = null;
			List<Field> fields = c.getFields();
			File outFile = new File(pathname+"/"+toLowerCaseFirstOne(shortName)+"Main.jsp");
			Map<String,String> fieldMaps = new HashMap<String, String>();
			for(Field field:fields){
				fieldMaps.put(field.getName(), field.getJavaDocLines().get(1).replace("*", "").replace(",", "").trim());
			}
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("shortName", toLowerCaseFirstOne(shortName));
			dataMap.put("fieldMaps", fieldMaps);
			dataMap.put("name",shortName);
			dataMap.put("author", author);
			dataMap.put("curDate", DateTimeUtil.getTime(new Date(), "yyyy-MM-dd hh:mm:ss"));
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void generatePageAdd(freemarker.template.Configuration configuration, List<TableConfiguration> tableConfigurations, String targetPackage, String targetProject) {
		Template t=null;
		try {
			t = configuration.getTemplate("pageMainAdd.ftl"); //获取模板文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(GeneratedJavaFile gJavaFile:generatedJavaFiles){
			TopLevelClass c = (TopLevelClass) gJavaFile.getCompilationUnit();
			String shortName = c.getType().getShortName();;
			String pathname = targetProject+"\\"+targetPackage.replace("entity", "pageInfo").replace(".","//")+"/"+toLowerCaseFirstOne(shortName);
			File destDir = new File(pathname);
			if(!destDir.exists()){
				destDir.mkdirs();
			}
			Writer out = null;
			List<Field> fields = c.getFields();
			File outFile = new File(pathname+"/"+toLowerCaseFirstOne(shortName)+"Add.jsp");
			Map<String,String> fieldMaps = new HashMap<String, String>();
			for(Field field:fields){
				fieldMaps.put(field.getName(), field.getJavaDocLines().get(1).replace("*", "").replace(",", "").trim());
			}
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("shortName", toLowerCaseFirstOne(shortName));
			dataMap.put("fieldMaps", fieldMaps);
			dataMap.put("name",shortName);
			dataMap.put("author", author);
			dataMap.put("curDate", DateTimeUtil.getTime(new Date(), "yyyy-MM-dd hh:mm:ss"));
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void generatePageEdit(freemarker.template.Configuration configuration, List<TableConfiguration> tableConfigurations, String targetPackage, String targetProject) {
		Template t=null;
		try {
			t = configuration.getTemplate("pageMainEdit.ftl"); //获取模板文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(GeneratedJavaFile gJavaFile:generatedJavaFiles){
			TopLevelClass c = (TopLevelClass) gJavaFile.getCompilationUnit();
			String shortName = c.getType().getShortName();;
			String pathname = targetProject+"\\"+targetPackage.replace("entity", "pageInfo").replace(".","//")+"/"+toLowerCaseFirstOne(shortName);
			File destDir = new File(pathname);
			if(!destDir.exists()){
				destDir.mkdirs();
			}
			Writer out = null;
			List<Field> fields = c.getFields();
			File outFile = new File(pathname+"/"+toLowerCaseFirstOne(shortName)+"Edit.jsp");
			Map<String,String> fieldMaps = new HashMap<String, String>();
			for(Field field:fields){
				fieldMaps.put(field.getName(), field.getJavaDocLines().get(1).replace("*", "").replace(",", "").trim());
			}
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("shortName", toLowerCaseFirstOne(shortName));
			dataMap.put("fieldMaps", fieldMaps);
			dataMap.put("name",shortName);
			dataMap.put("author", author);
			dataMap.put("curDate", DateTimeUtil.getTime(new Date(), "yyyy-MM-dd hh:mm:ss"));
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void generJavaFileList(Configuration configuration,ProgressCallback callback, Set<String> contextIds,
										  Set<String> fullyQualifiedTableNames) throws Exception{
		if (callback == null) {
			callback = new NullProgressCallback();
		}

		generatedJavaFiles.clear();

		// calculate the contexts to run
		List<Context> contextsToRun;
		if (contextIds == null || contextIds.size() == 0) {
			contextsToRun = configuration.getContexts();
		} else {
			contextsToRun = new ArrayList<Context>();
			for (Context context : configuration.getContexts()) {
				if (contextIds.contains(context.getId())) {
					contextsToRun.add(context);
				}
			}
		}

		// setup custom classloader if required
		if (configuration.getClassPathEntries().size() > 0) {
			ClassLoader classLoader = getCustomClassloader(configuration.getClassPathEntries());
			ObjectFactory.addExternalClassLoader(classLoader);
		}

		// now run the introspections...
		int totalSteps = 0;
		for (Context context : contextsToRun) {
			totalSteps += context.getIntrospectionSteps();
		}
		callback.introspectionStarted(totalSteps);

		for (Context context : contextsToRun) {
			context.introspectTables(callback, warnings,
					fullyQualifiedTableNames);
		}

		// now run the generates
		totalSteps = 0;
		for (Context context : contextsToRun) {
			totalSteps += context.getGenerationSteps();
		}
		callback.generationStarted(totalSteps);

		for (Context context : contextsToRun) {
			context.generateFiles(callback, generatedJavaFiles,
					generatedXmlFiles, warnings);
		}

	}

	private static void generateServerClass(String targetPackage,String targetProject,List<TableConfiguration> tableConfigurations,Class clazz) {
		freemarker.template.Configuration configuration = new freemarker.template.Configuration();
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateLoader(new ClassTemplateLoader(clazz,"/"));
		//   configuration.setClassForTemplateLoading(StartUp.class, "");//模板文件所在路径
		generateCtrollorlClass(configuration,tableConfigurations,targetPackage,targetProject);//生成controller文件
		generateServiceClass(configuration,tableConfigurations,targetPackage,targetProject);//生成serviceImpl 接口文件
		generateServiceImplClass(configuration,tableConfigurations,targetPackage,targetProject);//生成serviceImpl 接口文件
		generateDaoClass(configuration,tableConfigurations,targetPackage,targetProject);//生成serviceImpl 接口文件

		//generatePageMainJs(configuration,tableConfigurations,targetPackage,targetProject);//生成页面js文件
		//generatePageMain(configuration,tableConfigurations,targetPackage,targetProject);//生成主页面文件
		//generatePageAdd(configuration,tableConfigurations,targetPackage,targetProject);//生成新增页面文件
		//generatePageEdit(configuration,tableConfigurations,targetPackage,targetProject);//生成修改页面文件

	}

	private static void generateDaoClass(freemarker.template.Configuration configuration,List<TableConfiguration> confs, String targetPackage,String targetProject) {
		Template t=null;
		try {
			t = configuration.getTemplate("dao.ftl"); //获取模板文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		String pathname = targetProject+"\\"+targetPackage.replace("entity", "dao").replace(".","//");
		File destDir = new File(pathname);
		if(!destDir.exists()){
			destDir.mkdirs();
		}
		for(TableConfiguration conf:confs){
			File outFile = new File(pathname+"/"+conf.getDomainObjectName()+"Dao"+".java"); //导出文件
			if(!outFile.exists()){
				try {
					outFile.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Writer out = null;
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("interfacetargetPackage", targetPackage.replace("entity", "service.interfaces"));
			dataMap.put("targetPackage", targetPackage);
			dataMap.put("domainObjectName", conf.getDomainObjectName());
			dataMap.put("classDesc",targetPackage+"."+conf.getDomainObjectName()+"Server");
			dataMap.put("author", author);
			dataMap.put("curDate", DateTimeUtil.getTime(new Date(), "yyyy-MM-dd hh:mm:ss"));
			dataMap.put("samllDomainObjectName", toLowerCaseFirstOne(conf.getDomainObjectName()));
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void generateCtrollorlClass(freemarker.template.Configuration configuration,List<TableConfiguration> confs, String targetPackage,String targetProject) {
		Template t=null;
		try {
			t = configuration.getTemplate("controller.ftl"); //获取模板文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		String pathname = targetProject+"\\"+targetPackage.replace("entity", "controller").replace(".","//");
		File destDir = new File(pathname);
		if(!destDir.exists()){
			destDir.mkdirs();
		}
		for(TableConfiguration conf:confs){
			File outFile = new File(pathname+"/"+conf.getDomainObjectName()+"Controller"+".java"); //导出文件
			if(!outFile.exists()){
				try {
					outFile.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Writer out = null;
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("mapperTargetPackage", targetPackage);
			dataMap.put("impltargetPackage", targetPackage.replace("entity.common", "controller"));
			dataMap.put("targetPackage", targetPackage);
			dataMap.put("targetPackageService", targetPackage.replace("entity.common", "service"));
			dataMap.put("domainObjectName", conf.getDomainObjectName());
			dataMap.put("classDesc",conf.getDomainObjectName()+"Controller");
			dataMap.put("author", author);
			dataMap.put("curDate", DateTimeUtil.getTime(new Date(), "yyyy-MM-dd hh:mm:ss"));
			dataMap.put("samllDomainObjectName", toLowerCaseFirstOne(conf.getDomainObjectName()));
			dataMap.put("interfacetargetPackage", targetPackage.replace("entity", "interfaces"));
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void generateServiceImplClass(freemarker.template.Configuration configuration,List<TableConfiguration> confs, String targetPackage,String targetProject) {
		Template t=null;
		try {
			t = configuration.getTemplate("serviceImpl.ftl"); //获取模板文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		String pathname = targetProject+"\\"+targetPackage.replace("entity", "service.impl").replace(".","//");
		File destDir = new File(pathname);
		if(!destDir.exists()){
			destDir.mkdirs();
		}
		for(TableConfiguration conf:confs){
			File outFile = new File(pathname+"/"+conf.getDomainObjectName()+"ServiceImpl"+".java"); //导出文件
			if(!outFile.exists()){
				try {
					outFile.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Writer out = null;
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("mapperTargetPackage", targetPackage);
			dataMap.put("impltargetPackage", targetPackage.replace("entity", "service.impl"));
			dataMap.put("targetPackage", targetPackage);
			dataMap.put("targetPackageService", targetPackage.replace("entity", "service.interfaces"));
			dataMap.put("domainObjectName", conf.getDomainObjectName());
			dataMap.put("classDesc",targetPackage+"."+conf.getDomainObjectName()+"ServerImpl");
			dataMap.put("author", author);
			dataMap.put("curDate", DateTimeUtil.getTime(new Date(), "yyyy-MM-dd hh:mm:ss"));
			dataMap.put("samllDomainObjectName", toLowerCaseFirstOne(conf.getDomainObjectName()));
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void generateServiceClass(freemarker.template.Configuration configuration,List<TableConfiguration> confs, String targetPackage,String targetProject) {
		Template t=null;
		try {
			t = configuration.getTemplate("service.ftl"); //获取模板文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		String pathname = targetProject+"\\"+targetPackage.replace("entity", "service").replace(".","//");
		File destDir = new File(pathname);
		if(!destDir.exists()){
			destDir.mkdirs();
		}
		for(TableConfiguration conf:confs){
			File outFile = new File(pathname+"/"+conf.getDomainObjectName()+"Service"+".java"); //导出文件
			if(!outFile.exists()){
				try {
					outFile.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Writer out = null;
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("interfacetargetPackage", targetPackage.replace("entity", "service.interfaces"));
			dataMap.put("targetPackage", targetPackage);
			dataMap.put("domainObjectName", conf.getDomainObjectName());
			dataMap.put("classDesc",targetPackage+"."+conf.getDomainObjectName()+"Server");
			dataMap.put("author", author);
			dataMap.put("curDate", DateTimeUtil.getTime(new Date(), "yyyy-MM-dd hh:mm:ss"));
			dataMap.put("samllDomainObjectName", toLowerCaseFirstOne(conf.getDomainObjectName()));
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void generateServerImplClass(freemarker.template.Configuration configuration,List<TableConfiguration> confs, String targetPackage,String targetProject) {
		Template t=null;
		try {
			t = configuration.getTemplate("serverImpl.ftl"); //获取模板文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		String pathname = targetProject+"\\"+targetPackage.replace("entity", "server.impl").replace(".","//");
		File destDir = new File(pathname);
		if(!destDir.exists()){
			destDir.mkdirs();
		}
		for(TableConfiguration conf:confs){
			File outFile = new File(pathname+"/"+conf.getDomainObjectName()+"ServerImpl"+".java"); //导出文件
			if(!outFile.exists()){
				try {
					outFile.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Writer out = null;
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("intefacetargetPackage", targetPackage.replace("entity", "interfaces"));
			dataMap.put("impltargetPackage", targetPackage.replace("entity", "server"));
			dataMap.put("targetPackage", targetPackage);
			dataMap.put("targetPackageService", targetPackage.replace("entity", "service.interfaces"));
			dataMap.put("domainObjectName", conf.getDomainObjectName());
			dataMap.put("classDesc",targetPackage+"."+conf.getDomainObjectName()+"ServerImpl");
			dataMap.put("author", author);
			dataMap.put("curDate", DateTimeUtil.getTime(new Date(), "yyyy-MM-dd hh:mm:ss"));
			dataMap.put("samllDomainObjectName", toLowerCaseFirstOne(conf.getDomainObjectName()));
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void generateServerInterfaceClass(freemarker.template.Configuration configuration,List<TableConfiguration> confs, String targetPackage,String targetProject) {
		Template t=null;
		try {
			t = configuration.getTemplate("server.ftl"); //获取模板文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		String pathname = targetProject+"\\"+targetPackage.replace("entity", "interfaces").replace(".","//");
		File destDir = new File(pathname);
		if(!destDir.exists()){
			destDir.mkdirs();
		}
		for(TableConfiguration conf:confs){
			File outFile = new File(pathname+"/"+conf.getDomainObjectName()+"Server"+".java"); //导出文件
			if(!outFile.exists()){
				try {
					outFile.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Writer out = null;
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("interfacetargetPackage", targetPackage.replace("entity", "interfaces"));
			dataMap.put("targetPackage", targetPackage);
			dataMap.put("domainObjectName", conf.getDomainObjectName());
			dataMap.put("classDesc",targetPackage+"."+conf.getDomainObjectName()+"Server");
			dataMap.put("author", author);
			dataMap.put("curDate", DateTimeUtil.getTime(new Date(), "yyyy-MM-dd hh:mm:ss"));
			dataMap.put("samllDomainObjectName", toLowerCaseFirstOne(conf.getDomainObjectName()));
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String toLowerCaseFirstOne(String s){
		if(Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}
}