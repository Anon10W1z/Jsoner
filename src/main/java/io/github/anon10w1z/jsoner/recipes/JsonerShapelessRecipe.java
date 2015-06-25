package io.github.anon10w1z.jsoner.recipes;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class JsonerShapelessRecipe {
	private List<Object> inputs = Lists.newArrayList();
	private int outputQuantity;

	private JsonerShapelessRecipe(List<Object> inputs, int outputQuantity) {
		this.inputs = inputs.stream().filter(input -> input != null).collect(Collectors.toList());
		this.outputQuantity = outputQuantity;
	}

	public static JsonerShapelessRecipe of(List<Object> inputs, int outputQuantity) {
		return new JsonerShapelessRecipe(inputs, outputQuantity);
	}
}
