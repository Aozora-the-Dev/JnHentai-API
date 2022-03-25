package com.github.aozora.jnhentai.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Image implements Parcelable {
	public static final Creator<Image> CREATOR = new Creator<Image>() {
		@Override
		public Image createFromParcel(Parcel in) {
			return new Image(in);
		}

		@Override
		public Image[] newArray(int size) {
			return new Image[size];
		}
	};

	@SerializedName("t")
	private String type;

	@SerializedName("w")
	private int width;

	@SerializedName("h")
	private int height;

	public Image(String type, int width, int height) {
		this.type = type;
		this.width = width;
		this.height = height;
	}

	protected Image(Parcel in) {
		type = in.readString();
		width = in.readInt();
		height = in.readInt();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "Image{" + "type='" + type + '\'' + ", width=" + width + ", height=" + height + '}';
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.type);
		dest.writeInt(width);
		dest.writeInt(height);
	}
}