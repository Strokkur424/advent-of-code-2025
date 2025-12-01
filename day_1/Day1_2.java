final Path filePath = Path.of("data/input.txt");

void main() throws IOException {
  final String input = Files.readString(filePath);

  int found = 0;
  int dial = 50;

  for (final String line : input.split("\n")) {
    //<editor-fold desc="No spoilers :)">
    int num = Integer.parseInt(line.substring(1));

    System.out.printf("[DEBUG] %s. ", line);
    while (num >= 100) {
      num -= 100;
      found++;
    }

    final boolean wasZero = dial == 0;

    if (line.charAt(0) == 'L') {
      dial -= num;
    } else {
      dial += num;
    }

    if (dial == 0) {
      if (!wasZero) {
        found++;
      }
    } else if (dial < 0) {
      dial += 100;
      if (!wasZero) {
        found++;
      }
    } else if (dial >= 100) {
      dial -= 100;
      if (!wasZero) {
        found++;
      }
    }

    System.out.printf("New found: %d. New dial: %d%n", found, dial);
    //</editor-fold>
  }

  System.out.println("Found a total of " + found + " zeroes!");
}
