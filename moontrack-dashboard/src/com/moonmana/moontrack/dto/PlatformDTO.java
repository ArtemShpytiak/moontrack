package com.moonmana.moontrack.dto;

public class PlatformDTO implements Comparable<PlatformDTO> {
	private byte code;
	private String name;

	public PlatformDTO(byte code, String name) {
		this.code = code;
		this.name = name;
	}

	public byte getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return PlatformDTO.class.getName() + "[name=" + name + ", code=" + code + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PlatformDTO)) {
			return false;
		}
		PlatformDTO that = (PlatformDTO) o;
		return that.name.equals(name) && that.code == code;
	}

	@Override
	public int compareTo(PlatformDTO dto) {
		return name.compareTo(dto.name);
	}
}
