package org.polytech.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseCommands() {
        Parser parser = new Parser();
        List<Lexer.Token> tokens = Arrays.asList(
                new Lexer.Token("snake", "right"),
                new Lexer.Token("snake", "left")
        );
        Parser.Program program = parser.parse(tokens);

        assertEquals(2, program.commands.size());
        assertEquals("right", program.commands.get(0).action);
        assertEquals("left", program.commands.get(1).action);
    }
}
