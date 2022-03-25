package com.github.aozora.jnhentai.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Tag implements Parcelable {
	public static final Creator<Tag> CREATOR = new Creator<Tag>() {
		@Override
		public Tag createFromParcel(Parcel in) {
			return new Tag(in);
		}

		@Override
		public Tag[] newArray(int size) {
			return new Tag[size];
		}
	};
	private final int id;
	/*The list of possible types are:
	 * "artist" (0,n)
	 * "categories" (1,n)?
	 * "character" (0,n)
	 * "group" (0,n)
	 * "language" (1)
	 * "parody" (0,n)
	 * "tag" (0,n)?
	 */
	private final String type;
	private final String name;

	@SerializedName("url")
	private final String tagUrl;

	@SerializedName("count")
	private final int numberOfUses;

	public Tag(int id, String type, String name, String tagUrl, int numberOfUses) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.tagUrl = tagUrl;
		this.numberOfUses = numberOfUses;
	}

	protected Tag(Parcel in) {
		id = in.readInt();
		type = in.readString();
		name = in.readString();
		tagUrl = in.readString();
		numberOfUses = in.readInt();
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getTagUrl() {
		return tagUrl;
	}

	public int getNumberOfUses() {
		return numberOfUses;
	}

	@Override
	public String toString() {
		return "Tag{"
				+ "id="
				+ id
				+ ", type='"
				+ type
				+ '\''
				+ ", name='"
				+ name
				+ '\''
				+ ", tagUrl='"
				+ tagUrl
				+ '\''
				+ ", numberOfUses="
				+ numberOfUses
				+ '}';
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(type);
		dest.writeString(name);
		dest.writeString(tagUrl);
		dest.writeInt(numberOfUses);
	}
}