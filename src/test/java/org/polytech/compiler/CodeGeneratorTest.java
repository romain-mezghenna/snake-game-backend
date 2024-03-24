package org.polytech.compiler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CodeGeneratorTest {

    @Test
    public void testGenerateCode() throws Exception {
        // Création directe d'un programme avec des commandes
        List<Parser.Command> commands = List.of(new Parser.Command("right"));
        Parser.Program program = new Parser.Program(commands);

        CodeGenerator codeGenerator = new CodeGenerator();
        String outputPath = "src/test/java/org/polytech/compiler/SnakeController.java";

        // Génération du code
        codeGenerator.generateCode(program, outputPath);

        // Lecture et vérification du contenu généré
        String generatedCode = Files.readString(Paths.get(outputPath));
        assertTrue(generatedCode.contains("public class SnakeController"), "Le code généré doit contenir 'public class SnakeController'");
        assertTrue(generatedCode.contains("s.setVelX(1);"), "Le code généré doit déplacer le snake vers la droite");
    }
}
