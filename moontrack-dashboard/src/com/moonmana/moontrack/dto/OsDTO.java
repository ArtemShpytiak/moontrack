package com.moonmana.moontrack.dto;

public class OsDTO implements Comparable<OsDTO> {
	private byte code;
	private String name;

	public OsDTO(byte code, String name) {
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
		return OsDTO.class.getName() + "[name=" + name + ", code=" + code + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof OsDTO)) {
			return false;
		}
		OsDTO that = (OsDTO) o;
		return that.name.equals(name) && that.code == code;
	}

	@Override
	public int compareTo(OsDTO dto) {
		return name.compareTo(dto.name);
	}
}
