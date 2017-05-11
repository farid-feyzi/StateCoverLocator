/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst.visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import locator.common.config.Constant;
import locator.common.config.Identifier;
import locator.common.java.JavaFile;
import locator.common.java.Method;

/**
 * @author Jiajun
 * @date May 10, 2017
 */
public class StatementInstrumentVisitorTest {
	
	@Test
	public void test_InstrumentForTestClass(){
		String filePath = "res/junitRes/IterativeLegendreGaussIntegratorTest.java";
		CompilationUnit compilationUnit = JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		StatementInstrumentVisitor statementInstrumentVisitor = new StatementInstrumentVisitor();
		statementInstrumentVisitor.setFlag(Constant.INSTRUMENT_K_TEST);
		compilationUnit.accept(statementInstrumentVisitor);
		System.out.println(compilationUnit.toString());
	}
	
	@Test
	public void test_InstrumentForSourceClass(){
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		compilationUnit.accept(new StatementInstrumentVisitor());
		System.out.println(compilationUnit.toString());
	}
	
	@Test
	public void test_InstrumentForSingleMethod(){
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		String methodString = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		Set<Method> methods = new HashSet<>();
		methods.add(new Method(Identifier.getIdentifier(methodString)));
		compilationUnit.accept(new StatementInstrumentVisitor(methods));
		System.out.println(compilationUnit.toString());
	}
	
	@Test
	public void test_InstrumentForMultiMethod(){
		String filePath = "res/junitRes/BigFraction.java";
		CompilationUnit compilationUnit = JavaFile.genASTFromSource(JavaFile.readFileToString(filePath),
				ASTParser.K_COMPILATION_UNIT);
		Set<Method> methods = new HashSet<>();
		String methodString1 = "org.apache.commons.math3.fraction.BigFraction#?#BigFraction#?,BigInteger,BigInteger";
		methods.add(new Method(Identifier.getIdentifier(methodString1)));
		String methodString2 = "org.apache.commons.math3.fraction.BigFraction#BigFraction#divide#?,BigInteger";
		methods.add(new Method(Identifier.getIdentifier(methodString2)));
		String methodString3 = "org.apache.commons.math3.fraction.BigFraction$TroubleClazz#boolean#method2#?";
		methods.add(new Method(Identifier.getIdentifier(methodString3)));
		compilationUnit.accept(new StatementInstrumentVisitor(methods));
		System.out.println(compilationUnit.toString());
	}
	
}
