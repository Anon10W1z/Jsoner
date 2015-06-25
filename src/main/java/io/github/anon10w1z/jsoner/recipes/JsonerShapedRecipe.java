package io.github.anon10w1z.jsoner.recipes;

import java.util.Map;
import java.util.TreeMap;

/**
 * Wraps around recipes
 */
@SuppressWarnings("all")
public class JsonerShapedRecipe {
	private Map<String, Object> input = new TreeMap<>((string1, string2) -> Integer.compare(getSpot(string1), getSpot(string2)));
	private int outputQuantity;

	private JsonerShapedRecipe(Object[] input, int outputQuantity) {
		for (int spot = 0; spot < 9; ++spot)
			this.input.put(getSpotName(spot), input[spot] == null ? "empty" : input[spot]);
		this.outputQuantity = outputQuantity;
	}

	public static JsonerShapedRecipe of(Object[] input, int outputQuantity) {
		return new JsonerShapedRecipe(input, outputQuantity);
	}

	private static String getSpotName(int spot) {
		if (spot == 4)
			return "center";
		String spotName = "";
		switch (spot / 3) {
			case 0:
				spotName += "top-";
				break;
			case 1:
				spotName += "middle-";
				break;
			case 2:
				spotName += "bottom-";
		}
		switch (spot % 3) {
			case 0:
				spotName += "left";
				break;
			case 1:
				spotName += "center";
				break;
			case 2:
				spotName += "right";
				break;
		}
		return spotName;
	}

	private static int getSpot(String spotName) {
		if (spotName.equals("center"))
			return 4;
		int spot = 0;
		int dashIndex = spotName.indexOf('-');
		if (dashIndex < 0)
			return -1;
		switch (spotName.substring(0, dashIndex)) {
			case "middle":
				spot = 3;
				break;
			case "bottom":
				spot = 6;
		}
		switch (spotName.substring(dashIndex + 1)) {
			case "center":
				++spot;
				break;
			case "right":
				spot += 2;
				break;
		}
		return spot;
	}
}
