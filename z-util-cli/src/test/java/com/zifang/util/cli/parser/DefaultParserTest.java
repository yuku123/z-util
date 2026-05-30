package com.zifang.util.cli.parser;

import com.zifang.util.cli.exception.AlreadySelectedException;
import com.zifang.util.cli.exception.AmbiguousOptionException;
import com.zifang.util.cli.exception.MissingArgumentException;
import com.zifang.util.cli.exception.MissingOptionException;
import com.zifang.util.cli.exception.ParseException;
import com.zifang.util.cli.exception.UnrecognizedOptionException;
import com.zifang.util.cli.model.CommandLine;
import com.zifang.util.cli.model.Option;
import com.zifang.util.cli.model.OptionGroup;
import com.zifang.util.cli.model.Options;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Unit tests for DefaultParser.
 * Tests parsing of short options, long options, option groups, required options, and argument values.
 */
public class DefaultParserTest {

    private DefaultParser parser = new DefaultParser();

    // ==================== parse(Options, String[]) tests ====================

    @Test
    public void testParseEmptyArgs() throws ParseException {
        Options options = new Options();
        CommandLine cmd = parser.parse(options, new String[]{});
        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
    }

    @Test
    public void testParseNullArgs() throws ParseException {
        Options options = new Options();
        CommandLine cmd = parser.parse(options, null);
        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
    }

