package com.example.brpper.redditviewer;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;


@Entity()
public class RedditPostDB {

	@Id(autoincrement = true)
	private Long id;

	@Unique
	private String internalPostId;

	private String name;

	private String url;

	private String author;

	private Long subredditId;

	@Generated(hash = 1018992384)
	public RedditPostDB(Long id, String internalPostId, String name, String url,
			String author, Long subredditId) {
		this.id = id;
		this.internalPostId = internalPostId;
		this.name = name;
		this.url = url;
		this.author = author;
		this.subredditId = subredditId;
	}

	@Generated(hash = 270648309)
	public RedditPostDB() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Long getSubredditId() {
		return this.subredditId;
	}

	public void setSubredditId(Long subredditId) {
		this.subredditId = subredditId;
	}


	public String getInternalPostId() {
		return this.internalPostId;
	}

	public void setInternalPostId(String internalPostId) {
		this.internalPostId = internalPostId;
	}

}