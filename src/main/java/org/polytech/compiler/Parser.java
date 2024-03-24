package org.polytech.compiler;

import java.util.List;

public class Parser {

    public static class Command {
        public final String action;

        public Command(String action) {
            this.action = action;
        }
    }

    public static class Program {
        public final List<Command> commands;

        public Program(List<Command> commands) {
            this.commands = commands;
        }
    }

    public Program parse(List<Lexer.Token> tokens) {
        List<Command> commands = tokens.stream()
                .map(token -> new Command(token.direction))
                .toList();
        return new Program(commands);
    }
}
