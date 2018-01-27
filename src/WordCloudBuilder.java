import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class WordCloudBuilder {

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
	}

	public static WordCloud buildWordCloud(List<Word> words, Configurations configurations) {
		double minAvailable = words.stream().max(new Comparator<Word>() {
			@Override
			public int compare(Word o1, Word o2) {
				return Double.compare(o2.getFrequency(), o1.getFrequency());
			}
		}).get().getFrequency();

		double maxAvailable = words.stream().max(new Comparator<Word>() {
			@Override
			public int compare(Word o1, Word o2) {
				return Double.compare(o1.getFrequency(), o2.getFrequency());
			}
		}).get().getFrequency();

		System.out.println("Max :" + maxAvailable + "  Min : " + minAvailable);

		List<NormalizedWord> normalizedWords = words.stream()
				.map(e -> NormalizedWord.builder().word(e)
						.fontSize(findNormalizedFont(minAvailable, maxAvailable, configurations.getMinFontSize(),
								configurations.getMaxFontSize(), e.getFrequency()))
						.build())
				.collect(Collectors.toList());

		List<String> divTags = normalizedWords.stream()
				.map(e -> new StringBuilder().append(creatDivTagHtml(e)).append("&nbsp;&nbsp;").toString())
				.collect(Collectors.toList());
		return WordCloud.builder().normalizedWords(normalizedWords)
				.htmlData("<div>" + Joiner.on("").join(divTags) + "</div>").build();

	}

	public static double findNormalizedFont(double min, double max, double a, double b, double x) {
		return (((b - a) * (x - min)) / (max - min)) + a;
	}

	public static String creatDivTagHtml(NormalizedWord normalizedWord) {
		Word word = normalizedWord.getWord();
		StringBuilder stringBuilder = new StringBuilder();
		StringBuilder stringBuilder2 = new StringBuilder();
		if (word.getHtmlTag().getAttributesToValuesMap().containsKey("style")) {
			String value = word.getHtmlTag().getAttributesToValuesMap().get("style");
			value = value.replaceAll("font-size *: *[0-9]+", " ");
			value = value + ";" + "font-size: " + normalizedWord.getFontSize() + "px";
			word.getHtmlTag().getAttributesToValuesMap().put("style", value);
		} else {
			word.getHtmlTag().getAttributesToValuesMap().put("style",
					"font-size: " + normalizedWord.getFontSize() + "px");
		}
		for (String attribute : word.getHtmlTag().getAttributesToValuesMap().keySet()) {
			stringBuilder2.append(attribute).append("=\"")
					.append(word.getHtmlTag().getAttributesToValuesMap().get(attribute)).append("\"");
		}
		return stringBuilder.append("<").append(word.getHtmlTag().getTagName()).append(" ").append(stringBuilder2)
				.append(">").append(word.getHtmlTag().getTagData() == null ? "" : word.getHtmlTag().getTagData())
				.append("</").append(word.getHtmlTag().getTagName()).append(">").toString();

	}
}

@Builder
@Getter
class HtmlTag {
	private String tagName;
	private String tagData;
	// This will be added in the anchor tag as attributes. Values will be enclosed
	// in ". Also the font-size style will be overriden in the logic while
	// calculating the font sizes.
	private Map<String, String> attributesToValuesMap;
}

@Builder
@Getter
@ToString
class Word {
	private String word;
	private double frequency;
	private HtmlTag htmlTag;
}

@Builder
@Getter
class Configurations {
	private double maxFontSize;
	private double minFontSize;
	private WordCase wordCase;
}

enum WordCase {
	ALL_SMALL, ALL_CAPS, ALL_SENTENCE;
}

@Builder
@Getter
@ToString
class NormalizedWord {
	private Word word;
	private double fontSize;
}

@Builder
@Getter
@ToString
class WordCloud {
	private String htmlData;
	private List<NormalizedWord> normalizedWords;
}

