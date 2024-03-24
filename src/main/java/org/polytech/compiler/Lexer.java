package org.polytech.compiler;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lexer {
    public static class Token {
        public final String command;
        public final String direction;

        public Token(String command, String direction) {
            this.command = command;
            this.direction = direction;
        }

        @Override
        public String toString() {
            return String.format("Token[command=%s, direction=%s, steps=%d]", command, direction);
        }
    }

    public List<Token> tokenize(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        List<Token> tokens = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(" ");
            if (parts.length == 2 && parts[0].equalsIgnoreCase("snake")) {
                String direction = parts[1];
                tokens.add(new Token(parts[0], direction));
            } else {
                throw new IllegalArgumentException("Invalid command format: " + line);
            }
        }
        return tokens;
    }
}
