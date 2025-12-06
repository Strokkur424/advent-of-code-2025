final Path inputPath = Path.of("data/input.txt");
static final boolean debug1 = false;
static final boolean debug2 = false;

void main() throws IOException {
  final String input = Files.readString(inputPath);
  final String[] lines = input.strip().split("\n");

  IO.println("[Part 1] Total results: %s.".formatted(computePart1Result(lines)));
  IO.println("[Part 2] Total results: %s.".formatted(computePart2Result(lines)));
}

BigInteger computePart1Result(final String[] lines) {
  final Operator[] operators = getOperators(lines[lines.length - 1]);
  final BigInteger[] columnResults = new BigInteger[operators.length];
  Arrays.fill(columnResults, BigInteger.ZERO);

  for (int row = 0, linesLength = lines.length - 1; row < linesLength; row++) {
    final String line = lines[row];
    final String[] split = line.strip().split(" +");

    for (int column = 0; column < operators.length; column++) {
      columnResults[column] = operators[column].debugCompute(columnResults[column], new BigInteger(split[column]));
    }
  }

  BigInteger out = BigInteger.ZERO;
  for (final BigInteger result : columnResults) {
    out = out.add(result);
  }
  return out;
}

BigInteger computePart2Result(final String[] lines) {
  final Operator[] operators = getOperators(lines[lines.length - 1]);
  final Iterator<BigInteger[]> iterator = new ColumnNumbersIterator(Arrays.copyOfRange(lines, 0, lines.length - 1));

  BigInteger out = BigInteger.ZERO;
  for (int index = operators.length - 1; index >= 0; index--) {
    if (!iterator.hasNext()) {
      System.err.printf("[ERROR] No more column numbers found, whilst more operator indices are present! index: %d%n", index);
      System.exit(64);
    }

    final BigInteger[] pairs = iterator.next();
    final Operator operator = operators[index];

    BigInteger num = pairs[0];
    for (int i = 1, pairsLength = pairs.length; i < pairsLength; i++) {
      num = operator.debugCompute2(num, pairs[i]);
    }

    out = out.add(num);
  }

  return out;
}

static class ColumnNumbersIterator implements Iterator<BigInteger[]> {
  final String[] rows;
  final int rowLength;
  int index = 0;

  ColumnNumbersIterator(final String[] input) {
    this.rows = input;
    this.rowLength = Arrays.stream(input)
      .mapToInt(String::length)
      .max()
      .getAsInt();
  }

  @Override
  public boolean hasNext() {
    return index < rowLength;
  }

  @Override
  public BigInteger[] next() {
    final List<BigInteger> out = new ArrayList<>();

    while (true) {
      final StringBuilder builder = new StringBuilder(rows.length);
      index++;

      if (index > rowLength) {
        break;
      }

      final int charIndex = rowLength - index;
      for (final String row : rows) {
        if (row.length() <= charIndex) {
          debug2("Row <%s> index <%d> has no character.", row, index);
          continue;
        }

        final char c = row.charAt(charIndex);
        debug2("Row <%s> index <%d> has character: %s", row, index, c);
        if (c == ' ') {
          continue;
        }
        builder.append(c);
      }

      if (builder.isEmpty()) {
        break;
      }

      out.add(new BigInteger(builder.toString()));
    }

    return out.toArray(BigInteger[]::new);
  }
}

Operator[] getOperators(final String lastLine) {
  final String[] split = lastLine.strip().split(" +");
  final Operator[] operators = new Operator[split.length];

  for (int i = 0, splitLength = split.length; i < splitLength; i++) {
    final String operator = split[i].strip();
    operators[i] = operator.equals("+") ? BigInteger::add : (m1, m2) -> Objects.equals(m1, BigInteger.ZERO) ? m2 : m1.multiply(m2);
  }

  return operators;
}

@FunctionalInterface
interface Operator {
  BigInteger compute(BigInteger x, BigInteger y);

  default BigInteger debugCompute(final BigInteger x, final BigInteger y) {
    final BigInteger out = compute(x, y);
    debug("%s and %s computed together add up to %s.", x, y, out);
    return out;
  }

  default BigInteger debugCompute2(final BigInteger x, final BigInteger y) {
    final BigInteger out = compute(x, y);
    debug2("%s and %s computed together add up to %s.", x, y, out);
    return out;
  }
}

static void debug(final String message, final Object... args) {
  if (debug1) {
    IO.println("[DEBUG] " + message.formatted(args));
  }
}

static void debug2(final String message, final Object... args) {
  if (debug2) {
    IO.println("[DEBUG] " + message.formatted(args));
  }
}