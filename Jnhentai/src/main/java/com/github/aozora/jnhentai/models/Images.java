package com.github.aozora.jnhentai.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Images implements Parcelable {
	public static final Creator<Images> CREATOR = new Creator<Images>() {
		@Override
		public Images createFromParcel(Parcel in) {
			return new Images(in);
		}

		@Override
		public Images[] newArray(int size) {
			return new Images[size];
		}
	};
	private static final String TAG = "Images";
	private List<Image> pages;
	private Image cover;
	private Image thumbnail;

	public Images() {
		pages = new ArrayList<>();
	}

	protected Images(Parcel in) {
		pages = new ArrayList<>();
		in.readList(pages, Image.class.getClassLoader());
		cover = in.readParcelable(Image.class.getClassLoader());
		thumbnail = in.readParcelable(Image.class.getClassLoader());
	}

	public List<Image> getImages() {
		return pages;
	}

	public Image getCover() {
		return cover;
	}

	public Image getThumbnail() {
		return thumbnail;
	}

	public Image getImage(int pageNumber) {
		if (pageNumber <= pages.size() && pageNumber > 0) {
			return pages.get(pageNumber - 1);
		}
		return null;
	}

	public String getThumbnailExtension() {
		return typeToExtension(thumbnail.getType());
	}

	private String typeToExtension(String imageType) {
		switch (imageType) {
			case "p":
				return "png";
			case "j":
				return "jpg";
			default:
				Log.d(TAG, "getImageExtension: Page has type: " + imageType);
				return null;
		}
	}

	public String getCoverExtension() {
		return typeToExtension(cover.getType());
	}

	public String getImageExtension(int pageNumber) {
		if (pageNumber <= pages.size() && pageNumber > 0) {
			String pageType = pages.get(pageNumber - 1).getType();
			return typeToExtension(pageType);
		}
		return null;
	}

	@Override
	public String toString() {
		return "Images{" + "pages=" + pages + ", cover=" + cover + ", thumbnail=" + thumbnail + '}';
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(pages);
		dest.writeParcelable(cover, flags);
		dest.writeParcelable(thumbnail, flags);
	}
}