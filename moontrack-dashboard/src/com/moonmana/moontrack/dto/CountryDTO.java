package com.moonmana.moontrack.dto;

public class CountryDTO implements Comparable<CountryDTO> {
	private String code;
	private String name;

	public CountryDTO(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return CountryDTO.class.getName() + "[name=" + name + ", code=" + code + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof CountryDTO)) {
			return false;
		}
		CountryDTO that = (CountryDTO) o;
		return that.name.equals(name) && that.code.equals(code);
	}

	@Override
	public int compareTo(CountryDTO dto) {
		return name.compareTo(dto.name);
	}
}