    @Test
    public void testParseShortOptionNoArg() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Toggle A");
        options.addOption("b", false, "Toggle B");

        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b"});
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
    }

    @Test
    public void testParseShortOptionWithValue() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLine cmd = parser.parse(options, new String[]{"-f", "input.txt"});
        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParseShortOptionWithAttachedValue() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLine cmd = parser.parse(options, new String[]{"-finput.txt"});
        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParseLongOptionNoArg() throws ParseException {
        Options options = new Options();
        options.addOption(null, "verbose", false, "Verbose mode");

        CommandLine cmd = parser.parse(options, new String[]{"--verbose"});
        assertTrue(cmd.hasOption("verbose"));
    }

    @Test
    public void testParseLongOptionWithValue() throws ParseException {
        Options options = new Options();
        options.addOption(null, "file", true, "Input file");

        CommandLine cmd = parser.parse(options, new String[]{"--file", "input.txt"});
        assertTrue(cmd.hasOption("file"));
        assertEquals("input.txt", cmd.getOptionValue("file"));
    }

    @Test
    public void testParseLongOptionWithEquals() throws ParseException {
        Options options = new Options();
        options.addOption(null, "file", true, "Input file");

        CommandLine cmd = parser.parse(options, new String[]{"--file=input.txt"});
        assertTrue(cmd.hasOption("file"));
        assertEquals("input.txt", cmd.getOptionValue("file"));
    }

    @Test
    public void testParseCombinedShortOptions() throws ParseException {
        Options options = new Options();
        options.addOption("v", false, "Verbose");
        options.addOption("x", false, "X flag");
        options.addOption("c", false, "C flag");

        CommandLine cmd = parser.parse(options, new String[]{"-vxc"});
        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("x"));
        assertTrue(cmd.hasOption("c"));
    }

    @Test
    public void testParseShortOptionClusterWithArg() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLine cmd = parser.parse(options, new String[]{"-fvalue"});
        assertTrue(cmd.hasOption("f"));
        assertEquals("value", cmd.getOptionValue("f"));
    }

    @Test
    public void testParsePositionalArgs() throws ParseException {
        Options options = new Options();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2", "arg3"});

        assertEquals(3, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
        assertEquals("arg3", cmd.getArgList().get(2));
    }

    @Test
    public void testParseOptionWithPositionalArgs() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLine cmd = parser.parse(options, new String[]{"-f", "input.txt", "arg1"});
        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
        assertEquals(1, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
    }

    // ==================== parse(Options, String[], boolean) tests ====================

    @Test
    public void testParseStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Toggle A");

        CommandLine cmd = parser.parse(options, new String[]{"-a", "non-option", "-b"}, true);
        assertTrue(cmd.hasOption("a"));
        assertEquals(2, cmd.getArgList().size());
        assertEquals("non-option", cmd.getArgList().get(0));
        assertEquals("-b", cmd.getArgList().get(1));
    }

    // ==================== parse(Options, String[], Properties) tests ====================

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");
        options.addOption("v", false, "Verbose");

        Properties props = new Properties();
        props.setProperty("f", "default.txt");
        props.setProperty("v", "true");

        CommandLine cmd = parser.parse(options, new String[]{}, props);
        assertTrue(cmd.hasOption("f"));
        assertEquals("default.txt", cmd.getOptionValue("f"));
        assertTrue(cmd.hasOption("v"));
    }

    @Test
    public void testParseWithPropertiesEmptyArgs() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        Properties props = new Properties();
        props.setProperty("f", "from.properties");

        CommandLine cmd = parser.parse(options, null, props);
        assertTrue(cmd.hasOption("f"));
        assertEquals("from.properties", cmd.getOptionValue("f"));
    }

    @Test
    public void testParseWithPropertiesDoesNotOverride() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        Properties props = new Properties();
        props.setProperty("f", "default.txt");

        CommandLine cmd = parser.parse(options, new String[]{"-f", "cmdline.txt"}, props);
        assertEquals("cmdline.txt", cmd.getOptionValue("f"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnknownPropertyOption() throws ParseException {
        Options options = new Options();
        Properties props = new Properties();
        props.setProperty("unknown", "value");

        parser.parse(options, new String[]{}, props);
    }

    @Test
    public void testParseWithPropertiesBooleanTrue() throws ParseException {
        Options options = new Options();
        options.addOption("v", false, "Verbose");

        Properties props = new Properties();
        props.setProperty("v", "yes");

        CommandLine cmd = parser.parse(options, new String[]{}, props);
        assertTrue(cmd.hasOption("v"));
    }

    @Test
    public void testParseWithPropertiesBooleanFalse() throws ParseException {
        Options options = new Options();
        options.addOption("v", false, "Verbose");

        Properties props = new Properties();
        props.setProperty("v", "no");

        CommandLine cmd = parser.parse(options, new String[]{}, props);
        assertFalse(cmd.hasOption("v"));
    }

    // ==================== parse(Options, String[], Properties, boolean) tests ====================

    @Test
    public void testParseFullSignature() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        Properties props = new Properties();
        props.setProperty("f", "default.txt");

        CommandLine cmd = parser.parse(options, new String[]{"-f", "cmd.txt"}, props, false);
        assertEquals("cmd.txt", cmd.getOptionValue("f"));
    }

    // ==================== Required option tests ====================

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder().opt("r").description("Required").required(true).build());

        parser.parse(options, new String[]{});
    }

    @Test
    public void testParseRequiredOptionProvided() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder().opt("r").description("Required").required(true).build());

        CommandLine cmd = parser.parse(options, new String[]{"-r"});
        assertTrue(cmd.hasOption("r"));
    }

    @Test
    public void testParseMultipleRequiredOptionsAllProvided() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder().opt("r").description("Required").required(true).build());
        options.addOption(Option.builder().opt("s").description("Also required").required(true).build());

        CommandLine cmd = parser.parse(options, new String[]{"-r", "-s"});
        assertTrue(cmd.hasOption("r"));
        assertTrue(cmd.hasOption("s"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMultipleRequiredOptionsOneMissing() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder().opt("r").description("Required").required(true).build());
        options.addOption(Option.builder().opt("s").description("Also required").required(true).build());

        parser.parse(options, new String[]{"-r"});
    }

    // ==================== Option group tests ====================

    @Test
    public void testParseOptionGroupOneSelected() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(Option.builder().opt("a").description("Format A").build());
        group.addOption(Option.builder().opt("b").description("Format B").build());
        group.setRequired(true);
        options.addOptionGroup(group);

        CommandLine cmd = parser.parse(options, new String[]{"-a"});
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test(expected = AlreadySelectedException.class)
    public void testParseOptionGroupSelectBoth() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(Option.builder().opt("a").description("Format A").build());
        group.addOption(Option.builder().opt("b").description("Format B").build());
        options.addOptionGroup(group);

        parser.parse(options, new String[]{"-a", "-b"});
    }

    @Test
    public void testParseOptionGroupRequiredAllowsEither() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(Option.builder().opt("a").description("Format A").build());
        group.addOption(Option.builder().opt("b").description("Format B").build());
        group.setRequired(true);
        options.addOptionGroup(group);

        CommandLine cmd = parser.parse(options, new String[]{"-b"});
        assertFalse(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    // ==================== Exception tests ====================

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedShortOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Toggle A");

        parser.parse(options, new String[]{"-z"});
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Toggle A");

        parser.parse(options, new String[]{"--unknown"});
    }

    @Test
    public void testParseUnrecognizedOptionExceptionMessage() {
        Options options = new Options();
        options.addOption("a", false, "Toggle A");

        try {
            parser.parse(options, new String[]{"-z"});
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertTrue(e.getMessage().contains("-z"));
        } catch (ParseException e) {
            fail("Wrong exception type: " + e);
        }
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgumentForOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        parser.parse(options, new String[]{"-f"});
    }

    @Test
    public void testParseMissingArgumentExceptionMessage() {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        try {
            parser.parse(options, new String[]{"-f"});
            fail("Expected MissingArgumentException");
        } catch (MissingArgumentException e) {
            assertNotNull(e.getMessage());
            assertNotNull(e.getOption());
        } catch (ParseException e) {
            fail("Wrong exception type: " + e);
        }
    }

    @Test
    public void testParseAmbiguousLongOption() {
        Options options = new Options();
        options.addOption(null, "foo", false, "Foo");
        options.addOption(null, "foobar", false, "Foobar");

        try {
            parser.parse(options, new String[]{"--foo"});
            fail("Expected AmbiguousOptionException");
        } catch (AmbiguousOptionException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().contains("Ambiguous"));
        } catch (ParseException e) {
            fail("Wrong exception type: " + e);
        }
    }

    // ==================== Double dash tests ====================

    @Test
    public void testParseDoubleDashEndOfOptions() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLine cmd = parser.parse(options, new String[]{"--", "-f"});
        assertFalse(cmd.hasOption("f"));
        assertEquals(1, cmd.getArgList().size());
        assertEquals("-f", cmd.getArgList().get(0));
    }

    // ==================== Multiple values tests ====================

    @Test
    public void testParseOptionMultipleValues() throws ParseException {
        Options options = new Options();
        Option option = Option.builder()
                .opt("D")
                .hasArg(true)
                .argName("value")
                .maxArgs(3)
                .build();
        options.addOption(option);

        CommandLine cmd = parser.parse(options, new String[]{"-D", "val1", "-D", "val2"});

        String[] values = cmd.getOptionValues("D");
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("val1", values[0]);
        assertEquals("val2", values[1]);
    }

    // ==================== Flatten method tests ====================

    @Test
    public void testFlattenNullArguments() throws ParseException {
        Options options = new Options();
        String[] flatArgs = parser.flatten(new String[]{null}, false);
        assertEquals(0, flatArgs.length);
    }

    @Test
    public void testFlattenOnlyDoubleDash() throws ParseException {
        Options options = new Options();
        String[] flatArgs = parser.flatten(new String[]{"--"}, false);
        assertEquals(1, flatArgs.length);
        assertEquals("--", flatArgs[0]);
    }

    // ==================== indexOfEqual tests ====================

    @Test
    public void testIndexOfEqualWithEqual() {
        int index = DefaultParser.indexOfEqual("foo=bar");
        assertEquals(3, index);
    }

    @Test
    public void testIndexOfEqualNoEqual() {
        int index = DefaultParser.indexOfEqual("foobar");
        assertEquals(-1, index);
    }

    @Test
    public void testIndexOfEqualEmptyString() {
        int index = DefaultParser.indexOfEqual("");
        assertEquals(-1, index);
    }

    @Test
    public void testIndexOfEqualMultipleEquals() {
        int index = DefaultParser.indexOfEqual("a=b=c");
        assertEquals(1, index);
    }

    @Test
    public void testIndexOfEqualAtStart() {
        int index = DefaultParser.indexOfEqual("=value");
        assertEquals(0, index);
    }

    @Test
    public void testIndexOfEqualAtEnd() {
        int index = DefaultParser.indexOfEqual("value=");
        assertEquals(5, index);
    }

    // ==================== Option value retrieval tests ====================

    @Test
    public void testGetOptionValueWithDefault() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLine cmd = parser.parse(options, new String[]{"-f", "actual.txt"});
        assertEquals("actual.txt", cmd.getOptionValue("f", "default.txt"));
    }

    @Test
    public void testGetOptionValueWhenNotSet() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLine cmd = parser.parse(options, new String[]{});
        assertNull(cmd.getOptionValue("f"));
        assertEquals("default.txt", cmd.getOptionValue("f", "default.txt"));
    }

    @Test
    public void testHasOptionByString() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLine cmd = parser.parse(options, new String[]{"-f", "file.txt"});
        assertTrue(cmd.hasOption("f"));
        assertFalse(cmd.hasOption("other"));
    }

    @Test
    public void testGetArgs() throws ParseException {
        Options options = new Options();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});

        String[] args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testGetOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "A");
        options.addOption("b", false, "B");

        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b"});
        assertTrue(cmd.hasOptions());
        assertEquals(2, cmd.getOptions().size());
    }
}