# WordCloud



# Word Cloud using Java
There is growing need to create word clouds. This gives a very basic implementation of word cloud which gives the fonts to words and you can style them in HTML. This gives you flexibility ho have word cloud in HTML which can be used in many places. Words are given font sizes depending upon the frequency.

## Getting Started

This is a simple maven project and can be imported as a maven project. Here is an example of adding five unigrams and converting them into HTML anchor tag. Their fonts will depend on the frequency assigned to them.
```
public static void main(String args[]) {
		List<Word> words = new ArrayList<>();
		int i = 1;
		for (String word : Arrays.asList("one", "two", "three", "four", "five")) {
			Map<String, String> attributeMap = new HashMap<>();
			attributeMap.put("href", "http://example.com/" + word);
			HtmlTag htmlTag = HtmlTag.builder().tagName("a").tagData(word).attributesToValuesMap(attributeMap).build();
			Word word2 = Word.builder().htmlTag(htmlTag).frequency(100 * i).word(word).build();
			words.add(word2);
			i++;
		}
		Configurations configurations = Configurations.builder().maxFontSize(50).minFontSize(10)
				.wordCase(WordCase.ALL_CAPS).build();

		System.out.println(buildWordCloud(words, configurations));
		System.out.println(buildWordCloud(words, configurations).getNormalizedWords());
	}
```

# Sample output.



<!-- HTML CODE-->
<html>
  <h1>This is header</h1>
 <html>

