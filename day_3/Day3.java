final Path input = Path.of("data/input.txt");

void main() throws IOException {
  final List<String> lines = Files.readAllLines(input);

  IO.println("[PART 1] Found a total of %s jolts!".formatted(solveFor(lines, 2).toString()));
  IO.println("[PART 2] Found a total of %s jolts!".formatted(solveFor(lines, 12).toString()));
}

BigInteger solveFor(final List<String> lines, final int joltageSize) {
  BigInteger totalJolts = BigInteger.ZERO;

  for (final String line : lines) {
    final int[] jolts = new int[joltageSize];
    Arrays.fill(jolts, -1);

    final char[] charArray = line.toCharArray();
    for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
      final char jolt = charArray[i];
      final int joltValue = jolt - '0';

      for (int j = Math.max(0, joltageSize - (charArrayLength - i)); j < joltageSize; j++) {
        if (joltValue > jolts[j]) {
          jolts[j] = joltValue;
          for (int z = j + 1; z < joltageSize; z++) {
            jolts[z] = -1;
          }
          break;
        }
      }
    }

    for (int c = 0; c < joltageSize; c++) {
      final int val = jolts[c];
      if (val == -1) {
        IO.println("Something went terribly wrong. Line: %s. jolts: [%s].".formatted(line, String.join(", ", Arrays.stream(jolts).mapToObj(Integer::toString).toList())));
        System.exit(64);
      }

      final BigInteger bigVal = BigInteger.valueOf(val).multiply(BigInteger.TEN.pow(joltageSize - c - 1));
      totalJolts = totalJolts.add(bigVal);
    }
  }

  return totalJolts;
}