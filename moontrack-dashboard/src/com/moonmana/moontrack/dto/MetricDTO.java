package com.moonmana.moontrack.dto;

public class MetricDTO implements Comparable<MetricDTO> {

	private byte id;
	private String name;

	public MetricDTO(byte id, String name) {
		this.id = id;
		this.name = name;
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return MetricDTO.class.getName() + "[id=" + id + ", name=" + name + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof MetricDTO)) {
			return false;
		}
		MetricDTO that = (MetricDTO) o;
		return this.id == that.id;
	}

	@Override
	public int compareTo(MetricDTO dto) {
		return name.compareTo(dto.name);
	}

}
