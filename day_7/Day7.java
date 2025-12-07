static final Path inputPath = Path.of("data/input.txt");

void main() throws IOException {
  final String input = Files.readString(inputPath);
  final String[] lines = input.strip().split("\n");

  IO.println("[Part 1] Total tachyon splits: %d.".formatted(countTachyonSplits(lines)));
  IO.println("[Part 2] Total theoretically possible tachyon splits: %d.".formatted(countMultiversalTachyonSplits(lines)));
}

int countTachyonSplits(final String[] lines) {
  int tachyonSplitCount = 0;

  final String firstLine = lines[0].strip();
  final byte[] currBeams = new byte[firstLine.length()];

  Arrays.fill(currBeams, (byte) 0);
  currBeams[firstLine.indexOf('S')] = 1;

  for (int rowIndex = 2, linesLength = lines.length; rowIndex < linesLength; rowIndex += 2) {
    final String line = lines[rowIndex];

    for (int columnIndex = 1; columnIndex < line.length() - 1; columnIndex++) {
      if (currBeams[columnIndex] == 1 && line.charAt(columnIndex) == '^') {
        currBeams[columnIndex - 1] = 1;
        currBeams[columnIndex + 1] = 1;
        currBeams[columnIndex] = 0;
        tachyonSplitCount++;
      }
    }
  }

  return tachyonSplitCount;
}

long countMultiversalTachyonSplits(final String[] lines) {
  final String firstLine = lines[0].strip();
  final long[] currBeams = new long[firstLine.length()];

  Arrays.fill(currBeams, 0);
  currBeams[firstLine.indexOf('S')] = 1;

  for (int rowIndex = 2, linesLength = lines.length; rowIndex < linesLength; rowIndex += 2) {
    final String line = lines[rowIndex];

    for (int columnIndex = 1; columnIndex < line.length() - 1; columnIndex++) {
      if (currBeams[columnIndex] >= 1 && line.charAt(columnIndex) == '^') {
        currBeams[columnIndex - 1] += currBeams[columnIndex];
        currBeams[columnIndex + 1] += currBeams[columnIndex];
        currBeams[columnIndex] = 0;
      }
    }
  }

  long tachyonSplitCount = 0;
  for (final long beam : currBeams) {
    tachyonSplitCount += beam;
  }

  return tachyonSplitCount;
}