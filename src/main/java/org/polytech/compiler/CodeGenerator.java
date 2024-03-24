package org.polytech.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CodeGenerator {

    private final String className = "SnakeController";

    public void generateCode(Parser.Program program, String outputPath) throws IOException {
        StringBuilder classContent = new StringBuilder();

        classContent.append("package org.polytech.compiler;\n\n");
        classContent.append("import org.polytech.snakegame.entities.Snake;\n\n");
        classContent.append("public class ").append(className).append(" {\n\n");

        for (int i = 0; i < program.commands.size(); i++) {
            Parser.Command command = program.commands.get(i);
            classContent.append("    public void moveSnake").append(i).append("(Snake s) {\n");
            switch (command.action) {
                case "right" ->
                        classContent.append("s.setVelX(1);\n");
                case "left" ->
                        classContent.append("s.setVelX(-1);\n");
                case "up" ->
                        classContent.append("s.setVelY(1);\n");
                case "down" ->
                        classContent.append("s.setVelY(-1);\n");
            }
            classContent.append("    }\n\n");
        }
        classContent.append("}\n\n");
        // Voici la partie ajoutée pour écrire le contenu dans un fichier
        Files.writeString(Paths.get(outputPath), classContent.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}