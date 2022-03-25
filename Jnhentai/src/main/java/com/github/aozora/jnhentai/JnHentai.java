package com.github.aozora.jnhentai;

import android.util.Log;

import androidx.annotation.NonNull;

import com.github.aozora.jnhentai.callbacks.JnHentaiCallback;
import com.github.aozora.jnhentai.models.Book;
import com.github.aozora.jnhentai.models.SearchResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JnHentai {
	private static final String TAG = "JnHentai";

	//Root search formats
	private static final String API_BASE_URL = "https://nhentai.net";
	private static final String STANDARD_QUERY = API_BASE_URL + "/api/galleries/search?query=%s";
	private static final String RELATED_QUERY = API_BASE_URL + "/api/gallery/%d/related";
	private static final String TAG_QUERY = API_BASE_URL + "/api/galleries/tagged?tag_id=%d";
	private static final String DETAILS_QUERY = API_BASE_URL + "/api/gallery/%d";
	private static final String IMAGE_BASE_URL = "https://i.nhentai.net";
	private static final String PAGE_QUERY = IMAGE_BASE_URL + "/galleries/%s/%d.%s";
	private static final String THUMBNAIL_QUERY = "https://t.nhentai.net/galleries/%s/thumb.%s";
	private static final String COVER_QUERY = "https://t.nhentai.net/galleries/%s/cover.%s";

	//Useful for getting all books and applying a sorting option
	private static final String SEARCH_ALL_URL = API_BASE_URL + "api/galleries/search?query=pages:>1";

	//Search sort options
	private static final String SORT_POPULAR_TODAY = "&sort=popular-today";
	private static final String SORT_POPULAR_WEEK = "&sort=popular-week";
	private static final String SORT_POPULAR_ALL_TIME = "&sort=popular";

	//Search page option
	private static final String PAGE = "&page=%d";

	//Results are naturally given by most recently uploaded.

	private static JnHentai instance = null;
	private final OkHttpClient client;

	// TODO: Consider making entirely static

	/**
	 * Singleton constructor for the JnHentai API Wrapper.
	 */
	private JnHentai() {
		client = new OkHttpClient();
	}

	/**
	 * Get the instance of the JnHentai API Wrapper.
	 *
	 * @return the instance of the JnHentai API Wrapper.
	 */
	public static JnHentai getInstance() {
		if (instance == null) {
			instance = new JnHentai();
		}
		return instance;
	}

	public static String getCoverUrl(Book book) {
		Log.d(TAG, "getCoverUrl: Getting cover for book: " + book.getTitle().getPrettyTitle());
		return String.format(COVER_QUERY, book.getMediaId(), book.getImages().getCoverExtension());
	}

	public static String getThumbnailUrl(Book book) {
		Log.d(TAG, "getThumbnailUrl: Getting thumbnail for book: " + book.getTitle().getPrettyTitle());
		return String.format(THUMBNAIL_QUERY, book.getMediaId(), book.getImages().getThumbnailExtension());
	}

	public static String getPageUrl(Book book, int pageNumber) {
		Log.d(TAG, "getPageUrl: Getting page " + pageNumber + " for book: " + book.getTitle().getPrettyTitle());
		return String.format(PAGE_QUERY, book.getMediaId(), pageNumber, book.getImages().getImageExtension(pageNumber));
	}

	public void search(String query, JnHentaiCallback<SearchResult> callback) {
		Log.d(TAG, "Searching for books by query.");
		query = query.replace(' ', '+');
		search(query, 1, callback);
	}

	public void search(String query, int page, JnHentaiCallback<SearchResult> callback) {
		query = query.replace(' ', '+');
		String url;
		if (page > 1) {
			url = String.format(STANDARD_QUERY + PAGE, query, page);
		} else {
			url = String.format(STANDARD_QUERY, query);
		}
		Log.d(TAG, "Searching for books by query : " + query);
		Request request = createRequest(url);
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
				Log.e(TAG, "onFailure: Failure during OkHttp Query request: " + e.getMessage());
			}

			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response)
					throws IOException {
				Log.d(TAG, "URL: " + url);
				if (!response.isSuccessful()) {
					Log.e(TAG, Objects.requireNonNull(response.body()).string());
					throw new IOException("Failure during OkHttp Query request.");
				}
				JsonObject jsonObject = JsonParser.parseString(Objects.requireNonNull(response.body()).string()).getAsJsonObject();
				int pagesOfResults = jsonObject.get("num_pages").getAsInt();
				JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
				Gson gson = new Gson();
				List<Book> bookList = new ArrayList<>(1);
				for (JsonElement jsonElement : jsonArray) {
					bookList.add(gson.fromJson(jsonElement, Book.class));
				}
				callback.call(new SearchResult(bookList, pagesOfResults));
			}
		});
	}

	/**
	 * Create a new Okhttp JnHentaiRequest with using the given url.
	 *
	 * @param url the url for the request
	 *
	 * @return the JnHentaiRequest object
	 */
	private Request createRequest(String url) {
		return new Request.Builder().url(url).header("User-Agent", "J-nHentai API").build();
	}

	/**
	 * @param tagId the id of the tag to search
	 * @param callback Type List<Book>
	 */
	public void searchByTag(int tagId, JnHentaiCallback<List<Book>> callback) {
		Log.d(TAG, "searchByTag: Searching for books with tag ID: " + tagId);
		String url = String.format(Locale.getDefault(), TAG_QUERY, tagId);
		Request request = createRequest(url);
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response)
					throws IOException {
				if (!response.isSuccessful()) {
					throw new IOException("Failure while searching by tag.");
				}
				List<Book> bookList = getArrayResponse(response);
				callback.call(bookList);
			}
		});
	}

	/**
	 * Format the JSON response into a list of Books.
	 *
	 * @param okhttpResponse the response from the API query
	 */
	private List<Book> getArrayResponse(Response okhttpResponse) throws IOException {
		ArrayList<Book> results = new ArrayList<>();
		JsonArray jsonArray =
				JsonParser.parseString(Objects.requireNonNull(okhttpResponse.body()).string())
						.getAsJsonArray();
		Gson gson = new Gson();
		for (JsonElement jsonElement : jsonArray) {
			Book book = gson.fromJson(jsonElement, Book.class);
			results.add(book);
		}
		return results;
	}

	/**
	 * @param book the book in reference.
	 * @param callback Type List<Book>
	 */
	public void related(Book book, JnHentaiCallback<List<Book>> callback) {
		Log.d(TAG, "related: Searching for related books");
		String url = String.format(Locale.getDefault(), RELATED_QUERY, book.getId());
		Request request = createRequest(url);
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response)
					throws IOException {
				if (!response.isSuccessful()) {
					throw new IOException("Failure while searching for related books.");
				}
				List<Book> bookList = getArrayResponse(response);
				callback.call(bookList);
			}
		});
	}

	/**
	 * @param bookId the id of the book
	 * @param callback Type Book
	 */
	public void details(int bookId, JnHentaiCallback<Book> callback) {
		Log.d(TAG, "details: Asking for book details");
		String url = String.format(Locale.getDefault(), DETAILS_QUERY, bookId);
		Request request = createRequest(url);
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
				Log.e(TAG, "details:onFailure: Failure during OkHttp Details request: " + e.getMessage());
			}

			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response)
					throws IOException {
				if (!response.isSuccessful()) {
					throw new IOException("Failure during OkHttp Details request.");
				}
				JsonElement jsonElement =
						JsonParser.parseString(Objects.requireNonNull(response.body()).string());
				Gson gson = new Gson();
				Book book = gson.fromJson(jsonElement, Book.class);
				callback.call(book);
			}
		});
	}
}