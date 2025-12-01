final Path filePath = Path.of("data/input.txt");

void main() throws IOException {
  final String input = Files.readString(filePath);

  int found = 0;
  int dial = 50;

  for (final String line : input.split("\n")) {
    //<editor-fold desc="No spoilers :)">
    final int num = Integer.parseInt(line.substring(1));

    System.out.printf("[DEBUG] Current dial: %d. ", dial);
    if (line.charAt(0) == 'L') {
      dial -= num;
      while (dial < 0) {
        dial += 100;
      }
    } else {
      dial += num;
      while (dial > 99) {
        dial -= 100;
      }
    }
    System.out.printf("line: %s. new dial: %d%n", line, dial);

    if (dial == 0) {
      found++;
    }
    //</editor-fold>
  }

  System.out.println("Found a total of " + found + " zeroes!");
}
