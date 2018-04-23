/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.inst;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import locator.common.java.JavaFile;
import locator.common.util.LevelLogger;
import locator.inst.visitor.TraversalVisitor;

/**
 * 
 * @author Jiajun
 * @date May 9, 2017
 */
public class Instrument {

	private final static String __name__ = "@Instrument ";

	public static boolean execute(String path, TraversalVisitor traversalVisitor) {
		if (path == null || path.length() <= 1) {
			LevelLogger.error(__name__ + "#execute illegal input file : " + path);
			return false;
		}
		File file = new File(path);
		if (!file.exists()) {
			LevelLogger.error(__name__ + "#execute input file not exist : " + path);
			return false;
		}
		List<File> fileList = new ArrayList<>();
		if (file.isDirectory()) {
			fileList = JavaFile.ergodic(file, fileList);
		} else if (file.isFile()) {
			fileList.add(file);
		} else {
			LevelLogger.error(
					__name__ + "#execute input file is neither a file nor directory : " + file.getAbsolutePath());
			return false;
		}

		for (File f : fileList) {
			String source = JavaFile.readFileToString(f);
			CompilationUnit unit = (CompilationUnit) JavaFile.genASTFromSource(source, ASTParser.K_COMPILATION_UNIT);
			if (unit == null || unit.toString().trim().length() < 1) {
				continue;
			}
			traversalVisitor.setFileName(f.getAbsolutePath());
			traversalVisitor.traverse(unit);
			String formatSource = null;
			// Formatter formatter = new Formatter();
			// try {
			// formatSource = formatter.formatSource(unit.toString());
			// } catch (FormatterException e) {
			// LevelLogger.error(__name__ + "#execute Format Code Error for : "
			// + f.getAbsolutePath());
			formatSource = unit.toString();
			// }
			JavaFile.writeStringToFile(f, formatSource);
		}
		return true;
	}
}
