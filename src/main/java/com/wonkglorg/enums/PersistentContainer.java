package com.wonkglorg.enums;

import com.wonkglorg.Heads;
import org.bukkit.NamespacedKey;

public enum PersistentContainer
{
	ADVANCEMENT(new NamespacedKey(Heads.getInstance(),"advancement")),
	PERSISTENT_ADVANCEMENT(new NamespacedKey(Heads.getInstance(),"head_data"))
	
	
	;
	private final NamespacedKey namespacedKey;
	PersistentContainer(NamespacedKey namespacedKey)
	{
		this.namespacedKey = namespacedKey;
	}
	
	public NamespacedKey getNamespaceKey()
	{
		return namespacedKey;
	}
}