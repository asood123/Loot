package com.game.loot.learning;

public abstract class Features {
	public int output;
	
	public void setOutput(int output) {
		this.output = output;
	}
	
	public abstract String getFeatureHeaders();
	
	public abstract String getOutputHeader();
	
	public abstract String getFeaturesString();
	
	public String getOutputString() {
		return Integer.toString(output);
	}
}
