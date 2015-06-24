package io.github.anon10w1z.jsoner.items;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class JsonerRecipe {
	private List<Object> inputs;
	private int outputQuantity;

	private JsonerRecipe(List<Object> inputs, int outputQuantity) {
		this.inputs = inputs.stream().map(input -> input == null ? "empty" : input).collect(Collectors.toList());
		this.outputQuantity = outputQuantity;
	}

	public static JsonerRecipe of(List<Object> inputs, int outputQuantity) {
		return new JsonerRecipe(inputs, outputQuantity);
	}
}
