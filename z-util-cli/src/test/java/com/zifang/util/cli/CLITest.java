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
    public void testShortOptionWithAttachedValue() throws Exception {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-finput.txt"});

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testLongOptionWithSpace() throws Exception {
        Options options = new Options();
        options.addOption(null, "file", true, "Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--file", "input.txt"});

        assertTrue(cmd.hasOption("file"));
        assertEquals("input.txt", cmd.getOptionValue("file"));
    }

    @Test
    public void testLongOptionWithEquals() throws Exception {
        Options options = new Options();
        options.addOption(null, "file", true, "Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--file=input.txt"});

        assertTrue(cmd.hasOption("file"));
        assertEquals("input.txt", cmd.getOptionValue("file"));
    }

    @Test
    public void testPositionalArgs() throws Exception {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});

        assertFalse(cmd.hasOption("f"));
        assertEquals(2, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }

    @Test
    public void testOptionWithPositionalArgs() throws Exception {
        Options options = new Options();
        options.addOption("f", true, "Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-f", "input.txt", "arg1"});

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
        assertEquals(1, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
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
    public void testOptionGroupRejectsBoth() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(Option.builder().opt("a").description("Format A").build());
        group.addOption(Option.builder().opt("b").description("Format B").build());
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{"-a", "-b"});
            fail("Should throw AlreadySelectedException");
        } catch (com.zifang.util.cli.exception.AlreadySelectedException e) {
            assertNotNull(e.getMessage());
        } catch (ParseException e) {
            fail("Wrong exception type: " + e);
        }
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
    public void testUnrecognizedShortOption() {
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
    public void testUnrecognizedLongOption() {
        Options options = new Options();
        options.addOption("a", false, "Toggle A");

        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{"--xyz"});
            fail("Should throw UnrecognizedOptionException");
        } catch (com.zifang.util.cli.exception.UnrecognizedOptionException e) {
            assertTrue(e.getMessage().contains("--xyz"));
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
        assertEquals(2, cmd.getArgList().size());
        assertEquals("non-option", cmd.getArgList().get(0));
        assertEquals("-b", cmd.getArgList().get(1));
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
    public void testNegatedOption() throws Exception {
        Options options = new Options();
        options.addOption("a", false, "Toggle A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{});

        assertFalse(cmd.hasOption("a"));
    }

    @Test
    public void testCombinedShortOptions() throws Exception {
        Options options = new Options();
        options.addOption("v", false, "Verbose");
        options.addOption("x", false, "X flag");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-vx"});

        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("x"));
    }
}
