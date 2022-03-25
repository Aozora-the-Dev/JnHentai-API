package com.github.aozora.jnhentai.models;

public class BookTag {
	private int id;
	private String type;
	private String name;
	private String url;
	private int count;

	public BookTag(int id, String type, String name, String url, int count) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.url = url;
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}