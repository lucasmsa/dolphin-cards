package br.com.dolphinCards.Functional.Karate.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdjustTerminalConfigurationsAndRunCommand {
    public void run(String cmdCommand) throws IOException, InterruptedException {
        String cmd = cmdCommand;
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);
        pr.waitFor();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        new PrintKarateTestsResults().run(bufferedReader);
        return;
    }
}
