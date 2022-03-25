package com.github.aozora.jnhentai.models;

import java.util.List;

public class SearchResult {
	private final List<Book> bookList;
	private int pagesOfResults;

	public SearchResult(List<Book> bookList, int pagesOfResults) {
		this.bookList = bookList;
		this.pagesOfResults = pagesOfResults;
	}

	public List<Book> getBookList() {
		return this.bookList;
	}

	public int getPagesOfResults() {
		return this.pagesOfResults;
	}
}