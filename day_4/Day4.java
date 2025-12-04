final Path input = Path.of("data/input.txt");

void main() throws IOException {
  final String[] lines = Files.readString(input).strip().split("\n");
  final byte[][] mapped = mapPaperRollsTo2DArray(lines);

  IO.println("[PART 1] Found a total of %d accessible paper rolls.".formatted(countAccessiblePaperRolls(mapped, false)));
  IO.println("[PART 2] Found a total of %d accessible paper rolls.".formatted(countAccessiblePaperRolls(mapped, true)));
}

byte[][] mapPaperRollsTo2DArray(final String[] lines) {
  final int xSize = lines[0].length();
  final int ySize = lines.length;

  final byte[][] out = new byte[ySize][xSize];

  for (int y = 0; y < ySize; y++) {
    final String line = lines[y];
    if (line.length() != xSize) {
      IO.println("[ERROR] A fatal exception occurred: expected a length of %d, but %d was present for line <%s>!".formatted(xSize, line.length(), line));
      System.exit(64);
    }

    final char[] array = line.toCharArray();
    for (int x = 0; x < xSize; x++) {
      if (array[x] == '@') {
        out[y][x] = 1;
      }
    }
  }

  return out;
}

int countAccessiblePaperRolls(final byte[][] paperRolls, final boolean partTwo) {
  final int xSize = paperRolls[0].length;
  final int ySize = paperRolls.length;

  int accessibleCount = 0;

  for (int y = 0; y < ySize; y++) {
    for (int x = 0; x < xSize; x++) {
      if (paperRolls[y][x] == 0) {
        continue;
      }

      if (isPaperRollAccessible(x, y, paperRolls)) {
        accessibleCount++;
        if (partTwo) {
          paperRolls[y][x] = 0;
        }
      }
    }
  }

  if (!partTwo || accessibleCount == 0) {
    return accessibleCount;
  }
  return accessibleCount + countAccessiblePaperRolls(paperRolls, true);
}

boolean isPaperRollAccessible(final int x, final int y, final byte[][] paperRolls) {
  int surrounded = getPaperRoll(x - 1, y - 1, paperRolls) + getPaperRoll(x, y - 1, paperRolls) + getPaperRoll(x + 1, y - 1, paperRolls)
    + getPaperRoll(x - 1, y, paperRolls) + getPaperRoll(x + 1, y, paperRolls)
    + getPaperRoll(x - 1, y + 1, paperRolls) + getPaperRoll(x, y + 1, paperRolls) + getPaperRoll(x + 1, y + 1, paperRolls);

  return surrounded < 4;
}

int getPaperRoll(final int x, final int y, final byte[][] paperRolls) {
  if (x < 0 || y < 0 || x >= paperRolls[0].length || y >= paperRolls.length) {
    return 0;
  }
  return paperRolls[y][x];
}
