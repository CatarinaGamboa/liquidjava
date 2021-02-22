package repair.regen.processor.context;

import repair.regen.language.alias.Alias;

public class AliasWrapper {
	private String name;
	private Alias alias;

	public AliasWrapper(Alias alias) {
		name = alias.getName();
		this.alias = alias;
	}

	public String getName() {
		return name;
	}
	
	public Alias getAlias() {
		return alias;
	}

}
