package com.lannstark.type;

public enum Job {

	BE("BackEnd Developer"),
	FE("FrontEnd Developer"),
	DEV_OPS("Develop Operation"),
	ML_OPS("MachineLearning Operation"),
	PM("Project Manager"),
	DS("Data Scientist");

	private final String description;

	Job(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
