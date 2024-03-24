package org.polytech.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LexerTest {

    @Test
    public void testTokenizeSimpleCommands() throws Exception {
        Path tempFile = Files.createTempFile(null, ".txt");
        Files.write(tempFile, List.of("snake right", "snake left"));

        Lexer lexer = new Lexer();
        List<Lexer.Token> tokens = lexer.tokenize(tempFile.toString());

        assertEquals(2, tokens.size());
        assertEquals("right", tokens.get(0).direction);
        assertEquals("left", tokens.get(1).direction);

        Files.deleteIfExists(tempFile); // Nettoyer le fichier temporaire
    }
}
