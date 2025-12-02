final Path input = Path.of("data/input.txt");
final Pattern patternPartOne = Pattern.compile("(\\d+)\\1");
final Pattern patternPartTwo = Pattern.compile("(\\d+)\\1+");

final boolean printDebug = false;

void main() throws IOException {
  IO.print("Part 1: ");
  executeWithPattern(patternPartOne);

  IO.println();

  IO.print("Part 2: ");
  executeWithPattern(patternPartTwo);
}

void executeWithPattern(final Pattern patternToUse) throws IOException {
  long added = 0;
  for (final String idRange : Files.readString(input).split(",")) {
    final String[] split = idRange.split("-");
    if (split[0].startsWith("0") || split[1].startsWith("0")) {
      // invalid ID
      continue;
    }

    final long from = Long.parseLong(split[0].strip());
    final long to = Long.parseLong(split[1].strip());

    for (long i = from; i <= to; i++) {
      final String textual = Long.toString(i);
      final boolean matches = patternToUse.matcher(textual).matches();

      if (printDebug) {
        System.out.printf("[DEBUG] %s %s the pattern.%n", textual, matches ? "matches" : "does not match");
      }

      if (matches) {
        added += i;
      }
    }

    if (printDebug) {
      System.out.println();
    }
  }

  System.out.printf("The invalid IDs total up to %d!%n", added);
}
