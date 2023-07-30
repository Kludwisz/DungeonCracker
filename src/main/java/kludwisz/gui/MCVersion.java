package kludwisz.gui;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public enum MCVersion {
	v1_17("1.17", 5, 0, 0),
	v1_16("1.16", 4, 0, 0),
	v1_15("1.15", 3, 0, 0),
	v1_14("1.14", 2, 0, 0),
	v1_13("1.13", 1, 0, 0);

	public static final Map<String, MCVersion> STRING_TO_VERSION = Arrays.stream(values()).collect(Collectors.toMap(MCVersion::toString, (o) -> o));
	public final String name;
	public final int release;
	public final int subVersion;
	public final int snapshot;

	MCVersion(String name, int release, int subVersion, int snapshot) {
		this.name = name;
		this.release = release;
		this.subVersion = subVersion;
		this.snapshot = snapshot;
	}

	public static MCVersion fromString(String name) {
		return STRING_TO_VERSION.get(name);
	}

	public boolean isNewerThan(MCVersion v) {
		return this.compareTo(v) < 0;
	}

	public boolean isOlderThan(MCVersion v) {
		return this.compareTo(v) > 0;
	}

	public boolean isBetween(MCVersion a, MCVersion b) {
		return this.compareTo(a) <= 0 && this.compareTo(b) >= 0;
	}

	public String toString() {
		return this.name;
	}
}


