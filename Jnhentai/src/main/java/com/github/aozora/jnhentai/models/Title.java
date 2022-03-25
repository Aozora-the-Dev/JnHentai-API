package com.github.aozora.jnhentai.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Title implements Parcelable {
	public static final Creator<Title> CREATOR = new Creator<Title>() {
		@Override
		public Title createFromParcel(Parcel in) {
			return new Title(in);
		}

		@Override
		public Title[] newArray(int size) {
			return new Title[size];
		}
	};

	@SerializedName("english")
	private String englishTitle;

	@SerializedName("japanese")
	private String japaneseTitle;

	@SerializedName("pretty")
	private String prettyTitle;

	public Title(String englishTitle, String japaneseTitle, String prettyTitle) {
		this.englishTitle = englishTitle;
		this.japaneseTitle = japaneseTitle;
		this.prettyTitle = prettyTitle;
	}

	protected Title(Parcel in) {
		englishTitle = in.readString();
		japaneseTitle = in.readString();
		prettyTitle = in.readString();
	}

	public String getEnglishTitle() {
		return this.englishTitle;
	}

	public String getJapaneseTitle() {
		return this.japaneseTitle;
	}

	public String getPrettyTitle() {
		return this.prettyTitle;
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
		Title title = (Title) o;
		return Objects.equals(englishTitle, title.englishTitle)
				&& Objects.equals(japaneseTitle, title.japaneseTitle)
				&& Objects.equals(prettyTitle, title.prettyTitle);
	}

	@Override
	public String toString() {
		return "Title{"
				+ "englishTitle='"
				+ englishTitle
				+ '\''
				+ ", japaneseTitle='"
				+ japaneseTitle
				+ '\''
				+ ", prettyTitle='"
				+ prettyTitle
				+ '\''
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
		return Objects.hash(englishTitle, japaneseTitle, prettyTitle);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(englishTitle);
		dest.writeString(japaneseTitle);
		dest.writeString(prettyTitle);
	}
}