package dev.norbu.donezo;

import dev.norbu.donezo.cli.CLI;
import dev.norbu.donezo.test.Tests;

public class App {

  public static void main(String[] args) {
    Tests.run();
    CLI.init(args);
  }
}
