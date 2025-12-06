final Path inputPath = Path.of("data/input.txt");
static final boolean debug1 = false;
static final boolean debug2 = false;

static final NumberFormat compactFormat = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);

void main() throws IOException {
  final String input = Files.readString(inputPath);
  final String[] split = input.split("\n\n");

  final String[] freshRanges = split[0].strip().split("\n");
  final String[] ingredientIds = split[1].strip().split("\n");

  final List<FreshRange> freshRangesList = parseFreshRanges(freshRanges);
  IO.println("[PART 1] The number of fresh ingredients is %d!".formatted(countFreshIngredients(freshRangesList, ingredientIds)));
  IO.println("[PART 2] The count of fresh ingredient IDs is %d!".formatted(countFreshIngredientIds(freshRangesList)));
}

List<FreshRange> parseFreshRanges(final String[] ranges) {
  final List<FreshRange> out = new ArrayList<>(ranges.length);
  for (final String range : ranges) {
    final int splitIndex = range.indexOf('-');
    out.add(new FreshRange(
      Long.parseLong(range, 0, splitIndex, 10),
      Long.parseLong(range, splitIndex + 1, range.length(), 10)
    ));
  }
  return out;
}

int countFreshIngredients(final List<FreshRange> freshRanges, final String[] ingredients) {
  int out = 0;

  OmgIUsedALabel:
  for (final String id : ingredients) {
    for (final FreshRange range : freshRanges) {
      if (range.isInRange(Long.parseLong(id))) {
        debug("ID %s is inside range [%d|%d]", id, range.min(), range.max());
        out++;
        continue OmgIUsedALabel;
      }
    }
    debug("No fitting range found for %s.", id);
  }

  return out;
}

long countFreshIngredientIds(final List<FreshRange> freshRanges) {
  return new IdRangeHolder(freshRanges).getIdCount();
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

record IdRangeHolder(List<FreshRange> ranges) {
  IdRangeHolder(final List<FreshRange> ranges) {
    this.ranges = new ArrayList<>(ranges.size());
    ranges.forEach(this::insert);
  }

  private void insert(final FreshRange range) {
    for (int i = 0, rangesSize = ranges.size(); i < rangesSize; i++) {
      final FreshRange existingRange = ranges.get(i);

      if (existingRange.isInRange(range.min()) && existingRange.isInRange(range.max())) {
        // This range can fit fully inside an existing range; reject it
        debug2("The range %s fits entirely into %s; discarding.", range, existingRange);
        return;
      }

      if (range.min() < existingRange.min() && existingRange.isInRange(range.max())) {
        // The range has a smaller min, but overlaps with the range
        debug2("The range %s has a smaller min, but overlaps with %s.", range, existingRange);
        ranges.remove(i);
        insert(new FreshRange(range.min(), existingRange.max())); // insert the new range
        return;
      }

      if (range.max() > existingRange.max() && existingRange.isInRange(range.min())) {
        // The range has a bigger max, but overlaps with the range
        debug2("The range %s has a bigger max, but overlaps with %s.", range, existingRange);
        ranges.remove(i);
        insert(new FreshRange(existingRange.min(), range.max())); // insert the new range
        return;
      }

      if (range.isInRange(existingRange.min()) && range.isInRange(existingRange.max())) {
        // The range is unconditionally bigger than this range
        debug2("The range %s could contain %s; discarding the existing range.", range, existingRange);
        ranges.remove(i);
        insert(range); // insert the new range
        return;
      }

      if (range.min() < existingRange.min() && range.max() < existingRange.max()) {
        // The range has no overlap with any range
        debug2("The range %s has no overlap with existing ranges (%s).\n", range, ranges);
        ranges.add(i, range);
        return;
      }
    }

    // The range has no overlap with any range and is bigger than any existing ones
    debug2("The range %s contains bigger numbers than any other range. (%s)\n", range, ranges);
    ranges.add(range);
  }

  long getIdCount() {
    long out = 0;
    for (final FreshRange range : ranges) {
      out += range.max() + 1L - range.min();
    }
    return out;
  }
}

record FreshRange(long min, long max) {
  boolean isInRange(final long id) {
    return min <= id && id <= max;
  }

  @Override
  public String toString() {
    return "[" + compactFormat.format(min) + "|" + compactFormat.format(max) + ']';
  }
}