package com.github.aozora.jnhentai.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Book implements Parcelable {
	public static final Creator<Book> CREATOR = new Creator<Book>() {
		@Override
		public Book createFromParcel(Parcel in) {
			return new Book(in);
		}

		@Override
		public Book[] newArray(int size) {
			return new Book[size];
		}
	};
	private final int id;

	@SerializedName("media_id")
	private final String mediaId;

	private final Title title;
	private final Images images;
	private final String scanlator;

	@SerializedName("upload_date")
	private final long uploadDate;

	private final List<Tag> tags;

	@SerializedName("num_pages")
	private final int numberOfPages;

	@SerializedName("num_favorites")
	private final int numberOfFavorites;

	public Book(int id, String mediaId, Title title, Images images, String scanlator,
				long uploadDate, List<Tag> tags, int numberOfPages, int numberOfFavorites) {
		this.id = id;
		this.mediaId = mediaId;
		this.title = title;
		this.images = images;
		this.scanlator = scanlator;
		this.uploadDate = uploadDate;
		this.tags = tags;
		this.numberOfPages = numberOfPages;
		this.numberOfFavorites = numberOfFavorites;
	}

	protected Book(Parcel in) {
		id = in.readInt();
		mediaId = in.readString();
		title = in.readParcelable(Title.class.getClassLoader());
		images = in.readParcelable(Images.class.getClassLoader());
		scanlator = in.readString();
		uploadDate = in.readLong();
		tags = new ArrayList<>();
		in.readList(tags, Tag.class.getClassLoader());
		numberOfPages = in.readInt();
		numberOfFavorites = in.readInt();
	}

	public int getId() {
		return id;
	}

	public String getMediaId() {
		return mediaId;
	}

	public Title getTitle() {
		return title;
	}

	public Images getImages() {
		return images;
	}

	public String getScanlator() {
		return scanlator;
	}

	public long getUploadDate() {
		return uploadDate;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public int getNumberOfFavorites() {
		return numberOfFavorites;
	}

	public String getArtist() {
		for (Tag tag : tags) {
			if (tag.getType().equals("artist")) {
				return tag.getName();
			}
		}
		return "";
	}

	public String getLanguage() {
		for (Tag tag : tags) {
			if (tag.getType().equals("language") && !tag.getName().equals("translated")) {
				return tag.getName();
			}
		}
		return "";
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Book book = (Book) o;
		return id == book.id
				&& uploadDate == book.uploadDate
				&& numberOfPages == book.numberOfPages
				&& numberOfFavorites == book.numberOfFavorites
				&& mediaId.equals(book.mediaId)
				&& title.equals(book.title)
				&& images.equals(book.images)
				&& scanlator.equals(book.scanlator)
				&& Objects.equals(tags, book.tags);
	}

	@Override
	public String toString() {
		return "Book{"
				+ "id="
				+ id
				+ ", mediaId='"
				+ mediaId
				+ '\''
				+ ", title="
				+ title
				+ ", images="
				+ images
				+ ", scanlator='"
				+ scanlator
				+ '\''
				+ ", uploadDate="
				+ uploadDate
				+ ", tags="
				+ tags
				+ ", numberOfPages="
				+ numberOfPages
				+ ", numberOfFavorites="
				+ numberOfFavorites
				+ '}';
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	public int hashCode() {
		return Objects.hash(
				id, mediaId, title, images, scanlator, uploadDate, tags, numberOfPages, numberOfFavorites);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(mediaId);
		dest.writeParcelable(title, flags);
		dest.writeParcelable(images, flags);
		dest.writeString(scanlator);
		dest.writeLong(uploadDate);
		dest.writeList(tags);
		dest.writeInt(numberOfPages);
		dest.writeInt(numberOfFavorites);
	}
}