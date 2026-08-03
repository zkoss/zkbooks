/* MoldFileFormPropertyTest.java

	The SHAPE of a mold file, asserted at source level.

	Written from the book page zk_component_dev_essentials/the_mold_file_form.md, which
	states three rules about the file itself:

	  1. "ZK inlines the mold file as the right-hand side of an assignment, so the file
	      must be a single expression: no trailing semicolon and no statements."
	  2. "Use the named function name$mold$(out) form."
	  3. (the runtime consequence of 1) - covered in the browser by
	     MoldFormIT / DomAttrsIT#theMoldFileMustBeASingleExpressionOnTheRightHandSide,
	     because only a JS engine can say what parses.

	Layer: plain JUnit (surefire). This layer can only see the FILES, which is exactly
	what rules 1 and 2 are about - they are properties of the text a reader types, and
	nothing at runtime reports them (a mold whose file ends in ';' still parses; see the
	browser test named above). That asymmetry is why this file exists: the rules are
	conventions the lab's own molds must be able to demonstrate.

	How each test can fail: every predicate is first run against synthetic input that
	violates it, so a broken scanner is caught before the real files are scanned. Adding
	a ';' to the end of any mold file, or renaming its function away from the
	<name>$mold$ form, turns the corresponding test red.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoldFileFormPropertyTest {

	/** Anything ZK would refuse to accept on the right-hand side of an assignment. */
	private static final Pattern STATEMENT_START = Pattern
			.compile("^\\s*(var|let|const|import|export|module\\.exports|class)\\b");

	/** The form the book tells the reader to copy: function <something>$mold$(out). */
	private static final Pattern NAMED_MOLD_FORM = Pattern
			.compile("^function\\s+[A-Za-z_$][A-Za-z0-9_$]*\\$mold\\$\\s*\\(\\s*out\\s*\\)\\s*\\{");

	@Test
	@DisplayName("every mold file is a single expression: no trailing semicolon and no top-level statement")
	void everyMoldFileIsASingleExpression() throws IOException {
		// Self-check first: the code-extractor and both predicates must be able to see
		// the defects they exist to catch, otherwise the loop below is decoration.
		assertEquals("function a$mold$(out) {\n}", code("/* header */\nfunction a$mold$(out) {\n\t// note\n}"),
				"the extractor does not strip the comments a real mold file carries");
		assertTrue(endsWithSemicolon(code("function a$mold$(out) {\n};")),
				"the trailing-semicolon predicate cannot see a trailing semicolon");
		assertFalse(endsWithSemicolon(code("function a$mold$(out) {\n}")),
				"the trailing-semicolon predicate reports one where there is none");
		assertTrue(STATEMENT_START.matcher(code("var mold = function (out) {\n}")).find(),
				"the statement predicate cannot see a top-level var");
		assertFalse(STATEMENT_START.matcher(code("function a$mold$(out) {\n}")).find(),
				"the statement predicate rejects a plain function expression");

		List<Path> molds = moldFiles();
		assertFalse(molds.isEmpty(),
				"found no mold files under src/main/resources/web/js/**/mold/ - the scan path is wrong");

		for (Path mold : molds) {
			String code = code(read(mold));
			assertFalse(code.isEmpty(), mold + " has no code outside its comments");
			assertFalse(endsWithSemicolon(code),
					mold + " ends with ';'. ZK appends the semicolon that terminates the assignment,"
							+ " so the file itself must not.");
			assertFalse(STATEMENT_START.matcher(code).find(),
					mold + " opens with a statement. A mold file is inlined on the right-hand side of"
							+ " zk._m['<mold-name>'] = ..., where only an expression is legal.");
		}
	}

	@Test
	@DisplayName("every mold file uses the named function <something>$mold$(out) form the book tells the reader to copy")
	void everyMoldFileUsesTheNamedFunctionForm() throws IOException {
		// Self-check: the pattern must reject the anonymous form (which still WORKS - see
		// MoldFormIT#theAnonymousMoldFormRendersToo - but is not the form the book shows).
		assertTrue(NAMED_MOLD_FORM.matcher("function simpleLabel$mold$(out) {").find(),
				"the pattern rejects the very form the book prints");
		assertFalse(NAMED_MOLD_FORM.matcher("function (out) {").find(),
				"the pattern accepts the anonymous form, so it proves nothing");
		assertFalse(NAMED_MOLD_FORM.matcher("function simpleLabel(out) {").find(),
				"the pattern accepts a name without the $mold$ marker");

		for (Path mold : moldFiles()) {
			String code = code(read(mold));
			assertTrue(NAMED_MOLD_FORM.matcher(code).find(),
					mold + " is not the named 'function <name>$mold$(out)' expression the book documents,"
							+ " it starts: " + code.substring(0, Math.min(60, code.length())));
		}
	}

	// ------------------------------------------------------------------------ helpers

	private static List<Path> moldFiles() throws IOException {
		Path root = Paths.get("src/main/resources/web/js");
		if (!Files.isDirectory(root))
			return Collections.emptyList();
		try (Stream<Path> walk = Files.walk(root)) {
			return walk.filter(Files::isRegularFile)
					.filter(p -> p.toString().replace('\\', '/').contains("/mold/"))
					.filter(p -> p.toString().endsWith(".js"))
					.sorted()
					.collect(Collectors.toList());
		}
	}

	private static String read(Path p) throws IOException {
		return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
	}

	/**
	 * The mold file's CODE: block comments removed, whole-line {@code //} comments
	 * removed, blank lines dropped, trailing whitespace trimmed.
	 *
	 * <p>Deliberately conservative - it never touches a {@code //} that follows code on
	 * the same line, so it cannot corrupt a string literal. That is enough for the two
	 * questions asked of it (what does the code start with, what does it end with).
	 */
	private static String code(String source) {
		String noBlocks = source.replaceAll("(?s)/\\*.*?\\*/", "");
		return Stream.of(noBlocks.split("\n"))
				.map(line -> line.replaceAll("\\s+$", ""))
				.filter(line -> !line.trim().isEmpty())
				.filter(line -> !line.trim().startsWith("//"))
				.collect(Collectors.joining("\n"));
	}

	private static boolean endsWithSemicolon(String code) {
		return code.endsWith(";");
	}
}
