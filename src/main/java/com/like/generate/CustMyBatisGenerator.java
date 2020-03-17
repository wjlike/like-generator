package com.like.generate;

import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.exception.InvalidConfigurationException;

public class CustMyBatisGenerator extends MyBatisGenerator {
	
	private  List<GeneratedJavaFile> generatedJavaFiles;

	public CustMyBatisGenerator(Configuration configuration,
			ShellCallback shellCallback, List<String> warnings)
			throws InvalidConfigurationException {
		super(configuration, shellCallback, warnings);
	}

	public List<GeneratedJavaFile> getGeneratedJavaFiles() {
		return generatedJavaFiles;
	}

	public void setGeneratedJavaFiles(List<GeneratedJavaFile> generatedJavaFiles) {
		this.generatedJavaFiles = generatedJavaFiles;
	}
}
