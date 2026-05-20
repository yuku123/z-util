package com.zifang.util.cli;

import com.zifang.util.cli.model.CommandLine;
import com.zifang.util.cli.model.Option;
import com.zifang.util.cli.model.OptionGroup;
import com.zifang.util.cli.model.Options;
import com.zifang.util.cli.parser.CommandLineParser;
import com.zifang.util.cli.parser.DefaultParser;
import com.zifang.util.cli.exception.ParseException;
import com.zifang.util.cli.help.HelpFormatter;

import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CLITest {

    @Test
    public void testBasicShortOption() throws Exception {
        Options options = new Options();
        options.addOption("a", false, "Toggle A");
        options.addOption("b", false, "Toggle B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b"});

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
    }

    @Test
    public void testShortOptionWithValue() throws Exception {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-f", "input.txt"});

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testLongOptionWithEquals() throws Exception {
        Options options = new Options();
        options.addOption("file", true, "Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--file=input.txt"});

        assertTrue(cmd.hasOption("file"));
        assertEquals("input.txt", cmd.getOptionValue("file"));
    }

    @Test
    public void testMultipleArgs() throws Exception {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-f", "a.txt", "b.txt"});

        assertTrue(cmd.hasOption("f"));
        assertEquals("a.txt", cmd.getOptionValue("f"));
        assertEquals(Arrays.asList("a.txt", "b.txt"), cmd.getArgList());
    }

    @Test
    public void testOptionGroup() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(Option.builder().opt("a").description("Format A").build());
        group.addOption(Option.builder().opt("b").description("Format B").build());
        group.setRequired(true);
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a"});

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testRequiredOptionMissing() {
        Options options = new Options();
        options.addOption(Option.builder().opt("r").description("Required").required(true).build());

        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{});
            fail("Should throw MissingOptionException");
        } catch (com.zifang.util.cli.exception.MissingOptionException e) {
            assertTrue(e.getMessage().contains("Missing required"));
        } catch (ParseException e) {
            fail("Wrong exception type: " + e);
        }
    }

    @Test
    public void testUnrecognizedOption() {
        Options options = new Options();
        options.addOption("a", false, "Toggle A");

        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{"-z"});
            fail("Should throw UnrecognizedOptionException");
        } catch (com.zifang.util.cli.exception.UnrecognizedOptionException e) {
            assertTrue(e.getMessage().contains("-z"));
        } catch (ParseException e) {
            fail("Wrong exception type: " + e);
        }
    }

    @Test
    public void testHelpFormatter() {
        Options options = new Options();
        options.addOption("h", "help", false, "Display this help message");
        options.addOption("f", "file", true, "Input file");
        options.addOption("v", "verbose", false, "Verbose output");

        HelpFormatter formatter = new HelpFormatter();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        formatter.printHelp(pw, 80, "myapp", null, options, null, false);
        pw.flush();
        String help = sw.toString();

        assertTrue(help.contains("usage:"));
        assertTrue(help.contains("-h"));
        assertTrue(help.contains("--help"));
        assertTrue(help.contains("-f"));
        assertTrue(help.contains("--file"));
        assertTrue(help.contains("Display this help message"));
    }

    @Test
    public void testStopAtNonOption() throws Exception {
        Options options = new Options();
        options.addOption("a", false, "Toggle A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "non-option", "-b"}, true);

        assertTrue(cmd.hasOption("a"));
        assertEquals(Arrays.asList("non-option", "-b"), cmd.getArgList());
    }

    @Test
    public void testOptionBuilder() {
        Option opt = Option.builder()
                .opt("f")
                .longOpt("file")
                .hasArg(true)
                .argName("FILE")
                .description("Input file")
                .required(false)
                .build();

        assertEquals("f", opt.getOpt());
        assertEquals("file", opt.getLongOpt());
        assertTrue(opt.hasArg());
        assertEquals("FILE", opt.getArgName());
        assertFalse(opt.isRequired());
    }

    @Test
    public void testLongOptionWithSpace() throws Exception {
        Options options = new Options();
        options.addOption("f", "file", true, "Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--file", "input.txt"});

        assertTrue(cmd.hasOption("file"));
        assertEquals("input.txt", cmd.getOptionValue("file"));
    }

    @Test
    public void testNegatedOption() throws Exception {
        Options options = new Options();
        options.addOption("a", false, "Toggle A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{});

        assertFalse(cmd.hasOption("a"));
    }
}
